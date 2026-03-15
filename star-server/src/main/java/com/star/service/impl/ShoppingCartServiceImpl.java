package com.star.service.impl;

import com.star.context.BaseContext;
import com.star.dto.ShoppingCartDTO;
import com.star.entity.Dish;
import com.star.entity.Setmeal;
import com.star.entity.ShoppingCart;
import com.star.mapper.DishMapper;
import com.star.mapper.SetmealMapper;
import com.star.mapper.ShoppingCartMapper;
import com.star.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */

    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入购物车中的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //如果已经存在了，只需要将数量加一
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1); // update shopping_cart set number = ? where id =>
            shoppingCartMapper.updateNumberById(cart);
        } else {
            //如果不存在，需要插入一条购物车数据

            //判断本次添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //本次添加到购物车的是菜品
                Dish dish = dishMapper.selectById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            } else {
                // 本次添加到这个购物车的是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());


            shoppingCartMapper.insert(shoppingCart);

        }

    }

    /**
     * 查看购物车
     *
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        //获取当前用户的id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    public void cleanShoppingCart() {
        //获取当前微信用户的id
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 减少购物车
     * @param shoppingCartDTO
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入购物车中的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //如果已经存在了，只需要将数量减一
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            Integer number = cart.getNumber();
            if (number > 1) {
                cart.setNumber(number - 1); // update shopping_cart set number = ? where id =>
                shoppingCartMapper.updateNumberById(cart);
            } else {
                //如果数量等于1，直接删除这条数据
                shoppingCartMapper.deleteByUserId(userId);
            }
        } else {
            throw new RuntimeException("购物车数据异常");
        }

    }
}