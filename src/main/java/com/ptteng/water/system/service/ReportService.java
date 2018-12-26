package com.ptteng.water.system.service;

import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.system.pojo.Performance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface ReportService {
    // 部门年度绩效的变化趋势
    ArrayList<Performance> findGrade(String departmentName);

    ArrayList<Element> findBest(Integer year);

    ArrayList<Performance> findRank(Long parentId, Integer year, Integer page, Integer size);

}
