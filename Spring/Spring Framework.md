# Spring Framework



## 特性总览

#### 核心

- IoC容器
- Spring事件
- 资源管理器
- 国际化
- 校验
- 数据绑定
- 类型转换
- Spring表达式
- 面向切面编程



#### 数据存储

- JDBC
- 事物抽象
- DAO支持
- O/R映射
- XML编列



#### Web Servlet技术栈

- Spring MVC
- WebSocket
- SockJs



#### Web Reactive 技术栈

- Spring WebFlux
- WebClient
- WebSocket





#### 技术整合

- 远程调用（Remoting）
- Java消息服务（JMS）
- ~~Java连接架构（JCA）~~
- Java管理扩展（JMX）
- Java邮件客户端（Email）
- 本地任务（Tasks）
- 本地调度（Scheduling）
- 缓存抽象（Caching）
- Spring测试
  - 模拟对象（mock Objects）
  - TestContext框架
  - Spring MVC测试
  - Web测试客户端  





## Spring 模块化设计











## Spring核心API

- ApplicationContext

  ```mark
  1.作用：Spring提供的ApplicationContext这个工厂，用于对象的创建
  2.好处：解耦
  ```

  - ApplicationContext接口类型

  ``` mark
  1.接口：屏蔽实现差异
  2.非web环境：ClassPathXmlApplicationContext 
  3.web环境：XMLWebApplicationContext
  ```

  - 重量级资源

  ``` mark
  1.ApplicationContext 工厂的对象占用大量的内存
  2.不会频繁地创建对象：一个应用只会创建一个工厂对象
  3.ApplicationContext工厂：一定是线程安全的（多线程并发访问）
  ```

  code.e.g

  ``` java
  //1.获取spring工厂对象
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
  //2.通过工厂获取对象
  Person person = (Person) applicationContext.getBean("person");
  //3.通过id 和 class 获取对象 ， 无需强转
  Person person1 = applicationContext.getBean("person", Person.class);
  //4.通过类名获取对象，在当前Spring上下文中，只能有一个<bean class>属性是Person类型，否则报错。
  Person person2 = applicationContext.getBean(Person.class);
  //5.获取Spring工厂配置文件中所有bean标签的id值
  String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
  //6.根据类型获取Spring配置文件中对应bean标签的id值
  String[] typeNames=  applicationContext.getBeanNamesForType(Person.class);
  //7.根据id值判断是否存在配置文件中
  applicationContext.containsBeanDefinition("person");
  //8.根据id或name属性值判断是否存在配置文件中
  applicationContext.containsBean("person");
  ```

  

  配置文件注意细节：

  ``` mark
  1.只配置class属性
  <bean class="xxxx" />
  a)上述这种配置 没有id值 Spring 默认赋予全限定类目#加数字
  b)应用场景：
  	如果这个bean只需要使用一次，那么就可以省略id值
      如果这个bean会使用多次，或者被其他bean引用，则需设置id值
      
  2.name属性
  和id的相同点都可以通过getBean获取对象
  为Spring配置文件，配置name属性别名
  a)别名可以定义多个，逗号分隔，id只能是唯一
  b)XML的id属性值，命名是有要求的：必须以字母开头，不能以特殊字符开头，发展到现在，id属性的限制不存在，可以以特殊字符开头。
  3.代码
  //7.根据id值判断是否存在配置文件中
  applicationContext.containsBeanDefinition("person");
  //8.根据id或name属性值判断是否存在配置文件中
  applicationContext.containsBean("person");
  ```

  

  

  

## Spring注入

1.什么是注入

通过Spring工厂及配置文件，为所创建对象的成员变量赋值

2.为什么需要注入

解耦合

3.注入原理

1)set注入

2)构造器注入



### Set注入详解

