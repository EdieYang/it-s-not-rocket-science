package com.rocket.springtransaction.mock;

import com.rocket.springtransaction.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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

    @Resource
    private UserService userService;

    @Test
    public void test() {
        System.out.println(userService.getUserById("0000011f6821451383e63d9c80ad5610"));
    }


}
