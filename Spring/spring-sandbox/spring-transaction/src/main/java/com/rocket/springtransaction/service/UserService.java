package com.rocket.springtransaction.service;

import java.util.Map;

/**
 * TODO
 *
 * @author Eddie
 * @Date 2021/3/4
 * @since 1.0
 */
public interface UserService {

    Map<String, Object> getUserById(String id);

    void insertUser(Map<String, Object> user) throws Exception;
}
