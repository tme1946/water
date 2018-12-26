package com.ptteng.water.system.service.impl;

import com.ptteng.water.system.mapper.DepartmentMapper;
import com.ptteng.water.system.mapper.ElementMapper;
import com.ptteng.water.system.mapper.GradeMapper;
import com.ptteng.water.system.mapper.PerformanceMapper;
import com.ptteng.water.system.pojo.Department;
import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.system.pojo.Performance;
import com.ptteng.water.system.service.ReportService;
import com.ptteng.water.util.ReportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private PerformanceMapper performanceMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    private ElementMapper elementMapper;

    @Override
    public ArrayList<Performance> findGrade(String departmentName) {
        Department department = departmentMapper.findByName(departmentName);
        if(department==null){
            return null;
        }else {
            return performanceMapper.findGradeByDepartmentId(department.getId());

        }
    }

    @Override
    public ArrayList<Element> findBest(Integer year) {
        ArrayList<Performance> performances = performanceMapper.findPerformances(null, year, 2, null);
        log.info("{} 年的绩效， performance = {}", year, performances);
        ArrayList<Element> elements;
        if(performances.size() == 0){
            return null;
        }else {
            elements = gradeMapper.listElement(performances);
            for(Element element : elements) {
                Element item = elementMapper.findById(element.getId());
                element.setName(item.getName());
                element.setExpectGrade(item.getExpectGrade());
                element.setPercent();
            }
        }
        HashMap<Long, Double> out = ReportUtil.elementPercent(elements);
        ArrayList<Element> sortElements = new ArrayList<>();
        for(Long key : out.keySet()){
            Element element = elementMapper.findById(key);
            element.setPercent(out.get(key));
            sortElements.add(element);
        }
        return sortElements;
    }

    @Override
    public ArrayList<Performance> findRank(Long parentId, Integer year, Integer from, Integer to) {
        ArrayList<Department> departments = departmentMapper.listDepartmentByQuery(null, null, parentId, null, null, null, null, null);
        log.info("部门 = {}", departments.size());
        if(parentId == null){
            for (int i = 0; i < departments.size(); i++){
                log.info("删除父部门 parent = {}", departments.get(i).getParentId());
                if(departments.get(i).getParentId() == 0L){
                    departments.remove(i);
                    i--;
                }
            }
        }
        log.info("部门列表 size = {}", departments.size());
        if(departments.size() == 0){
            return null;
        }else {
            return performanceMapper.findRank(departments, year, from, to);
        }
    }
}
