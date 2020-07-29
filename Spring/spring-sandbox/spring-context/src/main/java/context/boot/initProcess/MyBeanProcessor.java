package context.boot.initProcess;

import context.boot.Person;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/29 17:00
 */
public class MyBeanProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization->" + beanName);
        if (bean instanceof Person) {
            Person person = ((Person) bean);
            person.setAge(12);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization->" + beanName);
        if (bean instanceof Person) {
            Person person = ((Person) bean);
            person.setAge(14);
        }
        return bean;
    }
}