``` xml
1.JDK内置类型
1.1 String + 8种基本类型
<value>xxx</value>
1.2 String[] , List 集合
<list>
	<value></value>
	<value></value>
	<value></value>
</list>

1.3 Set集合
<set>
	<value>xxx</value>
	<value>xxx</value>
	<value>xxx</value>
</set>

1.4 Map集合(map--entry--key value根据类型选对应的标签)
<map>
	<entry>
		<key><value>xxx</value></key>
		<value>xxx</value>
	</entry>
	<entry>
		<key><value>xxx</value></key>
		<value>xxx</value>
	</entry>
</map>

1.5 Properties
<props>
	<prop key = "xxx">xxxx</prop>
	<prop key = "xxx">xxxx</prop>
</props>

2.自定义类型
2.1 第一种方式
<bean id = "userService" class="xxx">
 	<property name = "userDao" >
    	<bean class="userDao"></bean>
    </property>
</bean>
<bean id = "orderService" class="xxx">
 	<property name = "userDao" >
    	<bean class="userDao"></bean>
    </property>
</bean>
<bean id = "addressService" class="xxx">
 	<property name = "userDao" >
    	<bean class="userDao"></bean>
    </property>
</bean>
2.2 第二种方式
第一种方式缺点：
1.被注入的对象userDao多次创建，浪费内存资源
2.多次注入对象userDao，代码冗余

<bean id = “userDao” class="xxx"></bean>

<bean id = "userService" class="xxx">
 	<property name = "userDao">
    	 <ref bean="userDao"></ref>
    </property>
</bean>
<bean id = "orderService" class="xxx">
 	<property name = "userDao">
    	 <ref bean="userDao"></ref>
    </property>
</bean>
<bean id = "addressService" class="xxx">
 	<property name = "userDao">
    	 <ref bean="userDao"></ref>
    </property>
</bean>

p命名空间简化

<property name = "userDao">
    <ref bean="userDao"></ref>
</property>

<property name = "userDao" ref=“userDao” />
<property name = "userDao" value=“userDao” />

<bean id="xxx" class="xxx" p:name = "xxx" p:id = "xxx"></bean>
<bean id="xxx" class="xxx" p:name = "xxx" p:id = "xxx" p:userDao-ref="userDao"></bean>
```



### 构造注入详解

 开发步骤

1.提供有参构造方法

2.Spring配置文件

``` xml
<constructor-arg></constructor-arg> 个数与构造参数个数相同
```



构造方法重载 ,构造参数个数相同类型不同，需指定类型

``` xml
<constructor-arg type=""></constructor-arg>
```



## 反转控制和依赖注入

### 1.反转控制IOC

控制：对于成员变量赋值的控制权

反转控制：把对于成员变量赋值的控制权，从代码中反转到Spring工厂和配置文件中完成

​	好处：解耦合

底层实现：工厂设计模式

### 2.依赖注入DI

注入：通过spring的工厂和配置文件，为对象的成员变量赋值

依赖注入：当一个类需要依赖另一个类时，将依赖的类作为成员变量，最终通过spring配置文件进行注入

​	好处：解耦



### 3.Spring工厂创建复杂对象

1.FactoryBean接口

- 实现FactoryBean接口

  ``` java
  public class ConnectionFactoryBean implements FactoryBean<Connection> {
  
  
      //创建复杂对象的代码，MySQL高版本时需要SSL证书认证，解决warning，url加上?useSSL=false
      public Connection getObject() throws Exception {
          Class.forName("com.mysql.jdbc.Driver");
          Connection conn = DriverManager.getConnection("jdbc:mysql://rm-uf61fs31zdd787euco.mysql.rds.aliyuncs.com/kangsai?useSSL=false", "kangsaidev", "Kangsai2017");
          return conn;
      }
  
      //获取复杂对象的类型
      public Class<?> getObjectType() {
          return Connection.class;
      }
  	//是否为单例，返回true，容器只创建一次对象，返回false，容器每次都创建一个新的对象
      public boolean isSingleton() {
          return false;
      }
  }
  ```

  

- 配置Spring配置文件，定义实现FactoryBean接口的Bean

  ``` xml
  #如果Class中指定的是实现FactoryBean接口的Bean，那么通过id值获取到的是这个类所创建的复杂对象 Connection
  #注意不是ConnectionFactoryBean 而是ConnectionFactoryBean的getObject方法返回的对象
  <bean id="conn" class="context.boot.factoryBean.ConnectionFactoryBean" />
  #如果想获取ConnectionFactoryBean对象,需在getBean的val加上前缀&
  ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
  Connection connection = (Connection) ctx.getBean("&conn");
  ```

  

FactoryBean的工作原理

