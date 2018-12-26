package com.ptteng.water.system.service;

import com.ptteng.water.system.pojo.RoleModule;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RoleModuleService {

    //增加角色模块关联
    @PostMapping("/a/u/rolemodule/list")
    boolean insertList(List<RoleModule> roleModules);

    //删除角色关联的所有模块
    @DeleteMapping("/a/u/rolemodule/{rid}")
    boolean deleteByRid(@PathVariable("rid") Long rid);

    //获取角色关联的所有模块id
    @GetMapping("/a/u/rolemodule/{rid}")
    List<Long> getMidsByRid(@PathVariable("rid") Long rid);

    //修改某一条关联记录
    @PutMapping("/a/u/rolemodule/{id}")
    Long updateOne(@PathVariable("id") Long id,
                   @RequestParam("roleModule") RoleModule roleModule);

    @GetMapping(value = "/a/u/role/{rid}/module")
    List<Long> getRoleModuleIdsByRid(@PathVariable("rid") Long rid);

    @GetMapping(value = "/a/u/rolemodule/list")
    List<RoleModule> getObjectsByIds(@RequestParam("ids") List<Long> list);
}
