package bean.factory;

import ioc.overview.dependency.domain.User;
import org.springframework.beans.factory.FactoryBean;

/**
 * 通过FactoryBean创建实例
 *
 * @author Eddie
 * @since
 */
public class UserFactoryBean implements FactoryBean<User> {


    @Override
    public User getObject() throws Exception {
        return User.createUser();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
