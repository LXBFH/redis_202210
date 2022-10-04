package com.lixubo.redis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lixubo.redis.entity.UserEntity;
import com.lixubo.redis.mapper.UserMapper;
import com.lixubo.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 用户信息
 *
 * @author lixubo
 * @email lxb68@139.com
 * @date 2022-10-01 23:53:58
 */
@RestController
@RequestMapping("redis")
public class UserController {
    @Autowired
    private UserService userService;

    //将redisTemplate对象注入进来
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Resource
    private UserMapper userMapper;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public UserEntity select(@PathVariable Long id) {
        return userService.queryById(id);
    }

    @GetMapping("/list")
    public List<UserEntity> selectAll() {
        List<UserEntity> list = userMapper.selectAll();
        return list;
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/insert")
    public int set(@RequestBody UserEntity userEntity) throws JsonProcessingException {
        int insert = userMapper.insert(userEntity);
        // 手动序列化
        String json = mapper.writeValueAsString(userEntity);
        //写入数据
        stringRedisTemplate.opsForValue().set("log:userEntity:" + userEntity.getId(), json);
        String entity = stringRedisTemplate.opsForValue().get("log:userEntity:" + userEntity.getId());
        //反序列化
        UserEntity userEntity1 = mapper.readValue(entity, UserEntity.class);
        System.out.println("反序列化：" + userEntity1);
        return insert;
    }

    @PostMapping("/insertH")
    public int setH(@RequestBody UserEntity userEntity) {
        int insert = userMapper.insert(userEntity);

        stringRedisTemplate.opsForHash().put("log:userEntity", ""+userEntity.getId(), ""+userEntity);
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("log:userEntity" );

        System.out.println("entries:" + entries);
        return insert;
    }

    @PostMapping("/updateLog")
    public UserEntity updateLog(@RequestBody UserEntity userEntity){
        return userService.update(userEntity);
    }

}
