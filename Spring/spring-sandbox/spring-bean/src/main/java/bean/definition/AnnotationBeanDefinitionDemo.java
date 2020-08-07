package bean.definition;

import ioc.overview.dependency.domain.User;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 注解BeanDefinition 示例
 *
 * <p>BeanDefinition注册
 * XML配置元信息 <bean name = "xxx" ../>
 * Java配置元信息
 * 1.@Bean
 * 2.@Import
 * 3.@Component
 * Java API 配置元信息
 * 1.命名方式：BeanDefinitionRegistry#RegisterBeanDefinition(String , BeanDefinition)
 * 2.非命名方式：BeanDefinitionReaderUtils#registerWithGeneatedName(AbstractBeanDefinition,BeanDefinitionRegistry)
 * 3.配置类方式：AnnotatedBeanDefinitionReader#register(Class..)
 *
 * @author Eddie
 * @since 2020/8/7
 */
@Component
public class AnnotationBeanDefinitionDemo {

    public static void main(String[] args) {
        // 1、构建Spring 容器
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        // 2.注册Components,通过AnnotatedBeanDefinitionReader#register(Class..<@Configuration>) 配置类方式进行注册。
        // 通过@Component导入
//        annotationConfigApplicationContext.register( AnnotationBeanDefinitionDemo.class);
        // 通过@Import 导入
        annotationConfigApplicationContext.register(ImportConfig.class);

        // 3.刷新容器，对刚才注册的类进行初始化工作
        annotationConfigApplicationContext.refresh();

        Map<String, User> map = annotationConfigApplicationContext.getBeansOfType(User.class);
        System.out.println(map);

        //关闭容器
        annotationConfigApplicationContext.close();


        //Java API 配置元信息 命名非命名方式注册Bean
        AnnotationConfigApplicationContext annotationConfigApplicationContextSub = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContextSub.refresh();

        //通过BeanDefinitionRegistry 命名方式注册Bean
        registerBeanDefinition(annotationConfigApplicationContextSub, User.class, "eddie");

        //通过BeanDefinitionReaderUtils 非命名方式注册Bean
        registerBeanDefinition(annotationConfigApplicationContextSub, User.class, "");

        User user = (User) annotationConfigApplicationContextSub.getBean("eddie");
        Map<String, User> userMap = annotationConfigApplicationContextSub.getBeansOfType(User.class);

        System.out.println(user);
        System.out.println(userMap);

    }


    public static void registerBeanDefinition(BeanDefinitionRegistry beanDefinitionRegistry, Class<?> beanClass, String beanName) {

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        beanDefinitionBuilder
                .addPropertyValue("name", "Eddie-rahman")
                .addPropertyValue("age", 29);
        if (StringUtils.hasText(beanName)) {
            beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        } else {
            BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinitionBuilder.getBeanDefinition(), beanDefinitionRegistry);
        }

    }


    /**
     * 如果内部类未声明为static，在实例化时首先需要new一个外部类的对象。
     * 静态内部类对象初始化构造器不指向外部类对象，所以初始化是不需要引用外部类对象
     * new Outer.Inner()
     */
    @Component
    public static class Config {
        @Bean
        public User user() {
            User user = new User();
            user.setName("Eddie");
            user.setAge(12);
            return user;
        }
    }

    /**
     * 内部类实例化默认构造器引入外部类的对象，所以外部类需要先用@Component注解实例化后，获得外部类的对象，才能将内部类进行实例化。
     * new Outer().new Inner()
     */
    @Component
    public class Config2 {

        @Bean
        public User user() {
            User user = new User();
            user.setName("Eddie2");
            user.setAge(13);
            return user;
        }

    }

    /**
     * 通过Import导入Bean
     */
    @Import({AnnotationBeanDefinitionDemo.Config.class})
    public static class ImportConfig {

    }

}
