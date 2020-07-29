package context.boot.proxy;

import context.boot.Person;
import org.aopalliance.aop.Advice;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/29 18:54
 */
public class UserServiceImpl implements UserService {

    @Override
    public void register(Person person) {
        System.out.println("register" + person);
    }

    @Override
    public void login(String userName, String password) {
        System.out.println("login....success");
    }
}
