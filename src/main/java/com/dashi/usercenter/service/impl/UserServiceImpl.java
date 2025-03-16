package com.dashi.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dashi.usercenter.common.ErrorCode;
import com.dashi.usercenter.exception.BusinessException;
import com.dashi.usercenter.model.domain.User;
import com.dashi.usercenter.service.UserService;
import com.dashi.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Kudas_Gorge
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-03-12 17:35:45
*/

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * 用于混淆密码，保护密码
     */
    private static String SALT = "dashi";

    /**
     * 用户登录状态健key
     */
    private static final String USER_LOGIN_STATE = "userLoginState";


    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @param workerCode
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String workerCode) {
        //1.check
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword, workerCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 ||  checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度过短");
        }
        if (workerCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "员工工号过长");
        }

        //不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        //userPassword==checkPassword
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        //账户不重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户重复");
        }

        //员工工号不重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("workerCode",workerCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "员工工号重复");
        }

       //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setWorkerCode(workerCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
            return user.getId();


    }

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.check
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        //不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //账户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed, userAccount Cannot match userPassword");
            return null;
        }

        //3.用户脱敏
        User safetyUser = getSafetyUser(user);


        //4.记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }


    /**
     * 用户脱敏
     * @param originUser
     * @return
     */

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
    User safetyUser = new User();
    safetyUser.setId(originUser.getId());
    safetyUser.setUsername(originUser.getUsername());
    safetyUser.setUserAccount(originUser.getUserAccount());
    safetyUser.setAvatarUrl(originUser.getAvatarUrl());
    safetyUser.setGender(originUser.getGender());
    safetyUser.setUserPassword(originUser.getUserPassword());
    safetyUser.setPhone(originUser.getPhone());
    safetyUser.setEmail(originUser.getEmail());
    safetyUser.setUserRole(originUser.getUserRole());
    safetyUser.setWorkerCode(originUser.getWorkerCode());
    safetyUser.setUserStatus(originUser.getUserStatus());
    safetyUser.setCreateTime(originUser.getCreateTime());
    safetyUser.setUpdateTime(originUser.getUpdateTime());
    safetyUser.setIsDelete(originUser.getIsDelete());
    return safetyUser;
}

    /**
     * 用户退登录
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录状态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


}




