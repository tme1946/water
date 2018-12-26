package com.ptteng.water.system.controller;

import com.ptteng.water.system.pojo.Manager;
import com.ptteng.water.system.pojo.Response;
import com.ptteng.water.system.pojo.Role;
import com.ptteng.water.system.service.ManagerService;
import com.ptteng.water.system.service.RoleService;
import com.ptteng.water.util.CookieUtil;
import com.ptteng.water.util.PasswordUtils;
import com.ptteng.water.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * at 2018/10/1 19:17
 */
@RestController
@Slf4j
public class SystemController {

    @Resource
    RoleService roleService;

    @Resource
    ManagerService managerService;

    /**
     *  后台管理员登录 用户id存入cookie
     *
     * @return by   2018/9/30 13:00
     **/
    @PostMapping("/a/login")
    public Response login(HttpServletResponse response,
                          @RequestBody Manager query) {
        log.info("login========" + query);
        String name = query.getName();
        String pwd = query.getPwd();
        List<Manager> managers = managerService.selectByName(name);
        System.out.println("managers:"+managers);
        if (CollectionUtils.isEmpty(managers)) {
            return new Response<>(-1, "用户名不存在", name);
        }
        // 用户密码比对
        if (PasswordUtils.authenticatePassword(managers.get(0).getPwd(), pwd)) {
            log.info("用户已登录 id = {}", managers.get(0).getId());
            //将用户uid存入cookie
            String uid = TokenUtil.createToken(managers.get(0).getId());
            Cookie cookie = new Cookie(CookieUtil.UID, uid);
            cookie.setPath("/");
            //过期时间一个月
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);
            //获取用户角色
            Role role = roleService.selectById(managers.get(0).getRoleId());
            Map<String, Object> map = new HashMap<>();
            map.put("manager", managers.get(0));
            map.put("role", role);
            return new Response<>(0, "success", map);
        }
        return new Response<>(-1, "密码错误", name);
    }

    @PostMapping("/a/u/logout")
    public Response loginOut(HttpServletRequest request, HttpServletResponse response) {
        Long uid = CookieUtil.getUid(request);
        log.info("logout uid======" + uid);

        CookieUtil.loginout(request, response);
        return new Response<>(0, "success", null);
    }

}
