package com.ptteng.water.system.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.ptteng.water.system.pojo.ListData;
import com.ptteng.water.system.pojo.Module;
import com.ptteng.water.system.pojo.Response;
import com.ptteng.water.system.service.ModuleService;
import com.ptteng.water.util.CookieUtil;
import com.ptteng.water.util.DataUtil;
import com.ptteng.water.util.ListPageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
public class ModuleController {
    @Resource
    private ModuleService moduleService;

    @GetMapping(value = "/a/u/module/")
    public Response listModule(HttpServletRequest request,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "size", required = false, defaultValue = "10") Integer pageSize) {
        log.info("getModuleList start=====");
        log.info("入参 page={}，size={}",pageNum,pageSize);
        try {
            List<Long> idsList = moduleService.findIdsList();
            ListPageUtil<Long> pageInfo = new ListPageUtil<>(idsList,pageNum,pageSize);
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
     * 新增模块
     * 模块name 不允许为空
     * by   2018/9/30 19:14
     **/
    @PostMapping(value = "/a/u/module")
    public Response addModule(@RequestBody Module module, HttpServletRequest request) {

        log.info("creat module========" + module);

        try {
            Long mid = CookieUtil.getUid(request);
            //验证参数
            if (module.getName() == null || module.getName().length() == 0) {
                return new Response<>(-1, "模块名称为空", module);
            }
            module.setCreateBy(mid);
            module.setUpdateBy(mid);
            module.setId(null);
            Long result = moduleService.insertModule(module);
            log.info("add module success========" + result);
            Response response = new Response(0, "success", result);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(-10000, "系统错误", null);
        }
    }

    /**
     * 删除模块
     * by   2018/9/30 19:26
     **/
    @DeleteMapping(value = "/a/u/module/{id}")
    public Response deleteModule(@PathVariable("id") Long id, HttpServletRequest request) {

        log.info("deleteModule==========" + id);

        try {
            Module check = moduleService.selectById(id);
            if (DataUtil.isNullOrEmpty(check)) {
                return new Response<>(-1, "模块不存在", id);
            }
            Long result = moduleService.deleteById(id);
            log.info("delete success=====" + result);
            Response response = new Response(0, "success", result);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 修改模块
     * name 不可为空
     * at 2018/9/30 19:38
     **/
    @PutMapping(value = "/a/u/module/{id}")
    public Response updateModule(@PathVariable("id") Long id, HttpServletRequest request,
                                 @RequestBody Module module) {
        log.info("update module========" + id);
        try {
            Long mid = CookieUtil.getUid(request);

            String name = module.getName();
            if (name == null || name.length() == 0 || name.length() > 15) {
                return new Response<>(-1, "模块名称异常", name);
            }

            Module check = moduleService.selectById(id);
            if (DataUtil.isNullOrEmpty(check)) {
                return new Response<>(-1, "模块不存在", id);
            }

            module.setId(id);
            module.setCreateAt(check.getCreateAt());
            module.setCreateBy(check.getCreateBy());
            module.setUpdateBy(mid);

            Long result = moduleService.updateByPrimaryKeySelective(module, id);
            log.info("update success=====" + result);
            Response response = new Response(0, "success", result);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }

    /**
     * 获取单个模块详情
     * at 2018/9/30 19:54
     **/
    @GetMapping(value = "/a/u/module/{id}")
    public Response getModule(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("get one module=====" + id);

        try {
            Module module = moduleService.selectById(id);
            if (DataUtil.isNullOrEmpty(module)) {
                return new Response<>(-1, "模块不存在", id);
            }
            log.info("get one module=====" + module);
            Response response = new Response(0, "success", module);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }


    /**
     * 批量获取模块详细信息
     *
     * @Author inh
     * @Description
     * @Date 16:48 2018/10/9
     * @Param
     */
    @GetMapping(value = "/a/u/multi/module")
    public Response findIdsList(HttpServletRequest request,
                                @RequestParam(value = "ids", required = true) List<Long> ids) {
        log.info("listModuleByIds====");
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
            List<Module> moduleList = moduleService.findListbyIds(ids);
            log.info("moduleList====" + moduleList.size());

            PageHelper.startPage(1, moduleList.size());
            PageInfo<Module> pageInfo = new PageInfo<>(moduleList);

            ListData listData = new ListData<Module>();
            listData.setPage(pageInfo.getPageNum());
            listData.setSize(pageInfo.getPageSize());
            listData.setTotal(pageInfo.getTotal());
            listData.setNext(pageInfo.isHasNextPage());
            listData.setTotalPage(pageInfo.getPages());
            listData.setList(moduleList);

            Response response = new Response(0, "success", listData);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-10000, "系统错误", null);
        }
    }
}
