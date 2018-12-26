package com.ptteng.water.system.service.impl;


import com.ptteng.water.system.mapper.ManagerMapper;
import com.ptteng.water.system.pojo.Manager;
import com.ptteng.water.system.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 * by   2018/9/30 12:54
 */

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {
    @Resource
    private ManagerMapper managerMapper;

    @Override
    public Manager selectByPrimaryKey(Long id) {
        return managerMapper.selectByPrimaryKey(id);
    }


    @Override
    public List<Manager> selectByName(String name) {
        return managerMapper.selectByName(name);
    }

    @Override
    public Long insertSelective(Manager record) {
        log.info("add manager======="+record);

        record.setId(null);
        record.setCreateAt(System.currentTimeMillis());
        record.setUpdateAt(System.currentTimeMillis());
        managerMapper.insertSelective(record);
        return record.getId();
    }


    @Override
    public List<Manager> selectByRoleAndStatus(@RequestParam(value = "roleID",required = false) Long roleID,
                                               @RequestParam(value = "status",required = false)String status) {
        log.info("selectByRoleAndStatus====roleID==="+roleID+"===status===="+status);
        return managerMapper.selectByRoleAndStatus(roleID,status);
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        return managerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Long updateByPrimaryKeySelective(Long id,Manager record) {
        record.setUpdateAt(System.currentTimeMillis());
        managerMapper.updateByPrimaryKeySelective(record);
        return record.getId();
    }

    @Override
    public List<Manager> findListbyIds(List<Long> ids) {
        List<Manager> managerList =managerMapper.findListbyIds(ids);
        return managerList;
    }
}
