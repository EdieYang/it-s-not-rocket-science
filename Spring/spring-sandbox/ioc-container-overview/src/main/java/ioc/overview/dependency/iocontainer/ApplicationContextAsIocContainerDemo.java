package ioc.overview.dependency.iocontainer;

import ioc.overview.dependency.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * ApplicationContext容器 {注解能力}
 *
 * @author Eddie
 * @since 2020/8/6
 */
@Configuration
public class ApplicationContextAsIocContainerDemo {

    public static void main(String[] args) {
        //创建beanFactory容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ApplicationContextAsIocContainerDemo.class);
        applicationContext.refresh();
        lookupCollectionInType(applicationContext);
    }

    @Bean
    public User user() {
        User user = new User();
        user.setName("Eddie");
        user.setAge(12);
        return user;
    }

    private static void lookupCollectionInType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);
            users.forEach((key, value) -> {
                System.out.println(key + " " + value);
            });
        }
    }
}
