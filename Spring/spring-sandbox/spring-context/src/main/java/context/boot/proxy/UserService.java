package context.boot.proxy;

import context.boot.Person;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/29 18:54
 */
public interface UserService {

    void register(Person person);

    void login(String userName, String password);

}
