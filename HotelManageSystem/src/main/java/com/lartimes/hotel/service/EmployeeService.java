package com.lartimes.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.model.dto.QueryEmployeeDto;
import com.lartimes.hotel.model.po.AdminUser;
import com.lartimes.hotel.model.dto.AdminUserDto;
import com.lartimes.hotel.model.po.Employee;
import com.lartimes.hotel.model.dto.EmployeeDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-06-18
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 对员工进行注册
     * @param employeeDTO
     * @return
     */
    Result registerOrModifyEmp(EmployeeDTO employeeDTO);

    /**
     * 对员工账号进行修改或者添加
     * @param adminUser
     * @return
     */
    public int updateAccount(AdminUser adminUser) ;

    /**
     * 根据员工id删除员工
     * @param id
     * @return
     */
    Result deleteEmployee(Integer id);


    /**
     * User/PWD  登录
     * @param adminUserDto
     * @return
     */
    Result login(AdminUserDto adminUserDto);


    /**
     * 分页查询
     * @param pageParams
     * @param employeeDto
     * @return
     */
    PageResult<Employee> getEmpByPage(PageParams pageParams, QueryEmployeeDto employeeDto);

}
