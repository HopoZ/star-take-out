package com.star.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.star.constant.MessageConstant;
import com.star.dto.UserLoginDTO;
import com.star.entity.User;
import com.star.exception.LoginFailedException;
import com.star.mapper.UserMapper;
import com.star.properties.WeChatProperties;
import com.star.service.UserService;
import com.star.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private final WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    public UserServiceImpl(WeChatProperties weChatProperties) {
        this.weChatProperties = weChatProperties;
    }

    /**
     * 微信用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenId(userLoginDTO);

        //判断当前openId是否为空
        if (openid == null || openid.isEmpty()) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前openId是否已经注册
        User user = userMapper.getByOpenid(openid);

        //如果没有注册，则自动注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);

        }
        return user;
    }

    /**
     * 调用微信登录接口，获取openid
     *
     * @param userLoginDTO
     * @return
     */
    private String getOpenId(UserLoginDTO userLoginDTO) {
        //调用微信登录接口，获取openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type", "authorization_code");
        log.info("调用微信登录接口，获取openid,请求参数:{}", map);
        String jsonResult = HttpClientUtil.doGet(WX_LOGIN_URL, map);

        JSONObject jsonObject = JSONObject.parseObject(jsonResult);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
