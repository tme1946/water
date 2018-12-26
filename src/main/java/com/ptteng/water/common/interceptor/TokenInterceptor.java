
package com.ptteng.water.common.interceptor;



import com.ptteng.water.system.pojo.Manager;
import com.ptteng.water.system.service.ManagerService;
import com.ptteng.water.system.service.ModuleService;
import com.ptteng.water.system.service.RoleModuleService;
import com.ptteng.water.util.CookieUtil;
import com.ptteng.water.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * TODO 拦截器实现token拦截
 * by   2018/9/29 15:46
 */

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    ManagerService managerService;

    @Autowired
    RoleModuleService roleModuleService;

    @Autowired
    ModuleService moduleService;


    /**
     * TODO 拦截器实现权限管理，目前未完成
     * at 2018/9/30 20:38
     **/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        log.info("hello token interceptor==========================");
        Long uid = CookieUtil.getUid(request);
        if (uid == null) {
            response.sendRedirect("/nologin");
            return false;
        }
        log.info("login uid is======" + uid);
        //进行判断，如果该uid对应的用户不为null，return true
        Manager manager = managerService.selectByPrimaryKey(uid);
        if (manager != null) {
            String requestUrl = this.getInterceptorUrl(request);
            log.info(" requstUrl  =======" + requestUrl);

            //todo 权限管理这里有问题，不做权限拦截了
            //查询该用户拥有的所有模块（权限）
//            List<Long> mids = roleModuleService.getMidsByRid(manager.getRoleId());
//            log.info("module ids ======" + mids);
//            if (CollectionUtils.isEmpty(mids)) {
//                response.sendRedirect("/noPermissin");
//                return false;
//            }
//            log.info("user has module ids========" + mids);
//            List<Module> modules = moduleService.getObjectsByIds(mids);
//            log.info("user has modules ========" + modules);
//            if (CollectionUtils.isEmpty(modules)) {
//                response.sendRedirect("/noPermissin");
//                return false;
//            }
//
//            boolean flag = false;
//            String[] requestUrls = requestUrl.split("\\?");
//            log.info(" reqeust is ========" + requestUrls[0]);
//            for (Module module : modules) {
//                log.info(" module url is  =======" + module.getUrl());
//                log.info("the flag is =======" + flag);
//                if (DataUtil.isNotNullOrEmpty(module.getUrl())) {
//                    //获取module的url，用“.”来分割url，获取第二部分所代表的模块信息
//                    //然后判断这个模块信息，是否包含在请求的url里，如果包含，则说明请求的url有权限
//                    String[] moduleUrls = module.getUrl().split("\\.");
//                    log.info(" managerUrl  is  ========" + moduleUrls[1]);
//                    if (moduleUrls.length > 1) {
//                        if (requestUrls[0].contains(moduleUrls[1])) {
//                            log.info(uid + " is have module ======" + moduleUrls[1]);
//                            flag = true;
//                            break;
//                        }
//                    }
//                } else {
//                    flag = true;
//                }
//            }
//            log.info("the final flag is " + flag);
//            if (!flag) {
//                log.info(uid + " is don't have module " + requestUrl);
//                response.sendRedirect("/noPermissin");
//                return false;
//            }
            return true;
        }
        return  false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public String getInterceptorUrl(HttpServletRequest request) {
        String interceptorUrl = request.getRequestURI()
                + (null == request.getQueryString() ? "" : "?"
                + request.getQueryString()).toString();
        interceptorUrl = interceptorUrl.replace("/app/", "/");
        log.info("get getInterceptorUrl is " + interceptorUrl);
        return interceptorUrl;
    }
}

