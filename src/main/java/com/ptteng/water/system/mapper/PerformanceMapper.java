package com.ptteng.water.system.mapper;

import com.ptteng.water.system.pojo.Department;
import com.ptteng.water.system.pojo.Performance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface PerformanceMapper {

    ArrayList<Performance> findPerformances(@Param("name") String name, @Param("year") Integer year,
                                            @Param("status") Integer status, @Param("list") ArrayList<Department> departments);

    Performance findOnePerformance(Long id);

    ArrayList<Performance> findDepartmentPerformance(Long departmentId);

    void addPerformance(Performance performance);

    Long updatePerformance(Performance performance);

    Long deletePerformance(Long id);

    ArrayList<Integer> findYears();

    ArrayList<Performance> findGradeByDepartmentId(Long departmentId);

    ArrayList<Performance> findRank(@Param("list") ArrayList<Department> department, @Param("year") Integer year,
                                    @Param("from") Integer from, @Param("to") Integer to);

    ArrayList<Long> findTemplateId();

    Long deleteByDepartmentId(Long departmentId);
}