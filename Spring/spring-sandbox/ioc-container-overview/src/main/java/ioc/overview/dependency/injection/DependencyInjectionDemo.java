package ioc.overview.dependency.injection;

import ioc.overview.dependency.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 依赖查找示例
 * 1.通过名称查找
 * 2.通过类型查找
 *
 * @author Eddie
 * @since 2020/8/5
 */
public class DependencyInjectionDemo {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-injection-context.xml");
        //依赖来源一：自定义bean
        UserRepository userRepository = (UserRepository) applicationContext.getBean("userRepository");
        //（依赖注入）依赖来源二：容器内建依赖
        System.out.println(userRepository.getBeanFactory());
        System.out.println(userRepository.getObjectFactory() == applicationContext.getBean("objectFactory"));
        //依赖来源二：容器内建Bean
        Environment environment = applicationContext.getBean(Environment.class);
        System.out.println(environment);
    }


}
