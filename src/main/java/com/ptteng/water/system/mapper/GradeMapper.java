package com.ptteng.water.system.mapper;

import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.system.pojo.Grade;
import com.ptteng.water.system.pojo.Performance;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface GradeMapper {
    ArrayList<Element> listGrade(Long performanceId);

    void addGrade(Grade grade);

    void updateGrade(Grade grade);

    void deleteGrade(Long performanceId);

    ArrayList<Element> listElement(ArrayList<Performance> performances);

    ArrayList<Long> findElements();
}
