package innerclass;

/**
 * 局部内部类示例
 *
 * @author Edie
 * @since 2020/08/06
 **/
public class Inner {

    public void innerMethod() {
        int num = 10;
        //error 方法常量需要事实不变，final
//        num =20;
        //局部内部类
        class superInner {
            public void method() {
                System.out.println(num);
            }
        }
    }
}
