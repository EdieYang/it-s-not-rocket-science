import innerclass.Outer;
import interfacedemo.MyInterface1;
import org.junit.Test;

/**
 * @author Eddie
 * @version 1.0
 * @description
 * @date 2020/8/5 11:15
 */
public class Unittest {

    @Test
    public void test() {
        Outer.Inner inner = new Outer().new Inner();
        inner.method();
    }

    @Test
    public void test2(){
        MyInterface1 myInterface1 = new MyInterface1() {
            @Override
            public void method() {
                System.out.println("method");
            }

            @Override
            public void method2() {
                System.out.println("method2");
            }

            @Override
            public void methodDefault(){
                System.out.println("匿名内部类默认方法");
            }
        };

        myInterface1.method();
        myInterface1.method2();
        myInterface1.methodDefault();
    }
}
