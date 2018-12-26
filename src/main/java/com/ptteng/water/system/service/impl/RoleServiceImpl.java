package com.ptteng.water.system.service.impl;

import com.ptteng.water.system.mapper.RoleMapper;
import com.ptteng.water.system.pojo.Role;
import com.ptteng.water.system.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 * by   2018/9/30 15:16
 */
@Service
@Slf4j
public class RoleServiceImpl  implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> findListbyIds(List<Long> list) {
        List<Role> roleList =roleMapper.findListbyIds(list);
        return roleList;
    }

    @Override
    public List<Long> findIdsList() {
        List<Long> roleIdsList =roleMapper.findIdsList();
        return roleIdsList;
    }

    @Override
    public Long deleteById(Long id) {
        roleMapper.deleteById(id);
        return id;
    }

    @Override
    public Long updateById( Long id,Role role) {
        role.setUpdateAt(System.currentTimeMillis());
        roleMapper.updateById(role,id);
        return role.getId();
    }

    @Override
    public Role selectById(Long id) {
        log.info("selectById========"+id);

        Role role = roleMapper.findById(id);
        return role;
    }

    @Override
    public Long insertRole(Role role) {
        role.setId(null);
        role.setUpdateAt(System.currentTimeMillis());
        role.setCreateAt(System.currentTimeMillis());
        roleMapper.insertRole(role);
        return role.getId();
    }

    @Override
    public Long updateByPrimaryKeySelective(Role role) {
        log.info("updateByPrimaryKeySelective======"+role);
        role.setUpdateAt(System.currentTimeMillis());
        roleMapper.updateByPrimaryKeySelective(role);
        return role.getId();
    }

    @Override
    public List<Role> selectByName(String name) {
        return roleMapper.selectByName(name);
    }
}
