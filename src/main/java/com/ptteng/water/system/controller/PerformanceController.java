package com.ptteng.water.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.ptteng.water.system.pojo.*;
import com.ptteng.water.system.service.DepartmentService;
import com.ptteng.water.system.service.PerformanceService;
import com.ptteng.water.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

@RestController
@Slf4j
public class PerformanceController {
    @Resource
    private PerformanceService performanceService;

    @Resource
    private DepartmentService departmentService;

    @GetMapping("/a/u/performance/list")
    public Response findAllPerformance(String name, Integer year, Integer status, Integer type,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("查找绩效列表 name = {}, year = {}, status = {}, type = {}", name, year, status, type);
        log.info("page = {} size = {}", page, size);
        ArrayList<Department> departments = departmentService.findAllDepartment(name, type, null, null, null, null, null, null);
        PageHelper.startPage(page, size);
        ArrayList<Performance> performances = performanceService.findPerformances(null, year, status, type, departments);
        PageInfo pageInfo = new PageInfo(performances);
        log.info("查找绩效列表 size = {}", performances.size());
        // 封装父部门名称
        for(Object performance : pageInfo.getList()) {
            Long departmentId = ((Performance)performance).getDepartmentId();
            String parentName = departmentService.findOneDepartment(departmentId).getParentName();
            if(parentName != null){
                ((Performance)performance).setParentName(parentName);
            }
        }
        return new Response<>(0, "success", pageInfo);
    }

    @GetMapping("/a/u/performance/year/list")
    public Response findYears() {
        log.info("查找绩效年份");
        ArrayList<Integer> years = performanceService.findYears();
        log.info("绩效年份, size = {}", years.size());
        return new Response<>(0, "success", years);
    }

    @GetMapping("/a/u/performance/all/list")
    public Response findAll() {
        log.info("查找全部绩效");
        ArrayList<Performance> performances = performanceService.findPerformances(null, null, null, null, null);
        log.info("全部绩效 size = {}", performances.size());
        return new Response<>(0, "success", performances);
    }

    @GetMapping("/a/u/performance/{id}")
    public Response findOnePerformance(@PathVariable("id") Long id) {
        log.info("查询绩效 id = {}", id);
        Performance performances = performanceService.findOnePerformance(id);
        if(performances == null) {
            return new Response<>(-1, "id无效", id);
        }
        return new Response<>(0, "success", performances);
    }

    @PostMapping("/a/u/performance")
    public Response addPerformance(@RequestBody @Validated Performance performance, BindingResult error, HttpServletRequest request) {
        Long mid = CookieUtil.getUid(request);
        log.info("新增绩效 mid = {}", mid);
        if(error.hasErrors()){
            String msg = Objects.requireNonNull(error.getFieldError()).getDefaultMessage();
            log.info("参数校验失败 msg = {}", msg);
            return new Response<>(-1, "参数校验失败", msg);
        }
        Department department = departmentService.findOneDepartment(performance.getDepartmentId());
        ArrayList<Element> elements = performance.getElements();
        Double sum = 0.0;
        for(Element element : elements) {
            sum += element.getGrade();
        }
        log.info("绩效总分为 sum = {}", sum);
        performance.setGrade(sum);
        Long id = performanceService.addPerformance(performance);
        log.info("新增绩效 id = {}", id);
        department.setGrade(sum);
        department.setStatus(performance.getStatus());
        departmentService.updateDepartment(department.getId(), department);
        log.info("部门设置绩效 grade = {}", sum);
        return new Response<>(0, "success", id);
    }

    @PutMapping("/a/u/performance/{id}")
    public Response updatePerformance(@PathVariable("id") Long id, @RequestBody Performance performance, HttpServletRequest request) {
        Long mid = CookieUtil.getUid(request);
        log.info("更新绩效 mid = {}", mid);
        Performance check = performanceService.findOnePerformance(id);
        if(check == null) {
            log.info("编辑绩效错误, id = {}", id);
            return new Response<>(-1, "无效id", id);
        }
        Department department = departmentService.findOneDepartment(check.getDepartmentId());
        Double sum = 0.0;
        for(Element element : performance.getElements()) {
            if(element.getGrade() != null){
                sum += element.getGrade();
            }
        }
        log.info("更新绩效总分 sum = {}", sum);
        performance.setId(id);
        performance.setGrade(sum);
        Long result = performanceService.updatePerformance(performance, performance.getElements());
        log.info("编辑绩效成功 id = {}, mid = {}", id, mid);
        department.setGrade(sum);
        department.setStatus(performance.getStatus());
        departmentService.updateDepartment(department.getId(), department);
        log.info("部门设置绩效 grade = {}", sum);
        return new Response<>(0, "success", result);
    }

    @DeleteMapping("/a/u/performance/{id}")
    public Response deletePerformance(@PathVariable("id") Long id, HttpServletRequest request) {
        Long mid = CookieUtil.getUid(request);
        log.info("删除绩效 id = {}, mid = {}", id, mid);
        Performance check = performanceService.findOnePerformance(id);
        if(check == null) {
            log.info("删除绩效错误, id = {}", id);
            return new Response<>(-1, "无效id", id);
        }
        Long result = performanceService.deletePerformance(id);
        log.info("删除绩效成功 id = {}, mid = {}", id, mid);
        return new Response<>(0, "success", result);
    }

    @GetMapping("/a/u/performance/download/{id}")
    public Response downFile(@PathVariable("id") Long id) {
        log.info("导出绩效表格文件 id = {}", id);
        Performance performance = performanceService.findOnePerformance(id);
        String result = performanceService.exportExcel(performance);
        if(result == null) {
            log.info("导出绩效表格文件失败 id = {}\", id");
            return new Response<>(-1, "导出表格失败", id);
        }
        String url = "/download/" + result;
        log.info("导出表格成功 url = ", url);
        return new Response<>(0, "导出表格成功", url);
    }
}
