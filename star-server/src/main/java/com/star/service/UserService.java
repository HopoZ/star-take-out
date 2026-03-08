package com.star.service;

import com.star.dto.UserLoginDTO;
import com.star.entity.User;

public interface UserService {
    /**
     * 微信用户登录
     *
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
