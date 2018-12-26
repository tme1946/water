package com.ptteng.water.system.service;


import com.ptteng.water.system.pojo.Module;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO
 * by   2018/9/30 18:51
 */
public interface ModuleService {

    @GetMapping("/a/u/module/list")
    List<Module> findList();

    @GetMapping("/a/u/module/")
    List<Long> findIdsList();

    @DeleteMapping("/a/u/module/{id}")
    Long deleteById(@PathVariable("id") Long id);

    @GetMapping("/a/u/module/{id}")
    Module selectById(@PathVariable("id") Long id);

    @GetMapping("/a/u/module/many")
    List<Module> getObjectsByIds(@RequestParam(name = "mids") List<Long> mids);

    @PutMapping("/a/u/module/{id}")
    Long updateByPrimaryKeySelective(Module module, @PathVariable("id") Long id);

    @PostMapping("/a/u/module")
    Long insertModule(Module module);

    @GetMapping(value = "/a/u/multi/module")
    List<Module> findListbyIds(@RequestParam(name = "ids") List<Long> ids);

}
