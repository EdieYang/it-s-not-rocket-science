package interfacedemo;

/**
 * @author Eddie
 * @version 1.0
 * @description
 * @date 2020/8/5 10:37
 */
public class Outer {
    //外部类属性
    private int num = 1;

    public class Inner {
        private int num = 2;
        public void method() {
            System.out.println("num" + num);
            System.out.println("Num2:" + Outer.this.num); // 获取外部类的变量
        }
    }

}
