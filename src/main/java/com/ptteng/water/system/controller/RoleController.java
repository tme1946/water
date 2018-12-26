package com.ptteng.water.system.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.ptteng.water.system.pojo.*;
import com.ptteng.water.system.pojo.Module;
import com.ptteng.water.system.service.ManagerService;
import com.ptteng.water.system.service.RoleModuleService;
import com.ptteng.water.system.service.RoleService;
import com.ptteng.water.util.CookieUtil;
import com.ptteng.water.util.DataUtil;
import com.ptteng.water.util.ListPageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class RoleController {
    @Resource
    RoleService roleService;

    @Resource
    RoleModuleService roleModuleService;

    @Resource
    ManagerService managerService;

    /**
     * 新增角色
     * 角色name 不允许为空
     * by   2018/9/30 19:14
     **/
    @PostMapping(value = "/a/u/role")
    public Response addRole(HttpServletRequest request,
                            @RequestBody Role role) {
        log.info("addRole==========" + role);

        try {
            Long mid = CookieUtil.getUid(request);
            //验证参数
            if (role.getName() == null || role.getName().length() == 0) {
                return new Response<>(-1, "角色名为空", role);
            }


            List<Role> check = roleService.selectByName(role.getName());
            log.info("check=======" + check.size());
            if (!CollectionUtils.isEmpty(check)) {
                return new Response<>(-1, "创建角色名已存在", role);
            }
            if (role.getPermissionsSet().size() == 0 || role.getPermissionsSet() == null) {
                return new Response<>(-1, "权限列表为空", role);
            }
          log.info("新增角色开始");
            //新增角色
            //此处有坑 sqlite存储string类型的数组[]前需要加转义符 /
            role.setCreateBy(mid);
            role.setUpdateBy(mid);
            role.setId(null);
            role.setStatus(Role.STATUS_USING);
            log.info("role ={}",role);
            Long roleId = roleService.insertRole(role);
            log.info("add role success======", roleId);

//            增加角色模块关联记录
            List<RoleModule> roleModules = new ArrayList<RoleModule>();
            for (Long moduleId : role.getPermissionsSet()) {
                if (null != moduleId && !"".equals(moduleId)) {
                    RoleModule roleModule = new RoleModule();
                    roleModule.setCreateBy(mid);
                    roleModule.setUpdateBy(mid);
                    roleModule.setRoleId(roleId);
                    roleModule.setModuleId(Long.valueOf(moduleId));
                    roleModules.add(roleModule);
                }
            }
            roleModuleService.insertList(roleModules);
            return new Response<>(0, "success", roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(-10000, "系统错误", null);
        }
    }

    /**
     * 角色列表
     * <p>
     * by   2018/9/30 19:14
     **/
    @GetMapping(value = "/a/u/role/list")
    public Response findIdsList(HttpServletRequest request,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("listRole====page==" + page + "====size=======" + size);

        try {
            Long mid = CookieUtil.getUid(request);
            log.info("mid=======" + mid);
            //验证参数

            List<Long> roleIdsList = roleService.findIdsList();
            log.info("roleList====" + roleIdsList.size());

//            PageHelper.startPage(page, size);
//            PageInfo<Long> pageInfo = new PageInfo<>(roleIdsList);
            ListPageUtil<Long> pageInfo = new ListPageUtil<>(roleIdsList,page,size);
            ListData listData = new ListData<Module>();
            listData.setPage(pageInfo.getNowPage());
            listData.setSize(pageInfo.getPageSize());
            listData.setTotal(pageInfo.getTotalCount());
            listData.setNext(pageInfo.getNextPage());
            listData.setTotalPage(pageInfo.getTotalPage());
            listData.setList(pageInfo.getPagedList());

            Response response = new Response(0, "success", listData);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 获取角色列表
     *
     * @Author inh
     * @Description 根据ids获取角色列表
     * @Date 15:27 2018/10/6
     * @Param
     */
    @GetMapping(value = "/a/u/multi/role")
    public Response findIdsList(HttpServletRequest request,
                                @RequestParam(value = "ids", required = true) List<Long> ids) {
        log.info("listRoleByIds====");

        try {
            Long mid = CookieUtil.getUid(request);
            log.info("mid=======" + mid);
            //验证参数
            if (DataUtil.isNullOrEmpty(mid)
                    || DataUtil.isZero(mid)) {
                return new Response<>(-1, "用户错误", null);
            }
            if (ids == null || ids.size() == 0) {
                return new Response<>(-1, "缺少参数", ids);
            }


            List<Role> roleList = roleService.findListbyIds(ids);
            log.info("roleList====" + roleList.size());

            PageHelper.startPage(1, roleList.size());
            PageInfo<Role> pageInfo = new PageInfo<>(roleList);

            ListData listData = new ListData<Role>();
            listData.setPage(pageInfo.getPageNum());
            listData.setSize(pageInfo.getPageSize());
            listData.setTotal(pageInfo.getTotal());
            listData.setNext(pageInfo.isHasNextPage());
            listData.setTotalPage(pageInfo.getPages());
            listData.setList(roleList);

            Response response = new Response(0, "success", listData);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }


    /**
     * 删除角色
     * by   2018/9/30 19:26
     **/
    @DeleteMapping(value = "/a/u/role/{id}")
    public Response deleteRole(HttpServletRequest request,
                               @PathVariable("id") Long id) {
        log.info("deleteRole=====" + id);

        try {
            //验证参数

            Role roleOld = roleService.selectById(id);
            if (DataUtil.isNullOrEmpty(roleOld)) {
                return new Response<>(-1, "角色不存在", id);
            }

            List<Manager> managerList = managerService.selectByRoleAndStatus(id, null);
            log.info("managerList====="+managerList);
            if (managerList != null && managerList.size() > 0) {
                return new Response<>(-1, "有用户正在使用此角色", id);
            }

            Long result = roleService.deleteById(id);
            Response response = new Response(0, "success", result);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 修改角色
     * name 不可为空
     * at 2018/9/30 19:38
     **/
    @PutMapping(value = "/a/u/role/{rid}")
    public Response updateRole(@PathVariable("rid") Long rid, HttpServletRequest request,
                               @RequestBody Role role) {
        log.info("updateRole====rid==" + rid + "====role====" + role);
        log.info("params id ====" + rid);

        try {
            Long mid = CookieUtil.getUid(request);
            log.info("updateRole=====mid=========" + mid);
            //验证参数

            Role roleOld = roleService.selectById(rid);
            if (DataUtil.isNullOrEmpty(roleOld)) {
                return new Response<>(-1, "角色不存在", rid);
            }

            if (DataUtil.isNullOrEmpty(role.getName())) {
                return new Response<>(-1, "角色名为空", role);
            }

            List<Role> check = roleService.selectByName(role.getName());
            log.info("check=======" + check.size());
            if (check.size()>1) {
                return new Response<>(-1, "角色名已存在", null);
            }
            roleOld.setName(role.getName());

            if (CollectionUtils.isEmpty(role.getPermissionsSet())) {
                return new Response<>(-1, "权限列表为空", null);
            }
            roleOld.setPermissionsSet(role.getPermissionsSet());
            roleOld.setUpdateBy(mid);
            log.info("changge role ====" + roleOld.getId());
            Long result = roleService.updateByPrimaryKeySelective(roleOld);
            log.info("updateRole result====" + result);

            //因为是多对对的关联表，所以修改要执行删除——新增这样的逻辑
            //删除关联表里的关联关系
            boolean deleteResult = roleModuleService.deleteByRid(roleOld.getId());
            if (!deleteResult) {
                return new Response<>(-1, "角色id错误", null);
            }

            //新增角色模块关联记录
            List<RoleModule> roleModules = new ArrayList<RoleModule>();
            for (Long moduleId : role.getPermissionsSet()) {
                if (null != moduleId && !"".equals(moduleId)) {
                    RoleModule roleModule = new RoleModule();
                    roleModule.setRoleId(roleOld.getId());
                    roleModule.setCreateBy(mid);
                    roleModule.setUpdateBy(mid);
                    roleModule.setModuleId(Long.valueOf(moduleId));
                    roleModules.add(roleModule);
                }
            }
            boolean finalResult = roleModuleService.insertList(roleModules);
            return new Response(0, "success", finalResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 获取单个角色详情
     * at 2018/9/30 19:54
     **/
    @GetMapping(value = "/a/u/role/{rid}")
    public Response getRole(HttpServletRequest request,
                            @PathVariable("rid") Long rid) {

        log.info("getRole=======" + rid);

        try {

            Role role = roleService.selectById(rid);
            log.info("role=========" + role);
            Response response = new Response(0, "success", role);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 返回单个角色及权限
     *
     * @Author inh
     * @Description
     * @Date 15:55 2018/10/9
     * @Param
     */
    @GetMapping(value = "/a/u/role/{rid}/module")
    public Response getRoleModule(HttpServletRequest request,
                                  @PathVariable("rid") Long rid) {

        log.info("getRoleModule=======" + rid);

        try {
            if(rid == null){
                return new Response<>(-1, "缺少rid", null);
            }
            else{
                if(rid.longValue() == -1L){
                    Long mid = CookieUtil.getUid(request);
                    Manager manager = this.managerService.selectByPrimaryKey(mid);
                    rid = manager.getRoleId();
                }

                Role role = roleService.selectById(rid);
                if(role == null){
                    return new Response<>(-1, "角色不存在", null);
                }else{
                    List<Long> roleModuleIds =  roleModuleService.getRoleModuleIdsByRid(rid);
                    List<RoleModule> roleModules =  roleModuleService.getObjectsByIds(roleModuleIds);

                    List<Long> moduleIds = new ArrayList<Long>();

                    for (RoleModule rm : roleModules) {
                        moduleIds.add(rm.getModuleId());

                    }

                    log.info("get role data is " + role);
                    Map<String, Object> data = new HashMap<>();
                    data.put("mids",moduleIds);
                    data.put("role",role);
                    Map<String, Object> map = new HashMap<>();
                    map.put("data", data);
                    Response response = new Response(0, "success", map);
                    return response;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }
}
