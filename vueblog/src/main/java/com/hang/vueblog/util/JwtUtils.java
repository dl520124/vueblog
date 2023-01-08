package com.hang.vueblog.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt工具类
 */
@Slf4j
@Data
@Component
/*// 将类定义为一个bean的注解，比如 @Component,@Service,@Controller,@Repository
// 或者 @Configuration*/
@ConfigurationProperties(prefix = "markerhub.jwt")
// 表示使用配置文件中前缀为user1的属性的值初始化该bean定义产生的的bean实例的同名属性
// 在使用时这个定义产生的bean时，其属性name会是Tom
public class JwtUtils {

    private String secret;
    private long expire;
    private String header;


    /**
     * 生成jwt token
     *
     */

    /*传入userid 生成 jwt*/
   public String generateToken(long userId) {
        Date nowDate = new Date();
        //过期时间  300*1000  五分钟
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId+"")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)  //设置过气时间
                .signWith(SignatureAlgorithm.HS512, secret)  //盐加密
                .compact();
    }












    /*解析token*/

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
