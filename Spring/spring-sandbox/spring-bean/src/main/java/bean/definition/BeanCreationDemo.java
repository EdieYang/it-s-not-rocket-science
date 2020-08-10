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
        User user = applicationContext.getBean("user-by-static-method", User.class);
        System.out.println("通过静态工厂方法创建实例" + user);

        User userByFactory = applicationContext.getBean("user-by-factory-method", User.class);
        System.out.println("通过实例工厂方法创建实例" + userByFactory);

        User userByFactoryBean = applicationContext.getBean("userFactoryBean", User.class);
        System.out.println("通过FactoryBean接口创建实例" + userByFactoryBean);

        System.out.println(user == userByFactory);

    }

}
