package com.kermi.common.requestBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegiestBody {
    /*用户名*/
    private String username;
    /*邮箱*/
    private String email;
    /*密码*/
    private String pwd;
}