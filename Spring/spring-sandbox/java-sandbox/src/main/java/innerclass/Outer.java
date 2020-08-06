package innerclass;

/**
 * 内部类示例
 *
 * @author Edie
 * @date 2020/08/06
 **/
public class Outer {
    //外部类属性
    private int num = 1;

    public class Inner {
        private int num = 2;

        public void method() {
            System.out.println("num:" + num);
            // 获取外部类的变量规则
            // Outer.this.field
            System.out.println("OuterNum:" + Outer.this.num);
        }
    }

    public static void main(String[] args) {
        //外部类初始化内部类实例
        Outer.Inner inner = new Outer().new Inner();
        //通过内部类实例执行内部类中定义的方法
        inner.method();
    }
}