1.通过conn获取ConnectionFactoryBean的对象，进而instanceOf判断出是否为FactoryBean接口的实现类

2.Spring按照规定，调用getObject() --->Connection

3.返回Connection



2.实例工厂

- 创建实例工厂

  ``` java
  public class ConnectionFactory {
  	//创建实例的方法
      public Connection getConnection() {
          Connection conn = null;
          try {
              Class.forName("com.mysql.jdbc.Driver");
              conn = DriverManager.getConnection("jdbc:mysql://rm-uf61fs31zdd787euco.mysql.rds.aliyuncs.com/kangsai?useSSL=false", "kangsaidev", "Kangsai2017");
          } catch (ClassNotFoundException e) {
              e.printStackTrace();
          } catch (SQLException throwables) {
              throwables.printStackTrace();
          }
          return conn;
      }
  }
  ```

  

- 配置xml

  ``` xml
  #实例工厂用于创建实例
  <bean id = "connFactory" class = "xxx.xxx.xxx.ConnectionFactory"></bean>
  #实例工厂生产的对象
  <bean id = "conn" factory-bean="connFactory" factory-method = "getConnection" />
  ```

  

3.静态工厂

- 创建静态工厂

- 配置xml

  ``` xml
  #直接配置factory-method标签
  <bean id = "conn" class = "xxx.xxx.xxx.StaticConnectionFactory" factory-method = "getConnection" />
  ```

  

Spring工厂创建对象的总结：

Spring 

简单对象 配置bean

复杂对象 FactoryBean、实例工厂、静态工厂





### 4.控制Spring工厂创建对象的次数

#### 1.如何控制简单对象的创建次数

``` xml
#单例，创建一次，默认单例
<bean scope=“singleton” id="xxx" class ="xxx"/> 
#每次都会创建一个新的对象
<bean scope=“prototype” id="xxx" class ="xxx"/> 
```

#### 2.如何控制复杂对象的创建次数

FactoryBean接口的isSingleton方法返回true和false

实例工厂和静态工厂可在xml配置scope

#### 3.为什么要控制对象的创建次数

在一些场景下控制创建次数，可以节省不必要的内存浪费，如果一个对象可以被大家共用，创建一次节约内存

什么样的对象只创建一次？

```xml
SqlSessionFactory
DAO
Service
```

什么对象每次都要创建？

```xml
Connection
SqlSession | Session
```





## 对象的生命周期



- 创建阶段

  ``` xml
  scope="singleton"
  Spring工厂创建的同时，对象也创建
  如果想配置Spring工厂会在获取对象的时候，创建对象，添加lazy-init= "true" 标签
  scope="propotype"
  Spring工厂会在获取对象的时候，创建对象
  ```

- 初始化阶段

  ```xml
  Spring工厂在创建完对象之后，调用对象的初始化方法，完成对应的初始化操作
  
  1.初始化方法提供：程序员根据需求，提供初始化方法，最终完成初始化操作
  2.初始化方法调用：Spring工厂进行调用
  ```

  - initializingBean接口

    ``` xml
    afterPropertiesSet()
    ```

  - 对象中提供一个普通的方法用来提供初始化

    ```xml
    然后在xml中配置 <bean init-method="xxxx" ></bean>
    ```

    细节
    1.如果一个对象即实现initializingBean接口又配置了init-method的普通初始化方法

    先执行initializtingBean ， 再 执行init-method 配置的方法

    2.注入发生在初始化操作的前面

    3.什么叫做初始化操作？

    资源的初始化：数据库 io 网络...

    

- 销毁阶段

  ```xml
  Spring销毁对象前，会调用对象的销毁方法，完成销毁操作
  
  1.Spring什么时候销毁创建的对象？
  ctx.close();
  2.销毁方法：程序员自己定义销毁方法，Spring工厂调用销毁方法
  ```

  - DisposableBean接口（销毁时先执行）

    ```xml
    destroy()
    ```

  - 定义普通的销毁方法（销毁时后执行）

    ```xml
    然后在xml中配置 <bean destroy-method="xxxx" ></bean>
    ```

    细节

    1.销毁方法的操作只适用于scope=“singleton”的对象

    2.什么叫做销毁操作？

    资源的释放操作

  

