package com.star.controller.user;

import com.star.constant.StatusConstant;
import com.star.entity.Dish;
import com.star.result.Result;
import com.star.service.DishService;
import com.star.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Set;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        //构造redis key，规则dish_分类id
        String redisKey = "dish_" + categoryId;

        // 先从redis中查询，如果存在直接返回
        List<DishVO> cacheResult = (List<DishVO>) redisTemplate.opsForValue().get(redisKey);
        if (cacheResult != null && !cacheResult.isEmpty()) {

            log.info("从redis中查询菜品，key={}, result={}", redisKey, cacheResult);
            return Result.success(cacheResult);
        }



        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        //如果不存在，从数据库中查询，并将结果存入redis
        cacheResult = dishService.listWithFlavor(dish);
        log.info("从数据库中查询菜品，key={}, result={}", redisKey, cacheResult);
        redisTemplate.opsForValue().set(redisKey, cacheResult);

        return Result.success(cacheResult);
    }

}
