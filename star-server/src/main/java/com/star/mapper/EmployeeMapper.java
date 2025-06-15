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
    // language=SQL
    @Insert("insert into employee ")
    void insert(@Param("employee") Employee employee);
}
