package com.hang.vueblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @program: project
 * @description:
 * @author: 作者
 * @create: 2021-07-13 16:51
 */
public class JwtToken implements AuthenticationToken {

    private String token;

/*传入jwt  ，jwt就是token*/
    public JwtToken(String jwt) {
        this.token = jwt;
    }

    /*返回这个token*/
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
