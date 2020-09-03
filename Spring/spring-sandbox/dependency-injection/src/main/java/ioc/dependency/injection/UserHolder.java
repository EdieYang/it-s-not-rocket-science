package ioc.dependency.injection;

import ioc.overview.dependency.domain.User;

/**
 *  {@link User}  Holder类对象
 *
 * @author Eddie
 * @since
 */
public class UserHolder {

    private User user ;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserHolder{" +
                "user=" + user +
                '}';
    }
}
