package com.dashi.usercenter.model.domain.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 *
 * @author Kudas_Gorge
 */

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 4550869734793840185L;

    private String userAccount;

    private String userPassword;
}
