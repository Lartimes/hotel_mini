package com.lartimes.hotel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.hotel.common.*;
import com.lartimes.hotel.mapper.AdminUserMapper;
import com.lartimes.hotel.mapper.EmployeeMapper;
import com.lartimes.hotel.model.dto.AdminUserDto;
import com.lartimes.hotel.model.dto.EmployeeDTO;
import com.lartimes.hotel.model.dto.QueryEmployeeDto;
import com.lartimes.hotel.model.po.AdminUser;
import com.lartimes.hotel.model.po.Employee;
import com.lartimes.hotel.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final AdminUserMapper adminUserMapper;

    @Lazy
    private EmployeeService beanProxy;

    @Autowired
    public EmployeeServiceImpl(EmployeeMapper employeeMapper, AdminUserMapper adminUserMapper) {
        this.employeeMapper = employeeMapper;
        this.adminUserMapper = adminUserMapper;
    }

    @Autowired
    public void setBeanProxy(EmployeeService beanProxy) {
        this.beanProxy = beanProxy;
    }

    /**
     * 进行员工注册或更改信息
     *
     * @param employeeDTO
     * @return
     */
    @Transactional
    @Override
    public Result registerOrModifyEmp(EmployeeDTO employeeDTO) {
        String idCard = employeeDTO.getIdCard();
        LambdaQueryWrapper<Employee> query = new LambdaQueryWrapper<Employee>().eq(idCard != null, Employee::getIdCard, idCard);
        Employee condition = employeeMapper.selectOne(query);
        if (condition != null) {
            BeanUtils.copyProperties(employeeDTO, condition);
            int count = employeeMapper.updateById(condition);
            AdminUser adminUser = new AdminUser();
            adminUser.setEmpId(condition.getId());
            adminUser.setName(employeeDTO.getAccountName());
            adminUser.setPassword(employeeDTO.getPassword());
            count += beanProxy.updateAccount(adminUser);
            if (count != 2) {
                log.error("级联更新失败");
                HotelException.cast("更新Employee失败");
            }
            return ResultReturn.success();
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        int pk = employeeMapper.insert(employee);
        if (pk < 0) {
            log.error("insert emp 失败");
            HotelException.cast("新增Emp失败");
        }
        AdminUser adminUser = new AdminUser();
        adminUser.setName(employeeDTO.getAccountName());
        adminUser.setPassword(employeeDTO.getPassword());
        String name = adminUser.getAccountName();
        Long l = adminUserMapper.selectCount(new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getName, name));
        if (l == 0) {
            adminUser.setEmpId(pk);
            adminUserMapper.insert(adminUser);
            return ResultReturn.success();
        }
        HotelException.cast("账号已存在");
        return ResultReturn.failure(0, "账号已存在");
    }

    @Transactional
    @Override
    public int updateAccount(AdminUser adminUser) {
        LambdaQueryWrapper<AdminUser> queryAdmin = new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getEmpId, adminUser.getEmpId());
        return adminUserMapper.update(adminUser, queryAdmin);
    }

    @Transactional
    @Override
    public Result deleteEmployee(Integer id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            log.debug("不存在该员工 ");
            return ResultReturn.success();
        }
        employeeMapper.deleteById(id);
        adminUserMapper.delete(new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getEmpId, id));
        log.debug("删除成功 :{}", employee);
        return ResultReturn.success();
    }

    @Override
    public Result login(AdminUserDto adminUserDto) {
        String name = adminUserDto.getName();
        String password = adminUserDto.getPassword();
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<AdminUser>()
                .eq(name != null, AdminUser::getName, name)
                .eq(password != null, AdminUser::getPassword, password);

        Long l = adminUserMapper.selectCount(queryWrapper);
        if (l > 0) {
            return ResultReturn.success();
        }
        return  ResultReturn.failure(0 , "不存在该用户");
    }

    @Override
    public PageResult<Employee> getEmpByPage(PageParams pageParams, QueryEmployeeDto employeeDto) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(employeeDto.getName()) , Employee::getName , employeeDto.getName() )
                .like(StringUtils.isNotBlank(employeeDto.getWorkPosition()) , Employee::getWorkPosition , employeeDto.getWorkPosition());
        IPage<Employee> page = new Page<>(pageParams.getPageNo()  , pageParams.getPageSize());
        IPage<Employee> employeeIPage = employeeMapper.selectPage(page , queryWrapper);
        List<Employee> records = employeeIPage.getRecords();
        long total = employeeIPage.getTotal();
        return  new PageResult<>(records , total , pageParams.getPageNo() , pageParams.getPageSize());
    }
}
