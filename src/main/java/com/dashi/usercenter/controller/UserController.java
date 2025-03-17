package com.dashi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.dashi.usercenter.common.BaseResponse;
//import com.dashi.usercenter.common.ErrorCode;
//import com.dashi.usercenter.common.ResultUtils;
//import com.dashi.usercenter.exception.BusinessException;
import com.dashi.usercenter.model.domain.User;
import com.dashi.usercenter.model.domain.request.UserLoginRequest;
import com.dashi.usercenter.model.domain.request.UserRegisterRequest;
import com.dashi.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dashi.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.dashi.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author Kudas_Gorge
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
//  public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
//          throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String workerCode = userRegisterRequest.getWorkerCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, workerCode)) {
            return null;
//           throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.userRegister(userAccount, userPassword, checkPassword, workerCode);
//        long result = userService.userRegister(userAccount, userPassword, checkPassword, workerCode);
//        return ResultUtils.success(result);

    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
//    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return userService.userLogin(userAccount,userPassword,request);
//        User user = userService.userLogin(userAccount, userPassword, request);
//        return ResultUtils.success(user);

    }

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
      public Integer userLogout(HttpServletRequest request) {
//    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return  userService.userLogout(request);
//        int result = userService.userLogout(request);
//        return ResultUtils.success(result);
    }


    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
//    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
//            throw new BusinessException(ErrorCode.NO_LOGIN);
        }

        long userId = currentUser.getId();
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
//        User safetyUser = userService.getSafetyUser(user);
//        return ResultUtils.success(safetyUser);
    }

    /**
     * 用户查询
     * @param username
     * @param request
     * @return
     */

    @GetMapping("/search")
    public List<User> searchUsers(String username,  HttpServletRequest request) {
//    public BaseResponse<List<User>> searchUsers(String username,  HttpServletRequest request) {
        //管理员才可查询
        if (!isAdmin(request)) {
            return new ArrayList<>();
//          throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.eq("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
//        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
//        return ResultUtils.success(list);
    }


    @PostMapping("/delete")
    public Boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
//    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        //管理员才可查询
        if (! isAdmin(request)) {
            return false;
//            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            return false;
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return userService.removeById(id);
//        boolean b = userService.removeById(id);
//        return ResultUtils.success(b);

    }

    /**
     * 是否管理员？
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
