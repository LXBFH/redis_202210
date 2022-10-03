package com.lixubo.redis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lixubo.redis.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户信息
 * 
 * @author lixubo
 * @email lxb68@139.com
 * @date 2022-10-01 23:53:58
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
  UserEntity selectById(Long id);
  List<UserEntity> selectAll();
}
