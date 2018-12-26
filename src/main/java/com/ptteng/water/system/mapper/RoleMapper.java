package com.ptteng.water.system.mapper;

import com.ptteng.water.system.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    int deleteById(@Param("id") Long id);

    Long insertRole(Role role);

    int insertSelective(Role role);

    Role findById(Long id);

    int updateByPrimaryKeySelective(Role role);

    int updateById(Role role, Long id);

    List<Role> findListbyIds(List<Long> list);

    List<Role> selectByName(String name);

    List<Long> findIdsList();
}