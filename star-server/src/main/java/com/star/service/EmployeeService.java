package com.star.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.star.constant.PasswordConstant;
import com.star.constant.StatusConstant;
import com.star.dto.EmployeeDTO;
import com.star.dto.EmployeeLoginDTO;
import com.star.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);
}
