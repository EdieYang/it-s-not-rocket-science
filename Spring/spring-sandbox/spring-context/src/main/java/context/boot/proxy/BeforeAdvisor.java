package context.boot.proxy;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/29 18:56
 */
public class BeforeAdvisor implements MethodBeforeAdvice {
    /**
     *
     * @param method 原始目标对象的方法
     * @param args 原始目标对象的方法参数
     * @param target 原始目标对象
     * @throws Throwable
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("---log---");
    }
}
