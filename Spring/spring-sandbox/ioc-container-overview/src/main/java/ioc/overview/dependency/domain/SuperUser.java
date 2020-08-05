package ioc.overview.dependency.domain;

import ioc.overview.dependency.annotation.Super;

/**
 * 超级用户
 *
 * @author Eddie
 * @since 2020/8/5
 */
@Super
public class SuperUser extends User {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SuperUser{" +
                "address='" + address + '\'' +
                "} " + super.toString();
    }
}
