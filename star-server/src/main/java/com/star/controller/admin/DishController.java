package com.star.controller.admin;

import com.star.dto.DishDTO;
import com.star.dto.DishPageQueryDTO;
import com.star.entity.Dish;
import com.star.result.PageResult;
import com.star.result.Result;
import com.star.service.DishService;
import com.star.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        //清理redis中对应分类的菜品缓存，key规则dish_分类id
        String redisKey = "dish_" + dishDTO.getCategoryId();
        cleaneCache(redisKey);
        return Result.success();

    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除菜品，ids={}", ids);
        dishService.deletebatch(ids);

        //清理redis中所有套餐缓存
        cleaneCache("dish_*");
        return Result.success();
    }


    /**
     * 根据id查询菜品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品信息")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品信息，id={}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息，dishDTO={}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        //清理redis中所有套餐缓存
        cleaneCache("dish_*");
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 根据id修改菜品起售状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("根据id修改菜品起售状态")
    public Result startOrStop(@PathVariable Integer status,Long id) {
        log.info("根据id修改菜品起售状态，status={}, id={}", status, id);
        dishService.startOrStop(status, id);

        //清理redis中所有套餐缓存
        cleaneCache("dish_*");

        return Result.success();
    }


    /**
     * 清理redis对应缓存
     * @param pattern
     */
    private void cleaneCache(String pattern) {
        //查询所有符合pattern的key
        Set keys = redisTemplate.keys(pattern);
        log.info("清理redis缓存，pattern={}, keys={}", pattern, keys);

        //删除这些key
        redisTemplate.delete(keys);
    }

}
