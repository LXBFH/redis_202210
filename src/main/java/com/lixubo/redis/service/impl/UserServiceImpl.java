package com.lixubo.redis.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lixubo.redis.entity.UserEntity;
import com.lixubo.redis.mapper.UserMapper;
import com.lixubo.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.lixubo.redis.constant.RedisConstants.*;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserEntity queryById(Long id) {

        String key = CACHE_LOG_KEY + id;
        //1.从redis查数据
        String logJson = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if (StrUtil.isNotBlank(logJson)) {
            //3.存在,直接返回
            UserEntity userEntity = JSONUtil.toBean(logJson, UserEntity.class);
            return userEntity;
        }
        //判断命中的是否是空值
        if (logJson != null) {
            //返回
            return null;
        }
        //4.不存在，根据id查数据库
        UserEntity userEntity = getById(id);
        //5.数据库不存在，返回错误
        if (userEntity == null) {
            //将空值写入redis
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        //6.数据库存在，写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userEntity), CACHE_LOG_TTL, TimeUnit.MINUTES);
        return userEntity;
    }

    @Override
    @Transactional
    public UserEntity update(UserEntity userEntity) {

        Long id = userEntity.getId();
        String key = CACHE_LOG_KEY + id;
        if (id == null) {
            return null;
        }
        //1.更新数据库
        updateById(userEntity);
        //2.删缓存
        stringRedisTemplate.delete(key);
        return userEntity;
    }
}