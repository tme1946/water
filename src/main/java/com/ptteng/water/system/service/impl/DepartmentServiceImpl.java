package com.ptteng.water.system.service.impl;

import com.ptteng.water.system.mapper.DepartmentMapper;
import com.ptteng.water.system.mapper.GradeMapper;
import com.ptteng.water.system.mapper.PerformanceMapper;
import com.ptteng.water.system.mapper.TemplateMapper;
import com.ptteng.water.system.pojo.Department;
import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.system.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private TemplateMapper templateMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    private PerformanceMapper performanceMapper;

    @Override
    public ArrayList<Department> findAllDepartment(String name, Integer type,
                                                   Long parentId, Integer status,
                                                   Double budgetLow, Double budgetHigh,
                                                   Double gradeLow, Double gradeHigh) {
        log.info("查询部门列表 name = {}, type = {}, parentId = {}, status = {}",name, type, parentId, status);
        ArrayList<Department> departments = departmentMapper.listDepartmentByQuery(name, type, parentId, status, budgetLow, budgetHigh, gradeLow, gradeHigh);
        log.info("部门列表 size = {}", departments.size());
        for (Department department : departments) {
            if(department != null) {
                setProperties(department);
            }
        }
        return departments;
    }

    @Override
    public Department findOneDepartment(Long id) {
        log.info("查询部门 id = {}", id);
        Department department = departmentMapper.findById(id);
        // 判断是否是父部门
        if(department != null) {

            setProperties(department);
        }
        return department;
    }

    @Override
    public Long updateDepartment(Long id, Department department) {
        log.info("更新部门 department = {}", department);
        department.setId(id);
        department.setUpdateAt(System.currentTimeMillis());
        return departmentMapper.updateDepartment(department);
    }

    @Override
    public Long saveDepartment(Department department) {
        Long currentTime = System.currentTimeMillis();
        department.setUpdateAt(currentTime);
        department.setCreateAt(currentTime);
        departmentMapper.addDepartment(department);
        return department.getId();
    }

    @Override
    public Long deleteDepartment(Long id) {
        log.info("删除部门 id = {}", id);
        if(departmentMapper.findById(id).getParentId() == 0){
            log.info("删除父部门，删除相应的子部门");
            departmentMapper.deleteByParent(id);
        }
        performanceMapper.deleteByDepartmentId(id);
        return departmentMapper.deleteDepartment(id);
    }

    @Override
    public ArrayList<Department> findPartents() {
        log.info("查找父部门");
        return departmentMapper.findParents();
    }

    @Override
    public void setProperties(Department department) {
        Long parentId = department.getParentId();
        if(parentId != 0){
            log.info("父节点id");
            String parentName = departmentMapper.findById(parentId).getName();
            department.setParentName(parentName);
        }
        Long templateId = department.getTemplateId();
        if(templateId != null){
            String templateName = templateMapper.findById(templateId).getName();
            department.setTemplateName(templateName);
        }
    }
}
