package com.ptteng.water.system.service;



import com.ptteng.water.system.pojo.Manager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO
 * by   2018/9/30 12:49
 */
public interface ManagerService {

    Manager selectByPrimaryKey(Long id);

    List<Manager> selectByName(String name);

    Long insertSelective(Manager record);

    //根据角色id和用户名动态查询后台用户信息
    List<Manager> selectByRoleAndStatus(Long roleID, String status);

    boolean deleteByPrimaryKey(Long id);

    Long updateByPrimaryKeySelective(Long id, Manager record);

    List<Manager> findListbyIds(List<Long> ids);
}
