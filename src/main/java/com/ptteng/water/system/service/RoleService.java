package com.ptteng.water.system.service;

import com.ptteng.water.system.pojo.Role;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * by   2018/9/30 15:15
 */

public interface RoleService {

    @GetMapping("/a/u/multi/role")
    List<Role> findListbyIds(@RequestParam("ids") List<Long> list);

    @DeleteMapping("/a/u/role/{id}")
    Long deleteById(@PathVariable("id") Long id);

    @PutMapping("/a/u/role/{id}")
    Long updateById(@PathVariable("id") Long id, Role role);

    @GetMapping("/a/u/role/{id}")
    Role selectById(@PathVariable("id") Long id);

    @PutMapping("/a/u/role")
    Long updateByPrimaryKeySelective(@RequestBody Role role);

    @PostMapping("/a/u/role")
    Long insertRole(Role role);

    @GetMapping("/a/u/role/search")
    List<Role> selectByName(@RequestParam("name") String name);

    @GetMapping("/a/u/role/list")
    List<Long> findIdsList();
}
