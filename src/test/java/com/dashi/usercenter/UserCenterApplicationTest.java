package com.dashi.usercenter;

import com.dashi.usercenter.mapper.UserMapper;
import com.dashi.usercenter.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class UserCenterApplicationTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
        System.out.println(("selectAll method test"));
        List<User> userslist = userMapper.selectList(null);
        Assertions.assertTrue(userslist.size() == 5, "");
        userslist.forEach(System.out::println);



    }




}
