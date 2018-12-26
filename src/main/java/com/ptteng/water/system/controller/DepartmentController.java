package com.ptteng.water.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ptteng.water.system.pojo.Department;
import com.ptteng.water.system.pojo.Performance;
import com.ptteng.water.system.pojo.Response;
import com.ptteng.water.system.pojo.Template;
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
public class DepartmentController {
    @Resource
    private DepartmentService departmentService;

    @Resource
    private PerformanceService performanceService;

    @PostMapping("/a/u/department")
    public Response addDepartment(@RequestBody @Validated Department department, BindingResult error, HttpServletRequest request) {
        Long mid = CookieUtil.getUid(request);
        log.info("新增部门, department = {}, mid = {}", department, mid);
        if(error.hasErrors()){
            String msg = Objects.requireNonNull(error.getFieldError()).getDefaultMessage();
            log.info("参数校验错误 msg = {}", msg);
            return new Response<>(-1, msg, department);
        }
        // 封装参数
        department.setCreateBy(mid);
        department.setUpdateBy(mid);
        Long id = departmentService.saveDepartment(department);
        log.info("新增部门成功, id = {}", id);
        return new Response<>(0, "success", id);
    }

    @DeleteMapping("/a/u/department/{id}")
    public Response deleteDepartment(@PathVariable("id") Long id, HttpServletRequest request) {
        Long mid = CookieUtil.getUid(request);
        log.info("删除部门 id = {}, mid = {}", id, mid);
        Department check = departmentService.findOneDepartment(id);
        if(check == null) {
            log.info("删除部门异常 id = {}", id);
            return new Response<>(-1, "无效id", id);
        }
        Long result = departmentService.deleteDepartment(id);
        log.info("删除部门成功 id = {}, mid = {}", id, mid);
        return new Response<>(0, "success", result);
    }

    @PutMapping("/a/u/department/{id}")
    public Response updateDepartment(@PathVariable("id") Long id, @RequestBody @Validated Department department, BindingResult error, HttpServletRequest request) {
        Long mid = CookieUtil.getUid(request);
        log.info("编辑部门 id = {}, department = {}, mid = {}", id, department, mid);
        if(error.hasErrors()){
            String msg = Objects.requireNonNull(error.getFieldError()).getDefaultMessage();
            log.info("参数校验错误 msg = {}", msg);
            return new Response<>(-1, msg, department);
        }
        // 校验是否为有限id
        Department check = departmentService.findOneDepartment(id);
        if(check == null) {
            log.info("编辑部门异常 id = {}", id);
            return new Response<>(-1, "无效id", id);
        }
        if(check.getTemplateId()!=null && !check.getTemplateId().equals(department.getTemplateId())){
            log.info("换绑模板，更新成绩");
            department.setGrade(0d);
        }
        // 封装参数
        department.setCreateBy(mid);
        department.setUpdateBy(mid);
        Long result = departmentService.updateDepartment(id, department);
        log.info("更新部门成功 id = {}, mid = {}", id, mid);
        return new Response<>(0, "success", result);
    }

    @GetMapping("/a/u/department/{id}")
    public Response findOneDepartment(@PathVariable("id") Long id) {
        log.info("查找部门 id = {}", id);
        Department department = departmentService.findOneDepartment(id);
        return new Response<>(0, "success", department);
    }

    @GetMapping("/a/u/department/list")
    public Response findAllDepartment(String name, Integer type,
                                      Long parentId, Integer status,
                                      Double budgetLow, Double budgetHigh,
                                      Double gradeLow, Double gradeHigh,
                                      @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取部门列表 name = {}, type = {}, parentId = {}, status = {}, budgetLow = {}, budgetHigh = {}, gradeLow = {}, gradeHigh = {}", name, type, parentId, status, budgetLow, budgetHigh, gradeLow, gradeHigh);
        PageHelper.startPage(page, size);
        ArrayList<Department> departments = departmentService.findAllDepartment(name, type, parentId, status, budgetLow, budgetHigh, gradeLow, gradeHigh);
        PageInfo pageInfo = new PageInfo(departments);
        log.info("部门列表 size = {}", departments.size());
        return new Response<>(0, "success", pageInfo);
    }

    @GetMapping("/a/u/department/parent/list")
    public Response findParents(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取父部门列表");
        PageHelper.startPage(page, size);
        ArrayList<Department> parents = departmentService.findPartents();
        PageInfo pageInfo = new PageInfo(parents);
        log.info("父部门列表 size = {}", parents.size());
        return new Response<>(0, "success", pageInfo);
    }

    @GetMapping("/a/u/department/parent/all")
    public Response findParents() {
        log.info("获取全部父部门");
        ArrayList<Department> parents = departmentService.findPartents();
        log.info("父部门列表 size = {}", parents.size());
        return new Response<>(0, "success", parents);
    }

    @GetMapping("/a/u/department/all/list")
    public Response findAll() {
        log.info("获取全部部门");
        ArrayList<Department> departments = departmentService.findAllDepartment(null, null, null, null, null, null, null, null);
        log.info("全部部门 size = {}", departments.size());
        return new Response<>(0, "success", departments);
    }

    @GetMapping("/a/u/department/year/list/{id}")
    public Response findYears(@PathVariable("id") Long id) {
        log.info("查找部门 id = {} 已有绩效年份列表", id);
        ArrayList<Performance> performances = performanceService.findDepartmentPerformance(id);
        ArrayList<Integer> years = new ArrayList<>();
        performances.forEach(item -> years.add(item.getYear()));
        log.info("已有绩效年份列表 size = {}", years.size());
        return new Response<>(0, "success", years);
    }
}
