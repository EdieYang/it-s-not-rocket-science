package context.boot.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/31 11:06
 */
public class Around implements MethodInterceptor {

    /**
     * invocation.proceed() 代表原始方法运行
     *
     * @param invocation
     * @return 原始方法执行后的返回值
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //TODO 添加原始方法之前的额外功能
        Object ret = null;
        try {
            ret = invocation.proceed(); // 执行原始方法
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //TODO 添加原始方法之后的额外功能
        return ret;
    }
}
