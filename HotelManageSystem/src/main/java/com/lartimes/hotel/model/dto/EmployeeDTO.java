package com.lartimes.hotel.model.dto;

import com.lartimes.hotel.model.po.AdminUser;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
public class EmployeeDTO extends AdminUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String sex;

    private LocalDate birthDay;


    private String phoneNum;

    private Double wage;

    private String workPosition;

    private String idCard;

}
