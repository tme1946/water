package com.ptteng.water.system.mapper;


import com.ptteng.water.system.pojo.RoleModule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleModuleMapper {

    int insert(RoleModule record);

    int insertSelective(RoleModule record);


    int updateByPrimaryKeySelective(RoleModule record);


    boolean insertList(List<RoleModule> roleModules);

    boolean deleteByRid(Long rid);

    List<Long> findMidsByRid(Long rid);

    List<Long> findidsByRid(Long rid);

    List<RoleModule> getObjectsByIds(List<Long> list);
}