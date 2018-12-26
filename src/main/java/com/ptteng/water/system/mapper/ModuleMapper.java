package com.ptteng.water.system.mapper;


import com.ptteng.water.system.pojo.Module;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModuleMapper {
    int deleteById(Long id);

    Long insertModule(Module module);

    int insertSelective(Module module);

    Module findById(Long id);

    int updateByPrimaryKeySelective(Module module);


    List<Module> findList();

    List<Module> getObjectsByIds(List<Long> mids);

    List<Long> findIdsList();

}