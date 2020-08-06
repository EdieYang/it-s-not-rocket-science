package bean.definition;


import ioc.overview.dependency.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * BeanDefinition构建示例
 *
 * @author Edie
 * @since 2020/08/06
 **/
public class BeanDefinitionCreationDemo {

    public static void main(String[] args) {
        // 1.通过BeanDefinitionBuilder 构建beanDefinition
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        // 通过属性设置
        beanDefinitionBuilder
                .addPropertyValue("name", "eddie")
                .addPropertyValue("age", 12);
        // 获取BeanDefinition实例
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        // BeanDefinition 并非Bean的最终态，可以自己定义修改


        // 2. 通过AbstractBeanDefinition 派生
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(User.class);
        // 通过属性设置
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        mutablePropertyValues
                .add("name", "eddie")
                .add("age", 12);
        genericBeanDefinition.setPropertyValues(mutablePropertyValues);
    }
}
