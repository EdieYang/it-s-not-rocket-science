package com.rocket.springtransaction.service.impl;

import com.rocket.springtransaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * TODO
 *
 * @author Eddie
 * @Date 2021/3/4
 * @since 1.0
 */
@Service

public class UserServiceImpl  implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getUserById(String id) {
        return jdbcTemplate.queryForMap("select * from sys_user where id = ? ",id);
    }
}
