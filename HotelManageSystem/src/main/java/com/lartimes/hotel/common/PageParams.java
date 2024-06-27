package com.lartimes.hotel.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lartimes
 * @version 1.0
 * @description: 分页参数包装类
 * @since 2024/2/5 21:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PageParams-分页查询参数")
public class PageParams {

    //当前页码
    @Schema(name = "pageNo" ,description = "第几页")
    private Long pageNo = 1L;

    //每页记录数默认值
    @Schema(name = "pageSize" ,description = "每一页数量")
    private Long pageSize =10L;

}
