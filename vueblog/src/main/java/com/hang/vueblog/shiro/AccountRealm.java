package com.hang.vueblog.shiro;


import cn.hutool.core.bean.BeanUtil;
import com.hang.vueblog.entity.User;
import com.hang.vueblog.service.UserService;
import com.hang.vueblog.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    /*判断token是否是jwttoken*/
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /*权限验证*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /*登录验证*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        /*将token强转为jwtToken？shiro 中的token和我们定义jwt作为token 是不一样的？所以要强转？*/
        JwtToken jwtToken = (JwtToken) token;
        /*解析当前token 获取到userid*/
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        /*强转为Long类型，因为解析的String类型，要传入long类型*/
        /*查找出当前登录的用户*/
        User user = userService.getById(Long.valueOf(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }

        if (user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定");
        }
        /*封装一个类，将可以公开的信息交给shiro*/
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);


        System.out.println("profile公开了什么："+profile.toString());

        /*private Long id;

    private String username;

    private String avatar;

    private String email;     当作  principal  获取的对象 */
        /*principal   作为SimpleAuthenticationInfo  第一个参数，是身份的标识，既可以传用户名，也可以传实体*/
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }
}












