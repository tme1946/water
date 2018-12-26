package com.ptteng.water.system.service.impl;

import com.ptteng.water.system.mapper.RoleModuleMapper;
import com.ptteng.water.system.pojo.RoleModule;
import com.ptteng.water.system.service.RoleModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class RoleModuleServiceImpl implements RoleModuleService {

    @Resource
    private RoleModuleMapper roleModuleMapper;


    //批量插入
    @Override
    public boolean insertList(List<RoleModule> roleModules) {
        log.info("insert role module List========" + roleModules.size());

        long currentTimeMillis = System.currentTimeMillis();
        for (RoleModule roleModule : roleModules) {
            roleModule.setCreateAt(currentTimeMillis);
            roleModule.setUpdateAt(currentTimeMillis);
        }
        boolean result = roleModuleMapper.insertList(roleModules);
        return result;
    }

    //根据role id删除关联记录
    @Override
    public boolean deleteByRid(Long rid) {
        log.info("deleteByRid======="+rid);
        return roleModuleMapper.deleteByRid(rid);
    }

    //根据role id获取所有的module id
    @Override
    public List<Long> getMidsByRid(Long rid) {
        log.info("getMidsByRid======="+rid);
        List<Long> mids = roleModuleMapper.findMidsByRid(rid);
        log.info("mids========"+mids);
        return mids;
    }

    //更新单条数据
    @Override
    public Long updateOne(Long id,RoleModule roleModule) {
        log.info("updateById======="+roleModule);
        int result = roleModuleMapper.updateByPrimaryKeySelective(roleModule);
        Long finalResult = Long.parseLong(result+"");
        return finalResult;
    }

    @Override
    public List<Long> getRoleModuleIdsByRid(Long rid) {
        log.info("getRoleModuleIdsByRid======="+rid);
        List<Long> ids = roleModuleMapper.findidsByRid(rid);
        log.info("ids========"+ids);
        return ids;
    }

    @Override
    public List<RoleModule> getObjectsByIds(List<Long> list) {
        log.info("getRoleModuleIdsByRid======="+list);
        List<RoleModule> roleModules = roleModuleMapper.getObjectsByIds(list);
        log.info("roleModules========"+roleModules);
        return roleModules;
    }
}
