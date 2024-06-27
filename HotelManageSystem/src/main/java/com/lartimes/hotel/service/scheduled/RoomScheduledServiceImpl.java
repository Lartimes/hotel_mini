package com.lartimes.hotel.service.scheduled;

import com.alibaba.fastjson2.JSON;
import com.lartimes.hotel.mapper.CheckinInfoMapper;
import com.lartimes.hotel.model.po.CheckinInfo;
import com.lartimes.hotel.service.GuestService;
import com.lartimes.hotel.service.RoomScheduledService;
import com.lartimes.hotel.service.RoomService;
import com.lartimes.hotel.service.mq.MQProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/22 9:40
 */
@Slf4j
@Service
public class RoomScheduledServiceImpl implements RoomScheduledService {
    private final RoomService roomService;
    private final RedisTemplate<String, String> redisTemplate;
    private final MQProducerService mqProducerService;
    private final CheckinInfoMapper checkinInfoMapper;
    private final GuestService guestService;

    public RoomScheduledServiceImpl(RedisTemplate<String, String> redisTemplate, RoomService roomService, MQProducerService mqProducerService, CheckinInfoMapper checkinInfoMapper, GuestService guestService) {
        this.redisTemplate = redisTemplate;
        this.roomService = roomService;
        this.mqProducerService = mqProducerService;
        this.checkinInfoMapper = checkinInfoMapper;
        this.guestService = guestService;
    }

    @Override
    @Scheduled(cron = "0 0 8,12,16 * * ? ")
    public void groupByHash() {
        List<List<Integer>> lists = roomService.groupByHash();
        lists.forEach(list -> {
            String jsonString = JSON.toJSONString(list);
            for (Integer key : list) {
                redisTemplate.opsForValue().set(String.valueOf(key), jsonString);
            }
        });
    }

    @Scheduled(cron = "0 30 11 * * ?")
    @Override
    public void checkLeavingRooms() {
//   检测所有今日过期的房间
        LocalDate now = LocalDate.now();
        List<CheckinInfo> destList = checkinInfoMapper.selectList(null).stream().filter(checkinInfo -> checkinInfo.getCheckoutDate().equals(now)).toList();
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        destList.forEach(info -> {
                    Integer guestId = info.getGuestId();
                    Integer roomId = info.getRoomId();
                    List<Integer> arr = map.get(roomId);
                    arr.add(guestId);
                    map.put(roomId, map.getOrDefault(roomId, Collections.singletonList(guestId)));
                }

        );
        String jsonString = JSON.toJSONString(map);
        log.debug("json： {}", jsonString);
        SendResult tag3 = mqProducerService.sendTagMsg(jsonString, "tag3");
        int count = 3;
        while (!tag3.getSendStatus().equals(SendStatus.SEND_OK)){
            count--;
            tag3 = mqProducerService.sendTagMsg(jsonString, "tag3");
            if(count<0){
                log.error("发送广播通知失败，请通知管理员");
                break;
            }
        }

    }


}
