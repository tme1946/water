package com.ptteng.water.system.service.impl;


import com.ptteng.water.system.mapper.ModuleMapper;
import com.ptteng.water.system.pojo.Module;
import com.ptteng.water.system.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * by   2018/9/30 18:54
 */
@Service
@Slf4j
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ModuleMapper moduleMapper;

    @Override
    public List<Module> findList() {
        List<Module> moduleList = moduleMapper.findList();
        return moduleList;
    }

    @Override
    public List<Long> findIdsList() {
        List<Long> moduleIdsList = moduleMapper.findIdsList();
        return moduleIdsList;
    }

    @Override
    public Long deleteById(Long id) {
        moduleMapper.deleteById(id);
        return id;
    }

    @Override
    public Module selectById(Long id) {
        Module module = moduleMapper.findById(id);
        return module;
    }

    @Override
    public List<Module> getObjectsByIds(@RequestParam(name = "mids") List<Long> mids) {
        log.info("getObjectsByIds========" + mids);
        if (CollectionUtils.isEmpty(mids)) {
            return null;
        }
        List<Module> modules = moduleMapper.getObjectsByIds(mids);
        return modules;
    }

    @Override
    public Long insertModule(Module module) {
        module.setId(null);
        module.setUpdateAt(System.currentTimeMillis());
        module.setCreateAt(System.currentTimeMillis());
        moduleMapper.insertModule(module);
        return module.getId();
    }

    @Override
    public Long updateByPrimaryKeySelective(Module module, Long id) {
        module.setUpdateAt(System.currentTimeMillis());
        moduleMapper.updateByPrimaryKeySelective(module);
        return module.getId();
    }

    @Override
    public List<Module> findListbyIds(List<Long> ids) {
        List<Module> moduleList =moduleMapper.getObjectsByIds(ids);
        return moduleList;
    }


}
