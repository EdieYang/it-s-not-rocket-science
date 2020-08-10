package bean.factory;

import ioc.overview.dependency.domain.User;

/**
 * 工厂创建实例
 *
 * @author Eddie
 * @since
 */
public class UserFactory {

    public User createUser() {
        return User.createUser();
    }
}
