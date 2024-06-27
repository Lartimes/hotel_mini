package com.lartimes.hotel.controller;

import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.common.ResultReturn;
import com.lartimes.hotel.model.dto.AdminUserDto;
import com.lartimes.hotel.model.dto.QueryEmployeeDto;
import com.lartimes.hotel.model.po.Employee;
import com.lartimes.hotel.model.dto.EmployeeDTO;
import com.lartimes.hotel.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("employee")
@Tag(name = "EmployeeController", description = "员工controller")
public class EmployeeController {

    private final EmployeeService employeeService;



    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @Operation(summary = "对员工进行新增注册/修改", parameters = {@Parameter(name = "employeeDTO", description = "员工信息DTO")})
    @PostMapping("/registerOrUpdate")
    @ResponseBody
    public Result registerEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return employeeService.registerOrModifyEmp(employeeDTO);
    }

    @Operation(summary = "删除员工", parameters = {@Parameter(name = "id", description = "id")})
    @GetMapping("/deleteEmp/{id}")
    @ResponseBody
    public Result deleteEmp(@PathVariable("id") Integer id) {
        return employeeService.deleteEmployee(id);
    }


    //    TODO 分页查询/员工/房间/客人/物品等等  重构
    @Operation(summary = "查询所有员工")
    @GetMapping("/empList")
    @ResponseBody
    public Result<List<Employee>> getEmps() {
        List<Employee> list = employeeService.list();
        return ResultReturn.success(list);
    }

    @Operation(summary = "分页查询" )
    @PostMapping("/pages/emp")
    public PageResult<Employee> getEmpsByPage(PageParams pageParams ,
                                              @RequestBody(required = false) QueryEmployeeDto employeeDto){
        return employeeService.getEmpByPage(pageParams ,employeeDto );
    }



    @Operation(summary = "员工登录", parameters = {@Parameter(name = "adminUserDto", description = "管理用户dto")})
    @PostMapping("/login")
    @ResponseBody
    public Result login(AdminUserDto adminUserDto) {
        return employeeService.login(adminUserDto);
    }


}
