package com.ptteng.water.system.controller;

import com.ptteng.water.system.pojo.Department;
import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.system.pojo.Performance;
import com.ptteng.water.system.pojo.Response;
import com.ptteng.water.system.service.DepartmentService;
import com.ptteng.water.system.service.PerformanceService;
import com.ptteng.water.system.service.ReportService;
import com.ptteng.water.util.ReportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@Slf4j
public class ReportController {

    @Resource
    private ReportService reportService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private PerformanceService performanceService;

    @GetMapping("/a/u/report/tendency")
    public Response getTendency(String name) {
        if(name == null) {
            ArrayList<Department> departments = departmentService.findAllDepartment(null, null, null, null, null, null, null, null);
            name = departments.get(0).getName();
        }
        log.info("查找部门 {} 的绩效趋势", name);
        ArrayList<Performance> performances = reportService.findGrade(name);
        if(performances==null){
            return new Response<>(0, "success", new ArrayList<>());
        }
        log.info("绩效 size = {}", performances.size());
        return new Response<>(0, "success", performances);
    }

    @GetMapping("/a/u/report/best")
    public Response getBest(Integer year) {
        if(year == null) {
            ArrayList<Integer> years = performanceService.findYears();
            year = years.get(0);
        }
        log.info("查找 {} 年的最优指标", year);
        ArrayList<Element> elements = reportService.findBest(year);
        if(elements == null) {
            return new Response<>(0, "success", new ArrayList<>());
        }
        ArrayList<Element> result = ReportUtil.sort(elements);
        Collections.reverse(result);
        return new Response<>(0, "success", result.subList(0, 10));
    }

    @GetMapping("/a/u/report/worst")
    public Response getWorst(Integer year) {
        if(year == null) {
            ArrayList<Integer> years = performanceService.findYears();
            year = years.get(0);
        }
        log.info("查找 {} 年的最差指标", year);
        ArrayList<Element> elements = reportService.findBest(year);
        if(elements == null) {
            return new Response<>(0, "success", new ArrayList<>());
        }
        ArrayList<Element> result = ReportUtil.sort(elements);
        return new Response<>(0, "success", result.subList(0, 10));
    }

    @GetMapping("/a/u/report/rank")
    public Response getRank(Long parentId, Integer year,
                            @RequestParam(defaultValue = "1") Integer page) {
        if(year == null) {
            ArrayList<Integer> years = performanceService.findYears();
            year = years.get(0);
        }
        log.info("绩效排名 parentId = {} year = {}", parentId, year);
        Integer from = (page - 1) *50;
        Integer to = from + 50;
        ArrayList<Performance> performances = reportService.findRank(parentId, year, from, to);
        if(performances == null) {
            return new Response<>(0, "success", new ArrayList<>());
        }
        log.info("绩效 size = {}", performances.size());
        // 封装父部门名称
        for(Performance performance : performances) {
            Long departmentId = performance.getDepartmentId();
            String parentName = departmentService.findOneDepartment(departmentId).getParentName();
            if(parentName != null){
                performance.setParentName(parentName);
            }
        }
        log.info("绩效排名列表 size = {}", performances.size());
        return new Response<>(0, "success", performances);
    }
}
