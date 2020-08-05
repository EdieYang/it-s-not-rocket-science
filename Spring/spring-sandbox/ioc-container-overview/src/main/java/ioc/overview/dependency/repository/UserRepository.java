package ioc.overview.dependency.repository;

import ioc.overview.dependency.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;

import java.util.Collection;
import java.util.List;

/**
 * 用户信息仓库
 *
 * @author Eddie
 * @since 2020/8/5
 */
public class UserRepository {

    private Collection<User> userList;//自定义对象

    private BeanFactory beanFactory;//内建对象

    private ObjectFactory<User> objectFactory;

    public Collection<User> getUserList() {
        return userList;
    }

    public void setUserList(Collection<User> userList) {
        this.userList = userList;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ObjectFactory<User> getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(ObjectFactory<User> objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public String toString() {
        return "UserRepository{" +
                "userList=" + userList +
                '}';
    }
}
