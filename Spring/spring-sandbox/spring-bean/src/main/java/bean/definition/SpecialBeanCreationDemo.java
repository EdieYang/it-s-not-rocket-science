package bean.definition;

import ioc.overview.dependency.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Bean特殊实例化示例
 *
 * @author Edie
 * @since
 **/
public class SpecialBeanCreationDemo {


    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/META-INF/bean-special-creation.xml");


    }

}
