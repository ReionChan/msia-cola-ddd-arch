package io.github.reionchan.users.repository.impl;

import com.alibaba.cola.exception.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.reionchan.users.convertor.UserEntityConvertor;
import io.github.reionchan.users.database.dataobject.UserDO;
import io.github.reionchan.users.database.mapper.UserMapper;
import io.github.reionchan.users.model.entity.User;
import io.github.reionchan.users.repository.IUserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;
import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Repository
public class UserRepositoryImpl implements IUserRepository {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserEntityConvertor userConvertor;

    @Override
    public boolean save(User user) {
        UserDO userDO = userConvertor.toDataObject(user);
        userDO.setRoles(user.getRoles());
        userDO.setStatus(user.getStatus());
        userDO.setCreateTime(new Date());
        userDO.setUpdateTime(new Date());
        boolean ret = userMapper.insert(userDO) > 0;
        if (ret) {
            user.setId(userDO.getId());
        }
        return ret;
    }

    @Override
    public boolean existsUserName(String userName) {
        Assert.isTrue(isNotEmpty(userName), "用户名不能为空");
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        return userMapper.exists(queryWrapper.lambda().eq(UserDO::getUserName, userName));
    }

    @Override
    public User queryByUserName(String userName) {
        Assert.isTrue(isNotEmpty(userName), "用户名不能为空");
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        UserDO userDO = userMapper.selectOne(queryWrapper.lambda().eq(UserDO::getUserName, userName));
        if(isEmpty(userDO)) {
            return null;
        }
        return userConvertor.toEntity(userDO);
    }

    @Override
    public User queryById(Long id) {
        Assert.isTrue(isNotEmpty(id) && id>0, "用户ID不能为空");
        UserDO userDO = userMapper.selectById(id);
        if(isEmpty(userDO)) {
            return null;
        }
        return userConvertor.toEntity(userDO);
    }

    @Override
    public boolean modify(User user) {
        UserDO toBeUpdate = userConvertor.toDataObject(user);
        toBeUpdate.setUpdateTime(new Date());
        return userMapper.updateById(toBeUpdate) > 0;
    }
}
