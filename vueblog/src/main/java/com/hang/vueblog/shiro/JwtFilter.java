package com.hang.vueblog.shiro;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hang.vueblog.common.lang.Result;
import com.hang.vueblog.util.JwtUtils;
import com.hang.vueblog.util.ShiroUtil;
import freemarker.template.utility.StringUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends AuthenticatingFilter {
    @Autowired
    JwtUtils jwtUtils;



    /**
     * 判断是否允许通过
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */

    /*jwt跨域登录controller之前的操作*/
  /*  @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        System.out.println("isAccessAllowed方法");
        try{
            return executeLogin(request,response);
            *//*
            * 成功则进入登录方法*//*

        }catch (Exception e){
            System.out.println("错误"+e);
//            throw new ShiroException(e.getMessage());
          *//*  responseError(response,"shiro fail");*//*
            return false;
        }
    }*/














    /*createToken：实现登录，我们需要生成我们自定义支持的JwtToken*/
    /*没有jwt情况下，创建JWT*/
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        /*在头文件获取到jwt*/
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");

        /*如果没有JWT  跳过验证*/
        if(StringUtils.isEmpty(jwt)) {
            return null;
        }
        /*封装成token，之后就realm的事情*/
        /*
         * getPrincipal()， getCredentials()  都会返回这个jwt即使token
         * */

        return new JwtToken(jwt);
    }



    /*onAccessDenied：拦截校验，当头部没有Authorization时候，我们直接通过，不需要自动登录；
    当带有的时候，首先我们校验jwt的有效性，没问题我们就直接执行executeLogin方法实现自动登录*/
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        /*在头文件获取到jwt*/
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        /*当没有jwt的时候直接到shiro权限验证*/
        if(StringUtils.isEmpty(jwt)) {
            return true;
        } else {

            //检验jwt，用jwt校验工具类
            // 校验jwt
            Claims claim = jwtUtils.getClaimByToken(jwt);

            System.out.println("~~~~~~~claim:"+claim);
            System.out.println("~~~~jwtUtils.isTokenExpired(claim.getExpiration:"+jwtUtils.isTokenExpired(claim.getExpiration()));




            if(claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                System.out.println("~~~过气拉");
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }

            System.out.println("servletRequest:"+servletRequest);
            System.out.println("servletResponse:"+servletResponse);
            // 执行登录
            return executeLogin(servletRequest, servletResponse);
        }
    }

    /*onLoginFailure：登录异常时候进入的方法，我们直接把异常信息封装然后抛出*/
            /*重写出现登录已禁用户的异常方法，使他返回json形式给前端*/


    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //处理登录失败的异常
        /*如果是空，返回e，如果不是返回错误原因*/
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }


    /**
     * 对跨域提供支持
     */
    /*preHandle：拦截器的前置拦截，因为我们是前后端分析项目，项目中除了需要跨域全局配置之外，
    我们再拦截器中也需要提供跨域支持。这样，拦截器才不会在进入Controller之前就被限制了。*/

/*
     不太需要
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求,这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
*/

}
