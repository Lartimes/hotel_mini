package com.lartimes.hotel.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description: 分页查询返回对象
 * @since 2024/2/5 21:43
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PageResult-分页查询返回对象")
public class PageResult<T>  implements Serializable {
    // 数据列表
    @Schema(name = "items" , description = "返回的数据List<T>")
    private List<T> items;

    //总记录数
    @Schema(name = "counts" , description = "数据库count(*)")
    private long counts;

    //当前页码
    @Schema(name = "page" , description = "当前页码")
    private long page;

    //每页记录数
    @Schema(name = "pageSize" , description = "每页记录数")
    private long pageSize;

}
