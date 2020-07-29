import context.boot.Person;
import context.boot.factoryBean.ConnectionFactoryBean;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/28 18:03
 */
public class ApplicationTest {

    @Test
    public void test() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Person person = (Person) applicationContext.getBean("person");

        //通过id 和 class 获取对象 ， 无需强转
        Person person1 = applicationContext.getBean("person", Person.class);
        //通过类名获取对象，在当前Spring上下文中，只能有一个<bean class>属性是Person类型，否则报错。
        Person person2 = applicationContext.getBean(Person.class);
        System.out.println(person1);
        System.out.println(person2);
        System.out.println(person1 == person2);
        //获取Spring工厂配置文件中所有bean标签的id值
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        String[] typeNames = applicationContext.getBeanNamesForType(Person.class);
        for (String typeName : typeNames) {
            System.out.println(typeName);
        }
        System.out.println(applicationContext.containsBeanDefinition("person"));
    }


    @Test
    public void test2() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        Connection  c1 = (Connection) ctx.getBean("conn");
        Connection c2 = (Connection) ctx.getBean("conn");
        System.out.println(c1==c2);
    }

}
