package com.star.mapper;

import com.star.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(@Param("username") String username);

    /**
     * 插入员工
     * @param employee
     */
    @Insert("INSERT INTO employee " +
            "(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "VALUES " +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")

    void insert(Employee employee);
}
