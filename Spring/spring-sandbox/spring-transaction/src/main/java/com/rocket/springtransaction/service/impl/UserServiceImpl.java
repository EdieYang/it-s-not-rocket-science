package com.rocket.springtransaction.service.impl;

import com.rocket.springtransaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * TODO
 *
 * @author Eddie
 * @Date 2021/3/4
 * @since 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getUserById(String id) {
        return jdbcTemplate.queryForMap("select * from sys_user where id = ? ", id);
    }


    @Override
    public void insertUser(Map<String, Object> user) throws Exception {
//        jdbcTemplate.update("insert into sys_user(id,user_name,password) values(?,?,?)", user.get("id"), user.get("userName"), user.get("password"));
        throw new UnsupportedOperationException();
    }
}
