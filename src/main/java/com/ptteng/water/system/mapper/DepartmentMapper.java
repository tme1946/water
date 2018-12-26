package com.ptteng.water.system.mapper;

import com.ptteng.water.system.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface DepartmentMapper {

    /**
     * 多条件查新
     * @param name 部门名称
     * @param type 部门类别
     * @param parentId 父部门
     * @param status 状态
     * @param budgetLow 预算起
     * @param budgetHigh 预算止
     * @param gradeLow 绩效起
     * @param gradeHigh 绩效止
     * @return 部门列表
     */
    ArrayList<Department> listDepartmentByQuery(@Param("name") String name,
                                                @Param("type") Integer type,
                                                @Param("parentId") Long parentId,
                                                @Param("status") Integer status,
                                                @Param("budgetLow") Double budgetLow,
                                                @Param("budgetHigh") Double budgetHigh,
                                                @Param("gradeLow") Double gradeLow,
                                                @Param("gradeHigh") Double gradeHigh);

    Department findById(Long id);

    Long updateDepartment(Department department);

    void addDepartment(Department department);

    Long deleteDepartment(Long id);

    ArrayList<Department> findParents();

    ArrayList<Department> findByTemplateId(Long id);

    Department findByName(String name);

    ArrayList<Long> findTemplateId();

    void deleteByParent(Long parentId);
}