package com.star.service;

import com.star.dto.DishDTO;
import com.star.dto.DishPageQueryDTO;
import com.star.result.PageResult;
import com.star.vo.DishVO;

public interface DishService {
    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deletebatch(Long[] ids);

    /**
     * 根据id查询菜品和对应的口味
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 更新菜品和对应的口味
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);
}
