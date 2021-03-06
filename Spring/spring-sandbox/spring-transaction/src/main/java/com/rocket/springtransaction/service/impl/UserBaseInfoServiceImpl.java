package com.rocket.springtransaction.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author Eddie
 * @date 2021/03/06
 * @since
 */
@Service
@Qualifier("userBaseInfoServiceImpl")
public class UserBaseInfoServiceImpl extends UserServiceImpl{

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void update(){
        jdbcTemplate.update("update sys_user set user_name ='123123kkk' where id  = 1 ");

        throw new UnsupportedOperationException();
    }

}
