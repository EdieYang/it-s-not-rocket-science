import context.boot.Person;
import context.boot.proxy.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

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
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        Person person = (Person) ctx.getBean("person3");
//        System.out.println(person);
//         Map<String,String> map = new HashMap<>(16);
//        System.out.println(map.isEmpty());
        List<String> list = new ArrayList<>(15);
        System.out.println(list.isEmpty());
    }

    @Test
    public void test3() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) ctx.getBean("userService");
        Person person = (Person) ctx.getBean("person3");
        userService.register(person);
        userService.login(person.getName(), "123");

    }

    @Test
    public void test4() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "walt");
        map.put("age", 12);
        map.put("gender", 1);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key=" + entry.getKey() + "&value=" + entry.getValue());
        }

        //使用Map.forEach遍历
        map.forEach((key, value) -> {
            System.out.println("key=" + key + "&value=" + value);
        });

    }
}
