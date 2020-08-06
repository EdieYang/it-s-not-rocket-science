package interfacedemo;

/**
 * @author Eddie
 * @version 1.0
 * @description
 * @date 2020/8/5 11:19
 */
public interface MyInterface1 {

    void method();

    void method2();

    default void methodDefault(){
        System.out.println("default method!");
    }


}
