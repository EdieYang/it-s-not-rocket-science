package bean.definition;

import ioc.overview.dependency.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Bean实例化示例
 *
 * @author Edie
 * @since
 **/
public class BeanCreationDemo {


    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/META-INF/bean-creation.xml");
        User user = (User) applicationContext.getBean("user-by-static-method");
        System.out.println(user);
    }

}
