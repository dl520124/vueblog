package com.hang.vueblog.common.lang.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: project
 * @description:
 * @author: 作者
 * @create: 2021-07-14 10:14
 */
@Data
public class LoginDto implements Serializable {

    /*只需要用户名和密码，校验还是要的*/
    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
