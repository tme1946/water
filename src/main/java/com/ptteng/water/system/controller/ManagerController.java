package com.ptteng.water.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.ptteng.water.system.pojo.ListData;
import com.ptteng.water.system.pojo.Manager;
import com.ptteng.water.system.pojo.Response;
import com.ptteng.water.system.pojo.Role;
import com.ptteng.water.system.service.ManagerService;
import com.ptteng.water.system.service.RoleService;
import com.ptteng.water.util.CookieUtil;
import com.ptteng.water.util.DataUtil;
import com.ptteng.water.util.ListPageUtil;
import com.ptteng.water.util.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class ManagerController {
    @Resource
    private ManagerService managerService;

    @Resource
    private RoleService roleService;


    @GetMapping(value = "/a/u/manager/list")
    public Response listManager(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                @RequestParam(value = "roleID", required = false) Long roleID,
                                @RequestParam(value = "status", required = false) String status) {
        log.info("listManager======page==" + page
                + "====size===" + size + "===roleID==" + roleID + "==status==" + status);

        try {
            List<Manager> managers = managerService.selectByRoleAndStatus(roleID, status);
            if (CollectionUtils.isEmpty(managers)) {
                Response response = new Response(0, "success", null);
                return response;
            }
            log.info("managers=====" + managers.size());
//            PageHelper.startPage(page, size);
//            PageInfo<Manager> pageInfo = new PageInfo<>(managers);

            List<Long> managerIds = new ArrayList<Long>();

            for (Manager manager : managers) {
                managerIds.add(manager.getId());

            }
            ListPageUtil<Long> pageInfo = new ListPageUtil<>(managerIds,page,size);
            ListData listData = new ListData<Manager>();
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
     * 新增用户
     * 模块name 不允许为空
     * by   2018/9/30 19:14
     **/
    @PostMapping(value = "/a/u/manager")
    public Response addManager(@RequestBody Manager manager, HttpServletRequest request) {
        log.info("addManager========={}" + manager);

        try {
            Long mid = CookieUtil.getUid(request);
            if (DataUtil.isNullOrEmpty(manager.getName())
                    || DataUtil.isNullOrEmpty(manager.getPwd())) {
                return new Response<>(-1, "用户名或密码不能为空", null);
            }
            //判断是否重名
            List<Manager> check = managerService.selectByName(manager.getName());
            log.info("check=======" + check.size());
            if (!CollectionUtils.isEmpty(check)) {
                return new Response<>(-1, "用户名已存在", null);
            }
            if (DataUtil.isNullOrEmpty(manager.getRoleId())) {
                return new Response<>(-1, "角色为空", null);
            }
            Role role = roleService.selectById(manager.getRoleId());
            if (role == null) {
                return new Response<>(-1, "roleId 无效", role);
            }

            manager.setCreateBy(mid);
            manager.setUpdateBy(mid);
            manager.setStatus(Manager.STATUS_USING);
            manager.setId(null);
            //对密码进行加密
            String encodepwd = PasswordUtils.encode(manager.getPwd());
            manager.setPwd(encodepwd);

            Long result = managerService.insertSelective(manager);
            log.info("result====" + result);
            Response response = new Response(0, "success", result);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 删除用户
     * by   2018/9/30 19:26
     **/
    @DeleteMapping(value = "/a/u/manager/{id}")
    public Response deleteManager(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("deleteManager======" + id);

        try {
            Long mid = CookieUtil.getUid(request);
            //验证参数
            if (DataUtil.isNullOrEmpty(mid)
                    || DataUtil.isZero(mid)) {
                return new Response<>(-1, "用户错误", null);
            }
            boolean result = managerService.deleteByPrimaryKey(id);
            log.info("result======" + result);

            return result ? new Response(0, "success", result)
                    : new Response(-1, "参数错误", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 修改用户
     * name 不可为空
     * at 2018/9/30 19:38
     **/
    @PutMapping(value = "/a/u/manager/{id}")
    public Response updateManager(@PathVariable("id") Long id, HttpServletRequest request,
                                  @RequestBody Manager manager) {

        Long mid = CookieUtil.getUid(request);

        String name = manager.getName();
        Long roleId = manager.getRoleId();
        String pwd = manager.getPwd();
        log.info("updateManager====== id={}，name={},roleId={}", mid, name, roleId);
        if (name.length() == 0 || pwd.length() == 0) {
            return new Response<>(-1, "用户名或密码为空", null);
        }
        List<Manager> check = managerService.selectByName(name);
        if (check != null && check.size() > 1) {
            return new Response<>(-1, "name重复", name);
        }
        Manager managerOld = managerService.selectByPrimaryKey(id);
        if (managerOld == null) {
            return new Response<>(-1, "用户不存在", managerOld);
        }
        Role role = roleService.selectById(roleId);
        if (role == null) {
            return new Response<>(-1, "roleId无效", role);
        }
        managerOld.setName(name);
        String pwdencode = PasswordUtils.encode(pwd);
        managerOld.setPwd(pwdencode);
        managerOld.setRoleId(roleId);
        managerOld.setUpdateBy(mid);
        Long result = managerService.updateByPrimaryKeySelective(id,managerOld);
        Response response = new Response(0, "success", result);
        return response;
    }

    /**
     * 获取单个用户详情
     * at 2018/9/30 19:54
     **/
    @GetMapping(value = "/a/u/manager/{id}")
    public Response getManager(@PathVariable("id") Long id, HttpServletRequest request) {

        Long mid = CookieUtil.getUid(request);
        Manager manager = managerService.selectByPrimaryKey(id);
        manager.setPwd("");

        Response response = new Response(0, "success", manager);
        return response;
    }

    /**
     * 根据ids获取用户列表
     *
     * @Author inh
     * @Description
     * @Date 1:08 2018/10/10
     * @Param
     */
    @GetMapping(value = "/a/u/multi/manager")
    public Response getManagerList(@RequestParam(value = "ids", required = true) List<Long> ids) {

        log.info("listRoleByIds====");
        log.info("ids====" + ids);

        try {
            //验证参数
            if (ids == null || ids.size() == 0) {
                return new Response<>(-1, "缺少参数", ids);
            }


            List<Manager> managerList = managerService.findListbyIds(ids);
            log.info("managerList====" + managerList.size());

            PageHelper.startPage(1, managerList.size());
            PageInfo<Manager> pageInfo = new PageInfo<>(managerList);

            ListData listData = new ListData<Manager>();
            listData.setPage(pageInfo.getPageNum());
            listData.setSize(pageInfo.getPageSize());
            listData.setTotal(pageInfo.getTotal());
            listData.setNext(pageInfo.isHasNextPage());
            listData.setTotalPage(pageInfo.getPages());
            listData.setList(managerList);

            Response response = new Response(0, "success", listData);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 修改密码
     *
     * @Author inh
     * @Description
     * @Date 15:50 2018/10/8
     * @Param
     */
    @PutMapping(value = "/a/u/pwd")
    public Response changePwd(
            @RequestBody JSONObject date,
            HttpServletRequest request) {

        log.info("change pwd");
        log.info("data====="+date);
        String oldPwd = date.getString("oldPwd");
        String newPwd = date.getString("newPwd");
        try {
            Long mid = CookieUtil.getUid(request);

            if (oldPwd == null || newPwd == null || oldPwd.trim().equals("") || newPwd.trim().equals("")) {
                log.info(" pwd  is null");
                return new Response<>(-1, "密码无效", newPwd);
            }

            if (oldPwd.length() > 200 || oldPwd.length() < 0 || newPwd.length() > 200 || newPwd.length() < 0) {
                log.info(" pwd  is null");
                return new Response<>(-1, "密码不合法", newPwd);
            }

            String oldPwdEncode = PasswordUtils.encode(oldPwd);
            Manager u = managerService.selectByPrimaryKey(mid);

            if (u.getPwd().equals(oldPwdEncode)) {
                String newenp = PasswordUtils.encode(newPwd);
                u.setPwd(newenp);
                u.setUpdateBy(mid);
                managerService.updateByPrimaryKeySelective(mid,u);

                Response response = new Response(0, "success", u);
                return response;
            } else {

                Response response = new Response(-1, "输入的旧密码有误", oldPwd);
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 根据RoleID查找用户
     *
     * @Author inh
     * @Description
     * @Date 16:39 2018/10/8
     * @Param
     */
    @GetMapping(value = "/a/u/role/{rid}/manager")
    public Response getManagerByRoleIDJson(@PathVariable("rid") Long rid,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        log.info("find manager where roleId===" + rid);
        try {

            Role role = roleService.selectById(rid);
            log.info("role is =====" + role);
            if (DataUtil.isNullOrEmpty(role)) {
                return new Response<>(-1, "角色不存在", rid);
            }

            List<Manager> managers = managerService.selectByRoleAndStatus(rid, "using");
            if (CollectionUtils.isEmpty(managers)) {
                Response response = new Response(0, "success", null);
                return response;
            }

            List<Long> managerIds = new ArrayList<Long>();

            for (Manager manager : managers) {
                managerIds.add(manager.getId());

            }

            log.info("managers=====" + managers.size());
            PageHelper.startPage(page, size);
            PageInfo<Manager> pageInfo = new PageInfo<>(managers);

            ListData listData = new ListData<Manager>();
            listData.setPage(pageInfo.getPageNum());
            listData.setSize(size);
            listData.setTotal(pageInfo.getTotal());
            listData.setNext(pageInfo.isHasNextPage());
            listData.setTotalPage(pageInfo.getPages());
            listData.setList(managerIds);

            Response response = new Response(0, "success", listData);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }
}
