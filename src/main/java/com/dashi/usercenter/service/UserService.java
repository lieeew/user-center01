package com.dashi.usercenter.service;

import com.dashi.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;


/**
* @author Kudas_Gorge
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-03-12 17:35:45
*/
public interface UserService extends IService<User> {

    /**
     * 用户登录状态健key
     */
    String USER_LOGIN_STATE = "userLoginState";


    /**
     *用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
long userRegister(String userAccount,String userPassword, String checkPassword);




    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);
}


