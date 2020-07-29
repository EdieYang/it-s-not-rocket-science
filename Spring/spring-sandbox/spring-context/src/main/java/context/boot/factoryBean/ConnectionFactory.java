package context.boot.factoryBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/29 14:32
 */
public class ConnectionFactory {

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://rm-uf61fs31zdd787euco.mysql.rds.aliyuncs.com/kangsai?useSSL=false", "kangsaidev", "Kangsai2017");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
}
