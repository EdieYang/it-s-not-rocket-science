package context.boot.factoryBean;

import org.springframework.beans.factory.FactoryBean;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/29 13:26
 */
public class ConnectionFactoryBean implements FactoryBean<Connection> {


    //创建复杂对象的代码
    public Connection getObject() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://rm-uf61fs31zdd787euco.mysql.rds.aliyuncs.com/kangsai?useSSL=false", "kangsaidev", "Kangsai2017");
        return conn;
    }

    //获取复杂对象的类型
    public Class<?> getObjectType() {
        return Connection.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
