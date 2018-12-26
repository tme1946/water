package com.ptteng.water.system.mapper;


import com.ptteng.water.system.pojo.Manager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ManagerMapper {
    boolean deleteByPrimaryKey(Long id);

    int insert(Manager record);

    int insertSelective(Manager record);

    Manager selectByPrimaryKey(Long id);

    List<Manager> selectByName(String name);

    int updateByPrimaryKeySelective(Manager record);


    List<Manager> selectByRoleAndStatus(@Param("roleID") Long roleID,
                                        @Param("status") String status);

    List<Manager> findListbyIds(List<Long> list);
}