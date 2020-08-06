package abstractpractice;

public class AbstractTest {

    public static void main(String[] args) {
        Student student = new Student();
        student.eat();
    }

}

class Person {
    private String name;
    private Integer age;


    public Person() {
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println("eat something");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}


class Student extends Person {

    @Override
    public void eat() {
        System.out.println("student eat slowly");
    }
}