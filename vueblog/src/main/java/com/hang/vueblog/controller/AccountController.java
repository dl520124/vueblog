package com.hang.vueblog.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hang.vueblog.common.lang.Result;
import com.hang.vueblog.common.lang.dto.LoginDto;
import com.hang.vueblog.entity.User;
import com.hang.vueblog.service.UserService;
import com.hang.vueblog.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: project
 * @description:
 * @author: 作者
 * @create: 2021-07-14 10:10
 *
 */
/*为什么要添加@RestController？和普通的Controller有何区别*/
@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;


    /*登录成功颁发token*/
    @PostMapping("/login")
    /*c传入的是用户名和密码，这些都在body里面，最好转成dto*/
    public Result lgin(@Validated  @RequestBody LoginDto loginDto, HttpServletResponse response){
        /*根据用户名去匹配密码，为什么用getOne(new QueryWrapper<User>()？*/
        /*是mybatisplus 中的条件查询QueryWrapper，username对应数据库字段  */
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
         //1.判断用户是否为空, Assert.notNull判断传进来的参数值是否不为空值，如果为空就抛出异常throw new IllegalArgumentException(msg)
        Assert.notNull(user,"用户不存在");

        //2.判断密码是否错误，用md5加密
         if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
              return Result.fail("密码不正确");
         }
        //如果密码正确生成jwt,工具类写的是userid生成jwt、
        String jwt = jwtUtils.generateToken(user.getId());

        /*将用户名字 、 当前时间、刷新时间   存入redis中  */




       //将jwt返回给前端，具体返回数据还是hander看项目，如果要做jwt延期最好放在hander里面

         response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");





/*上传vueblog到github*/


        /*返回succ也可以直接返回map用户信息*/
        return Result.succ(MapUtil.builder()
                        .put("id", user.getId())
                        .put("username", user.getUsername())
                        .put("avatar", user.getAvatar())
                        .put("email", user.getEmail())
                        .map());
    }

     // 退出
    @GetMapping("/logout")
    /*有权限进去才有退出按钮*/
    /*更改了点东西*/
    @RequiresAuthentication
    public Result logout() {
        System.out.println("进入logout方法");
        /*交给shiro退出*/
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }





}
