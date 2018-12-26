package com.ptteng.water.system.service;

import com.ptteng.water.system.pojo.Department;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface DepartmentService {
    ArrayList<Department> findAllDepartment(String name, Integer type,
                                            Long parentId, Integer status,
                                            Double budgetFrom, Double budgetTo,
                                            Double gradeFrom, Double gradeTo);
    Department findOneDepartment(Long id);

    Long updateDepartment(Long id, Department department);

    Long saveDepartment(Department department);

    Long deleteDepartment(Long id);

    ArrayList<Department> findPartents();

    void setProperties(Department department);
}
