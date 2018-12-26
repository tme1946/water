package com.ptteng.water.system.service.impl;

import com.ptteng.water.system.mapper.DepartmentMapper;
import com.ptteng.water.system.mapper.GradeMapper;
import com.ptteng.water.system.mapper.PerformanceMapper;
import com.ptteng.water.system.pojo.Department;
import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.system.pojo.Grade;
import com.ptteng.water.system.pojo.Performance;
import com.ptteng.water.system.service.DepartmentService;
import com.ptteng.water.system.service.PerformanceService;
import com.ptteng.water.system.service.TemplateService;
import com.ptteng.water.util.ExportExcelUtil;
import com.ptteng.water.util.PerformanceTree;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.util.ArrayList;

@Service
@Slf4j
public class PerformanceServiceImpl implements PerformanceService {

    @Resource
    private PerformanceMapper performanceMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    private TemplateService templateService;

    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    public Performance findOnePerformance(Long id) {
        log.info("查找绩效 id= {}", id);
        Performance performance = performanceMapper.findOnePerformance(id);
        if(performance == null) {
            return null;
        }
        ArrayList<Element> elements = templateService.findOneTemplate(performance.getTemplateId()).getElements();
        ArrayList<Element> fourthElement = gradeMapper.listGrade(id);
        log.info("四级节点 size = {}", fourthElement.size());
        // 封装属性
        for(Element fourth : fourthElement) {
            for(Element element : elements) {
                if(element.getId().equals(fourth.getId()) && fourth.getGrade() != null) {
                    element.setGrade(fourth.getGrade());
                    element.setActual(fourth.getActual());
                    element.setDiff();
                }
            }
        }
        performance.setElements(elements);
        return performance;
    }

    @Override
    public ArrayList<Performance> findPerformances(String name, Integer year, Integer status, Integer type, ArrayList<Department> departments) {
        log.info("查找绩效列表, name = {}, year = {}, status = {}, type = {}", name, year, status, type);
        if(departments != null && departments.size() == 0){
            return null;
        }else {
            return performanceMapper.findPerformances(null, year, status, departments);
        }

    }

    @Override
    public ArrayList<Performance> findDepartmentPerformance(Long departmentId) {
        return performanceMapper.findDepartmentPerformance(departmentId);
    }

    @Override
    public Long addPerformance(Performance performance) {
        log.info("新增绩效 performance = {}", performance);
        Long currentTime = System.currentTimeMillis();
        performance.setCreateAt(currentTime);
        performance.setUpdateAt(currentTime);
        performanceMapper.addPerformance(performance);
        Long id = performance.getId();
        for(Element element : performance.getElements()) {
            Grade grade = new Grade();
            grade.setElementId(element.getId());
            grade.setActual(element.getActual());
            grade.setGrade(element.getGrade());
            grade.setPerformanceId(id);
            grade.setCreateAt(System.currentTimeMillis());
            gradeMapper.addGrade(grade);
        }
        return id;
    }

    @Override
    public Long updatePerformance(Performance performance, ArrayList<Element> elements) {
        log.info("编辑绩效");
        Long currentTime = System.currentTimeMillis();
        performance.setUpdateAt(currentTime);
        Long result = performanceMapper.updatePerformance(performance);
        for(Element element : elements) {
            Grade grade = new Grade();
            grade.setElementId(element.getId());
            grade.setActual(element.getActual());
            grade.setGrade(element.getGrade());
            grade.setPerformanceId(performance.getId());
            grade.setUpdateAt(System.currentTimeMillis());
            gradeMapper.updateGrade(grade);
        }
        return result;
    }

    @Override
    public Long updateStatus(Performance performance) {
        log.info("编辑绩效状态");
        Long currentTime = System.currentTimeMillis();
        performance.setUpdateAt(currentTime);
        return performanceMapper.updatePerformance(performance);
    }

    @Override
    public Long deletePerformance(Long id) {
        log.info("删除绩效 id = {}", id);
        gradeMapper.deleteGrade(id);
        return performanceMapper.deletePerformance(id);
    }

    @Override
    public ArrayList<Integer> findYears() {
        return performanceMapper.findYears();
    }

    @Override
    public String exportExcel(Performance performance) {
        String name = performance.getName();
        ArrayList<Element> elements = PerformanceTree.getTree(performance.getElements());
        ArrayList<Element> tree = PerformanceTree.getRoots(elements);
        long random = System.currentTimeMillis();
        try {
            ExportExcelUtil exportExcelUtil = new ExportExcelUtil(amqpTemplate);
            exportExcelUtil.getWb(tree, name, random);
            return name + random +".xlsx";
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
