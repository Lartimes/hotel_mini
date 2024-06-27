package com.lartimes.hotel.service.startup;

import com.lartimes.hotel.service.RoomScheduledService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/21 19:32
 */

@Component
@Order(1)
public class RoomsGroupRunner implements ApplicationRunner {

    private final RoomScheduledService roomScheduledService;

    public RoomsGroupRunner(RoomScheduledService roomScheduledService) {
        this.roomScheduledService = roomScheduledService;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
            roomScheduledService.groupByHash();
    }




}

