package bean.definition;

import ioc.overview.dependency.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Bean 别名示例
 *
 * @author Eddie
 * @since 2020/8/7
 */
public class BeanAliasDemo {


    public static void main(String[] args) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-definition.xml");
        //通过别名 获取bean
        User eddieUser = beanFactory.getBean("eddie-user", User.class);
        User user = beanFactory.getBean("user", User.class);
        System.out.println(eddieUser == user);

    }

}
