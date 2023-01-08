package com.hang.vueblog.controller;



import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.vueblog.common.lang.Result;
import com.hang.vueblog.entity.Blog;
import com.hang.vueblog.service.BlogService;
import com.hang.vueblog.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class BlogController {

    @Autowired
    BlogService blogService;
    /*分页*/
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {

        /*分页操作，mybatisplus中有page  第一个参数是当前页，第二个参数是页大小*/
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return Result.succ(pageData);
    }
    /*详情*/
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        /*编辑的先把自己的记录查询出来，再根据内容修改保存*/
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return Result.succ(blog);
    }




    //@PathVariable动态路由
    @RequiresAuthentication  //需要认证之后才能操作
    @PostMapping("/blogdel/{id}")
    public Result del(@PathVariable Long id){
        boolean b = blogService.removeById(id);
        //判断是否为空 为空则断言异常
        if(b==true){

            return Result.succ("文章删除成功");
        }else{
            return Result.fail("文章删除失败");
        }
    }


    /*认证才能编辑*/
    /*编辑,传实体，编辑和添加是同个的还要校验，判断blog是否有id，如果有id说明是编辑状态*/
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

//        Assert.isTrue(false, "公开版不能任意编辑！");

        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            /*编辑的先把自己的记录查询出来，再根据内容修改保存*/
            System.out.println(ShiroUtil.getProfile().getId());
            //只能编辑自己的文章
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {

            /*新增*/

            /*修改部分*/
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
            Date date = new Date();// 获取当前时间

       /*    *//*data强转string*//*
            String  date1 = sdf.format(date);*/

            /*添加的文章，必填 id  创建时间，状态*/
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
         /*   temp.setCreated(LocalDateTime.now());*/

           temp.setCreated(date);

/*            temp.setCreated(date1);*/
            temp.setStatus(0);
        }

        /*修改部分*/
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        System.out.println("现在时间：" + sdf.format(date)); // 输出已经格式化的现在时间（24小时制）
        /*难道提前修改会被复制的清掉？*/
        /*data强转string*/
   /*     String  date1 = sdf.format(date);*/
        temp.setCreated(date);

        System.out.println("temp:~~~~"+temp.toString());

        /*BeanUtil.copyProperties进行对象之间属性的赋值*/

      /*  BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");*/

        BeanUtil.copyProperties(blog, temp, "id", "userId", "status");

        System.out.println("修改后的temp~~~"+temp.toString());

        temp.setCreated(date);

        System.out.println("修改后吼吼的temp~~~"+temp.toString());

        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }


}
