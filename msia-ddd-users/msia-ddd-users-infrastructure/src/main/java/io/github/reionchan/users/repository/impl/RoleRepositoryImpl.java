package io.github.reionchan.users.repository.impl;

import com.alibaba.cola.exception.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.reionchan.users.database.dataobject.RoleDO;
import io.github.reionchan.users.database.mapper.RoleMapper;
import io.github.reionchan.users.repository.IRoleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Set;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Repository
public class RoleRepositoryImpl implements IRoleRepository {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public boolean allRolesExist(String roleStr) {
        Assert.isTrue(isNotEmpty(roleStr), "角色逗号分隔的字符串不能为空");
        String[] roles = roleStr.split(SPLIT);
        Set<String> roleNameSet = Set.copyOf(Arrays.stream(roles).toList());
        Assert.isTrue(roleNameSet.size() == roles.length, roleStr + " 存在重复值");

        QueryWrapper<RoleDO> roleQW = new QueryWrapper<>();
        long count = roleMapper.selectCount(roleQW.lambda().in(RoleDO::getRoleName, roleNameSet));
        return count == roles.length;
    }
}
