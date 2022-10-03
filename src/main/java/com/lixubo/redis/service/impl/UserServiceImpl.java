package com.lixubo.redis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lixubo.redis.entity.UserEntity;
import com.lixubo.redis.mapper.UserMapper;
import com.lixubo.redis.service.UserService;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

}