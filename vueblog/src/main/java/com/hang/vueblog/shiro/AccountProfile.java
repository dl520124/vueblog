package com.hang.vueblog.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: project
 * @description:
 * @author: 作者
 * @create: 2021-07-13 17:46
 */
@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String username;

    private String avatar;

    private String email;

}
