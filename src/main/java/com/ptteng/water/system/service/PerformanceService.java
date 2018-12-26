package com.ptteng.water.system.service;

import com.ptteng.water.system.pojo.Department;
import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.system.pojo.Performance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface PerformanceService {

    Performance findOnePerformance(Long id);

    ArrayList<Performance> findPerformances(String name, Integer year, Integer status, Integer type, ArrayList<Department> departments);

    ArrayList<Performance> findDepartmentPerformance(Long departmentId);

    Long addPerformance(Performance performance);

    Long updatePerformance(Performance performance, ArrayList<Element> elements);

    Long deletePerformance(Long id);

    ArrayList<Integer> findYears();

    String exportExcel(Performance performance);

    Long updateStatus(Performance performance);
}
