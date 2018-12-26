package com.ptteng.water.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.ptteng.water.system.pojo.Department;
import com.ptteng.water.system.pojo.Performance;
import com.ptteng.water.system.pojo.Response;
import com.ptteng.water.system.pojo.Template;
import com.ptteng.water.system.service.DepartmentService;
import com.ptteng.water.system.service.TemplateService;
import com.ptteng.water.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@Slf4j
public class TemplateController {
    @Resource
    private TemplateService templateService;

    @GetMapping("/a/u/template/list")
    public Response findTemplateList(String name,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取模板列表");
        PageHelper.startPage(page, size);
        ArrayList<Template> templates = templateService.findAllTemplate(name);
        log.info("模板列表 size = {}", templates.size());
        PageInfo pageInfo = new PageInfo(templates);
        return new Response<>(0, "success", pageInfo);
    }

    @GetMapping("/a/u/template/all")
    public Response findAllTemplate() {
        log.info("获取全部模板列表");
        ArrayList<Template> templates = templateService.findAllTemplate(null);
        return new Response<>(0, "success", templates);
    }

    @GetMapping("/a/u/template/{id}")
    public Response findOneTemplate(@PathVariable("id") Long id) {
        log.info("查找模板 id = {}", id);
        Template template = templateService.findOneTemplate(id);
        return new Response<>(0, "success", template);
    }

    @PostMapping("/a/u/template")
    public Response addTemplate(@RequestBody Template template, HttpServletRequest request) {
        Long mid = CookieUtil.getUid(request);
        log.info("新增模板 mid = {}", mid);
        // 封装参数
        template.setCreateBy(mid);
        template.setUpdateBy(mid);
        Long id = templateService.saveTemplate(template, template.getElements());
        log.info("新增模板成功 id = {}, mid = {} ", id, mid);
        return new Response<>(0, "success", id);
    }

    @PutMapping("/a/u/template/{id}")
    public Response updateTemplate(@PathVariable("id") Long id, @RequestBody Template template, HttpServletRequest request) {
        Long mid = CookieUtil.getUid(request);
        log.info("编辑模板 id = {}, mid = {}", id, mid);
        Template check = templateService.findOneTemplate(id);
        if(check == null) {
            log.info("编辑模板异常 id = {}", id);
            return new Response<>(-1, "无效id", id);
        }
        // 封装参数
        template.setCreateBy(mid);
        template.setUpdateBy(mid);
        Long result = templateService.updateTemplate(id, template);
        log.info("编辑模板成功 id = {}, mid = {}", id, mid);
        return new Response<>(0, "success", result);
    }

    @DeleteMapping("/a/u/template/{id}")
    public Response deleteTemplate(@PathVariable("id") Long id) {
        log.info("删除模板 id = {}", id);
        Template check = templateService.findOneTemplate(id);
        if(check == null) {
            log.info("删除模板异常 id = {}", id);
            return new Response<>(-1, "无效id", id);
        }
        if (templateService.beUsed(id)) {
            log.info("模板被使用，不能删除 id = {}", id);
            return new Response<>(-1, "模板被绑定", id);
        }
        Long result = templateService.deleteTemplete(id);
        return new Response<>(0, "success", result);
    }
}
