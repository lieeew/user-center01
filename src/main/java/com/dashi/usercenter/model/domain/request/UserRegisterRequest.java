package com.dashi.usercenter.model.domain.request;

import com.dashi.usercenter.service.UserService;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 *
 * @author Kudas_Gorge
 */

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3973774980087001310L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String workerCode;

}
