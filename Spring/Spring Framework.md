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

  

##  配置文件参数化

把Spring配置文件中需要经常修改的字符串信息，转移到一个更小的配置文件中，利于Spring配置文件的维护

```xml
1.提供一个properties文件，名称随意，放置位置随意
2.配置属性维护到properties文件中
3.导入properties文件，classpath 目录代表java和resource包合并的classes目录下。
<context:property-placeholder location="classpath:/xxx.properties" />
4.在value标签中替换成${key}
```



## 自定义类型转换器

当Spring内部没有提供特定的类型转换器时，自己定义

1.实现自定义的converter<S,T> 接口

```java
public class DateConverter implements Converter<String, Date> {

    public Date convert(String source) {
        
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
```



2.Spring配置文件中配置自定义转换器

- DateConverter对象创建出来

  ```xml
  <bean id = "dateConverter" class="context.boot.converter.DateConverter" />
  ```

- 类型转换器的注册

  ```xml
  #告知Spring，自定义的dateConverter是一个类型转换器,
   <!--配置自定义converter-->
  <bean id="dateConverter" class="context.boot.converter.DateConverter"/>
  
  <!--用于注册类型转换器，id必须为id="conversionService"-->
  <bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
      <property name="converters">
          <set>
              <ref bean="dateConverter"/>
          </set>
      </property>
  </bean>
  ```

  

- ConversionServiceFactoryBean 定义 id属性 必须是conversionService
- Spring框架内部提供了日期类型转换器 支持的日期格式：yyyy/mm/dd



## 后置处理Bean

BeanPostProcessor作用：对Spring工厂所创建的对象，进行再加工

程序员实现BeanPostProcessor接口的方法

 ``` java
Object postProcessBeforeInitialization(Object bean, String beanName)
作用：Spring创建完对象，并进行注入后，可以运行Before方法进行加工
获得Spring创建好的对象：通过方法的参数Object bean
最终通过返回值交给Spring框架
    
Object postProcessAfterInitialization(Object bean, String beanName)
作用：Spring执行完对象的初始化操作后，可以执行After方法进行加工

实战中：
    很少处理Spring初始化操作，没有必要区分Before，After，只要实现其中的一个方法即可。
    
 ```

- 定义类，实现BeanPostProcessor接口

  ```java
  public class MyBeanProcessor implements BeanPostProcessor {
  
      @Override
      public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
          System.out.println("postProcessBeforeInitialization->" + beanName);
          if (bean instanceof Person) {
              Person person = ((Person) bean);
              person.setAge(12);
          }
          return bean;
      }
  
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
          System.out.println("postProcessAfterInitialization->" + beanName);
          if (bean instanceof Person) {
              Person person = ((Person) bean);
              person.setAge(14);
          }
          return bean;
      }
  }
  ```

  

- Spring配置

  ```xml
  <bean id="MyBeanProcessor" class="context.boot.initProcess.MyBeanProcessor"/>
  ```

 - BeanPostProcessor细节

   BeanPostProcessor会对Spring工厂中所有创建的对象进行加工。 







## AOP



### 1.代理设计模式

1.1概念

通过代理类，为原始类（目标类）增加额外的功能

2.好处：利于原始类的维护

1.2名词解释

原始类，目标类指的是业务类，只做核心功能

目标方法，原始方法指目标类原始类的方法

额外功能，附加功能（日志、事务、性能）



#### 1.静态代理：为每个原始类，手动新增原始类的proxy

代理开发的核心要素

代理类 = 目标类（原始类） + 额外功能 + 原始类实现相同的接口



静态代理存在的问题：

1.静态代理类文件数量过多，不利于项目管理

2.额外功能维护性差：代理类中 额外功能修改复杂，冗余



#### 2.动态代理

开发步骤

依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.9.4</version>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.5</version>
</dependency>
```



1.创建原始对象（目标对象）

```java
public class UserServiceImpl implements UserService {

    @Override
    public void register(Person person) {
        System.out.println("register" + person);
    }

    @Override
    public void login(String userName, String password) {
        System.out.println("login....success");
    }
}

```

```xml
 <bean id="userService" class="context.boot.proxy.UserServiceImpl"/>
```

2.额外功能

MethodBeforeAdvice接口

额外的功能书写在接口的实现中，在原始方法执行之前运行额外功能

```java
public class BeforeAdvisor implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("---log---");
    }
}
```

```xml
<bean id="beforeAdvisor" class="context.boot.proxy.BeforeAdvisor"/>
```



3.定义切点

```xml
切入点：额外功能加入的位置

目的：有程序员根据自己的需要，决定额外功能加入给哪个原始方法
register / login
```

```xml
<aop:config>
    <!--所有的方法都作为切入点，，都加入额外功能-->
    <aop:pointcut id="pc" expression="execution(* *(..))"/>
    <!--组装：目的把切入点和额外功能进行整合-->
    <aop:advisor id="advisor" pointcut-ref="pc" advice-ref="beforeAdvisor"/>
</aop:config>
```

4.获取动态代理对象执行方法

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//Spring的工厂通过原始对象的id值获得的是代理对象，userService此地是动态代理对象
UserService userService = (UserService) ctx.getBean("userService");
Person person = (Person) ctx.getBean("person3");
userService.register(person);
userService.login(person.getName(), "123");
```

