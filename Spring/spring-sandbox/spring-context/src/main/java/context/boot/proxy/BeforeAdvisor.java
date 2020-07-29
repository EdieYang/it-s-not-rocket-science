package context.boot.proxy;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/29 18:56
 */
public class BeforeAdvisor implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("---log---");
    }
}
