package com.rocket.springtransaction.mock;

import com.rocket.springtransaction.service.UserService;
import com.rocket.springtransaction.service.impl.UserBaseInfoServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author Eddie
 * @Date 2021/3/4
 * @since 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMock {

//    @Resource
//    private UserService userService;

    @Autowired
    @Qualifier("userBaseInfoServiceImpl")
    private UserBaseInfoServiceImpl userBaseInfoService;

    @Test
    public void test() throws Exception {
//        Map<String,Object> newUser = new HashMap<>();
//        newUser.put("id",10);
//        newUser.put("userName","1347262626");
//        newUser.put("password","1235");
//        userService.insertUser(newUser);
//        System.out.println(userService.getUserById("10"));

        userBaseInfoService.update();
    }


}