```xml
---log---
postProcessBeforeInitialization->dateConverter
---log---
postProcessAfterInitialization->dateConverter
---log---
postProcessBeforeInitialization->conversionService
---log---
postProcessAfterInitialization->conversionService
---log---
---log---
---log---
postProcessAfterInitialization->conversionService
---log---
---log---
---log---
---log---
---log---
---log---
---log---
---log---
---log---
---log---
---log---
converting
---log---
postProcessBeforeInitialization->person3
---log---
postProcessAfterInitialization->person3
---log---
---log---
postProcessBeforeInitialization->userService
---log---
postProcessAfterInitialization->userService
---log---
---log---
---log---
---log---
registerPerson{name='Eddie', age=14, gender=1, birthday=Sun Jul 18 22:43:00 CST 1993}
---log---
---log---
login....success
```





##### 动态代理细节分析

1.Spring创建的动态代理类在哪里？

```xml
Spring框架在运行时，通过动态字节码技术，在JVM创建的，运行在JVM内部，等程序结束后，会和JVM一起消失
什么叫动态字节码技术：通过第三个动态字节码框架，在JVM中创建对应类的字节码，进行创建对象，当虚拟机结束，动态字节码跟着消失。
结论：动态代理不需要定义类文件，都是JVM运行过程中动态创建的，所以不会造成静态代理 类文件数量过多，影响项目管理的问题。
```

2.动态代理编程简化代理的开发

```xml
在额外功能不变的前提下，创建其他目标类（原始类）的代理对象时，只需要指定原始（目标）对象即可
```

3.动态代理额外功能可维护性大大增加



##### Spring动态代理详解

###### 1.额外功能

MethodBeforeAdvice：运行在原始方法执行之前

```java
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
```



MethodInterceptor：运行在原始方法之前，之后 ， 环绕

```java
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
        Object ret = invocation.proceed(); // 执行原始方法
        //TODO 添加原始方法之后的额外功能
        return ret;
    }
}
```

MethodInterceptor：运行在原始方法抛出异常时

```java
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
```



###### 2.切点详解

切入点决定额外功能加入位置（方法）

```xml
<!--所有的方法都作为切入点，，都加入额外功能-->
<aop:pointcut id="pc" expression="execution(* *(..))"/>

* 					*				(..)
修饰符+返回值		   包+类+方法		 参数
```

1.方法切入点

```xml
参数：非java.lang包中的类，必须要写全限定名
e.g.精准匹配
* com.xxx.UserServiceImpl.register(String,String) 
```

2.类切入点

```xml
指定特定类作为切入点（额外功能添加的位置），自然这个类中的所有方法，都会加上对应的额外功能
* com.xxx.UserServiceImpl.*(..)

忽略包
* *.UserServiceImpl.*(..)  //一级包
* *..UserServiceImpl.*(..)  //多级包
```

3.包切入点

```xml
#切入点包中的所有类，必须在xxx包中，不能在xxx包的子包中
* com.xxx.*.*(..)

#切入点在xxx包下及其子包的类
* com.xxx..*.*(..)
```



###### 3.切入点函数

切入点函数：用于执行切入点表达式

1.execution 

2.args

用于方法切点的参数表达式匹配

3.within

主要用于进行类、包切入点表达式的匹配

4.@annotation

为具有特殊注解的方法加入切入点

```java
//定义一个注解

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
}

//标注在需要加入切点的方法
public class UserServiceImpl implements UserService {

    @Log
    @Override
    public void register(Person person) {
        System.out.println("register" + person);
    }

    @Override
    public void login(String userName, String password) {
        System.out.println("login....success");
    }
}


//使用@annotation函数
 <aop:pointcut id="pc" expression="@annotation(context.boot.proxy.Log)"/>

```

5.切入点函数的逻辑运算

整合多个切入点函数一起配合，完成更加复杂的需求

- and 与操作

  ```xml
  案例：login同时满足两个参数
  
  execution(* login(String,String))
  execution(* login(..)) and args(String,String)
  
  与操作不能用于同种类型的切点函数
  
  ```

- or 或操作

  ```xml
  案例：将register 和 login 方法作为切入点
  
  execution(* register(..)) or execution(* login(..)) 
  ```







### AOP编程

```xml
1.原始对象
2.额外功能（MethodInterceptor)
3.切入点
4.组装切面（额外功能+切入点）
```



#### 1.aop底层实现

1.AOP如何创建动态代理类（动态字节码技术）

2.Spring工厂是如何加工创建代理对象

​	通过原始对象的id值最终获得的是代理对象



动态代理类的创建

1.JDK动态代理





2.cgLib动态代理





总结：

1.JDK动态代理 Proxy.newProxyInstance() 通过接口创建代理的实现类

2.Cglib动态代理 Enhancer 通过继承父类创建的代理类







BeanPostProcessor  Spring工厂是如何创建代理对象 ，在BeanPostProcessor中使用jdk动态代理





#### 2.基于注解的AOP编程

1.原始对象

2.额外功能

3.切入点

4.组装切面

```xml
通过切面类定义	   额外的功能点 around(ProceedingJoinPoint joinPoint)
				切入点 @Around("execution(* login(..))")
				组装切面类 @Aspect
```





