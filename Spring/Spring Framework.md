# Spring Framework



## 特性总览

### 核心

- IoC容器
- Spring事件
- 资源管理器
- 国际化
- 校验
- 数据绑定
- 类型转换
- Spring表达式
- 面向切面编程

### 数据存储

- JDBC
- 事物抽象
- DAO支持
- O/R映射
- XML编列

### Web Servlet技术栈

- Spring MVC
- WebSocket
- SockJs

### Web Reactive 技术栈

- Spring WebFlux
- WebClient
- WebSocket

### 技术整合

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

  

  

  

### Spring注入

1.什么是注入

通过Spring工厂及配置文件，为所创建对象的成员变量赋值

2.为什么需要注入

解耦合

3.注入原理

1)set注入

2)构造器注入

#### Set注入详解

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

#### 构造注入详解

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



#### 反转控制和依赖注入

##### 1.反转控制IOC

控制：对于成员变量赋值的控制权

反转控制：把对于成员变量赋值的控制权，从代码中反转到Spring工厂和配置文件中完成

​	好处：解耦合

底层实现：工厂设计模式

##### 2.依赖注入DI

注入：通过spring的工厂和配置文件，为对象的成员变量赋值

依赖注入：当一个类需要依赖另一个类时，将依赖的类作为成员变量，最终通过spring配置文件进行注入

​	好处：解耦

##### 3.Spring工厂创建复杂对象

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

- 简单对象 配置bean

- 复杂对象 FactoryBean、实例工厂、静态工厂

特殊创建方式：

- 通过ServiceLoaderFactoryBean （配置元信息：XML、Java注解和Java API）

- 通过AutowireCapableBeanFactory#createBean(java.lang.Class,int ,boolean)
- 通过BeanDefinitionRegistry#registerBeanDefinition(String,BeanDefinition)



##### 4.控制Spring工厂创建对象的次数

1.如何控制简单对象的创建次数

``` xml
#单例，创建一次，默认单例
<bean scope=“singleton” id="xxx" class ="xxx"/> 
#每次都会创建一个新的对象
<bean scope=“prototype” id="xxx" class ="xxx"/> 
```

2.如何控制复杂对象的创建次数

FactoryBean接口的isSingleton方法返回true和false

实例工厂和静态工厂可在xml配置scope

3.为什么要控制对象的创建次数

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



### 对象的生命周期

- 创建阶段

  ``` xml
  scope="singleton"
  Spring工厂创建的同时，对象也创建
  如果想配置Spring工厂会在获取对象的时候(依赖查找时)，创建对象，添加lazy-init= "true" 标签
  scope="propotype"
  Spring工厂会在获取对象的时候，创建对象
  ```

- 初始化阶段

  ```xml
  Spring工厂在创建完对象之后，调用对象的初始化方法，完成对应的初始化操作
  
  1.初始化方法提供：程序员根据需求，提供初始化方法，最终完成初始化操作
  2.初始化方法调用：Spring工厂进行调用
  ```

  - @PostConstruct 标注方法

  - 实现initializingBean接口的afterPropertiesSet()方法

    ``` xml
    afterPropertiesSet()
    ```

  - 自定义初始化方法：

    对象中提供一个普通的方法用来提供初始化
    
    ```xml
  1.Xml 配置： <bean init-method="xxxx" ></bean>
    2.Java 注解：@Bean(initMethod = "init")
    3.Java API：AbstractBeanDefinition#setInitMethodName(String)
    ```
  ```
    
  细节
    1.如果一个对象即实现initializingBean接口又配置了init-method的普通初始化方法
  
    先执行initializtingBean ， 再 执行init-method 配置的方法
  
    2.注入发生在初始化操作的前面
  
    3.什么叫做初始化操作？
    
    资源的初始化：数据库 io 网络...
  ```

- Bean延迟初始化(Lzay Initialization)

  - XML配置 

    ``` xml
    <bean lazy-init= "true" /> 
    ```

  - Java 注解：@Lazy(true)

- 销毁阶段

  ```xml
  Spring销毁对象前，会调用对象的销毁方法，完成销毁操作
  
  1.Spring什么时候销毁创建的对象？
  ctx.close();
  2.销毁方法：程序员自己定义销毁方法，Spring工厂调用销毁方法
  ```

  - @PreDestroy 标注方法

  - DisposableBean接口（销毁时先执行）

    ```xml
    destroy()
    ```

  - 定义普通的销毁方法（销毁时后执行）

    ```xml
    1.Xml 配置： <bean destroy-method="xxxx" ></bean>
    2.Java 注解：@Bean(destroy = "destroy")
  3.Java API：AbstractBeanDefinition#setDestroyMethodName(String)
    ```

    细节

    1.销毁方法的操作只适用于scope=“singleton”的对象

    2.什么叫做销毁操作？
    
    资源的释放操作

- Spring 垃圾回收销毁的Bean

  - 关闭Spring容器（应用上下文）
  - 执行GC
  - Spring Bean覆盖的finalize()方法被回调

  

### 配置文件参数化

把Spring配置文件中需要经常修改的字符串信息，转移到一个更小的配置文件中，利于Spring配置文件的维护

```xml
1.提供一个properties文件，名称随意，放置位置随意
2.配置属性维护到properties文件中
3.导入properties文件，classpath 目录代表java和resource包合并的classes目录下。
<context:property-placeholder location="classpath:/xxx.properties" />
4.在value标签中替换成${key}
```



### 自定义类型转换器

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



### 后置处理Bean

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



### AOP

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



Pointcut注解





动态代理的创建方式

默认情况下 AOP编程 底层应用JDK动态代理创建方式

如果切换Cglib

```xml
1.基于注解AOP开发
<aop:aspectj-autoproxy proxy-target-class="true" />
2.传统AOP开发
<aop:config proxy-target-class="true"></aop:config>
```



#### 3.AOP总结

AOP编程概念（Spring的动态代理开发）

概念：通过代理类为原始类添加额外功能

好处：利于原始类的维护



AOP编程的开发（动态代理开发）

1.原始对象

2.额外功能

3.切入点

4.组装切面



基于配置的开发

基于注解的开发



## 数据存储



### Java事务

#### 什么是事务

事务（Transaction），一般是指要做的或所做的事情。在计算机术语中是指访问并可能更新数据库中各种数据项的一个程序执行单元(unit)。事务通常由高级数据库操纵语言或编程语言（如SQL，C++或Java）书写的用户程序的执行所引起，并用形如begin transaction和end transaction语句（或函数调用）来界定。事务由事务开始(begin transaction)和事务结束(end transaction)之间执行的全体操作组成。

#### 为什么要事务

事务是**为解决数据安全操作提出的，事务控制实际上就是控制数据的安全访问**。

用一个简单例子说明：银行转帐业务，账户A要将自己账户上的1000元转到B账户下面，A账户余额首先要减去1000元，然后B账户要增加1000元。假如在中间网络出现了问题，A账户减去1000元已经结束，B因为网络中断而操作失败，那么整个业务失败，必须做出控制，要求A账户转帐业务撤销。这才能保证业务的正确性，完成这个操走就需要事务，将A账户资金减少和B账户资金增加放到同一个事务里，**要么全部执行成功，要么全部撤销，这样就保证了数据的安全性**。

#### 事务的4个特性（ACID）

- 原子性（atomicity）：事务是数据库的逻辑工作单位，而且是必须是原子工作单位，对于其数据修改，要么全部执行，要么全部不执行。
- 一致性（consistency）：事务在完成时，必须是所有的数据都保持一致状态。在相关数据库中，所有规则都必须应用于事务的修改，以保持所有数据的完整性。（实例：转账，两个账户余额相加，值不变。）
- 隔离性（isolation）：一个事务的执行不能被其他事务所影响。
- 持久性（durability）：一个事务一旦提交，事物的操作便永久性的保存在DB中。即便是在数据库系统遇到故障的情况下也不会丢失提交事务的操作。

#### Java有几种类型的事务

- JDBC事务
- JTA（Java Transaction API）事务
- 容器事务

##### JDBC事务

在JDBC中处理事务，都是通过Connection完成的。同一事务中所有的操作，都在使用同一个Connection对象。JDBC事务默认是开启的，并且是默认提交。

JDBC Connection 接口提供了两种事务模式：自动提交和手工提交

JDBC中的事务java.sql.Connection 的三个方法与事务有关：

setAutoCommit（boolean）:设置是否为自动提交事务，如果true（默认值为true）表示自动提交，也就是每条执行的SQL语句都是一个单独的事务，如果设置为false，需要手动提交事务。

```java
//手动提交事务需手动执行一下方法

void commit() throws SQLException // 提交事务
//Makes all changes made since the previous commit/rollback permanent and releases any database locks currently held by this Connection object. This method should be used only when auto-commit mode has been disabled.

void rollback() throws SQLException //回滚事务
//Undoes all changes made in the current transaction and releases any database locks currently held by this Connection object. This method should be used only when auto-commit mode has been disabled.
```

传统JDBC操作流程：

 1).获取JDBC连接  2).声明SQL  3).预编译SQL  4).执行SQL  5).处理结果集  

 6).释放结果集  7).释放Statement  8).提交事务  9).处理异常并回滚事务 10).释放JDBC连接

JDBC优缺点

1.冗长、重复     2.显示事务控制     3.每个步骤不可获取    4.显示处理受检查异常

JDBC为使用Java进行数据库的事务操作提供了最基本的支持。通过JDBC事务，我们可以将多个SQL语句放到同一个事务中，保证其ACID特性。JDBC事务的主要优点就是API比较简单，可以实现最基本的事务操作，性能也相对较好。

但是，JDBC事务有一个局限：**一个 JDBC 事务不能跨越多个数据库**！所以，如果涉及到多数据库的操作或者分布式场景，JDBC事务就无能为力了。

##### JTA事务

JTA(Java Transaction API)提供了跨数据库连接（或其他JTA资源）的事务管理能力。JTA事务管理则由JTA容器实现，J2ee框架中事务管理器与应用程序，资源管理器，以及应用服务器之间的事务通讯。

- JTA的构成
  - 高层应用事务界定接口，供事务客户界定事务边界的
  - X/Open XA协议(资源之间的一种标准化的接口)的标准Java映射，它可以使事务性的资源管理器参与由外部事务管理器控制的事务中
  - 高层事务管理器接口，允许应用程序服务器为其管理的应用程序界定事务的边界
  
- JTA的主要接口位于javax.transaction包中

  - UserTransaction接口：让应用程序得以控制事务的开始、挂起、提交、回滚等。由Java客户端程序或EJB调用。
  - TransactionManager 接口：用于应用服务器管理事务状态
  - Transaction接口：用于执行相关事务操作
  - XAResource接口：用于在分布式事务环境下，协调事务管理器和资源管理器的工作
  - Xid接口：为事务标识符的Java映射

  注：前3个接口位于Java EE版的类库 javaee.jar 中，Java SE中没有提供！UserTransaction是编程常用的接口,JTA只提供了接口，没有具体的实现。

  JTS(Java Transaction Service)是服务OTS的JTA的实现。简单的说JTS实现了JTA接口，并且符合OTS的规范。

  JTA的事务周期可横跨多个JDBC Connection生命周期，对众多Connection进行调度，实现其事务性要求。

  JTA可以处理任何提供符合XA接口的资源。包括：JDBC连接，数据库，JMS，商业对象等等。 

- JTA编程的基本步骤

  - 首先配置JTA ，建立相应的数据源

  - 建立事务：通过创建UserTransaction类的实例来开始一个事务。

    ```java
    Context ctx = new InitialContext(p) ;
    
    UserTransaction trans = (UserTransaction) ctx.lookup("javax.Transaction.UserTransaction")
    ```

  - 开始事务：代码为 trans.begin() ;

  - 找出数据源：从Weblogic Server上找到数据源DataSource ds = (DataSource) ctx.lookup(“mysqldb") ;

  - 建立数据库连接：Connection mycon = ds.getConnection() ;

  - 执行SQL操作：stmt.executeUpdate(sqlS);

  - 完成事务：trans.commit(); / trans.rollback();

  - 关闭连接：mycon.close() ;

JTA的优缺点：

JTA的优点很明显，就是提供了分布式事务的解决方案，严格的ACID。但是，标准的JTA方式的事务管理在日常开发中并不常用。

JTA的缺点是实现复杂，通常情况下，JTA UserTransaction需要从JNDI获取。这意味着，如果我们使用JTA，就需要同时使用JTA和JNDI。

JTA本身就是个笨重的API，通常JTA只能在应用服务器环境下使用，因此使用JTA会限制代码的复用性。



##### Spring容器事务

Spring事务管理涉及的接口及其联系：

![img](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210302143040.png)

### 事务抽象

#### 事务抽象的核心接口

##### PlatformTransactionManager

- DataSourceTransactionManager
- HibernateTransactionMangaer
- JpaTransactionManager
- JtaTransactionManager

Spring并不直接管理事务，而是提供了多种事务管理器，他们将事务管理的职责委托给Hibernate或者JTA等持久化机制所提供的相关平台框架的事务来实现。 Spring事务管理器的接口是org.springframework.transaction.PlatformTransactionManager，通过这个接口，Spring为各个平台如JDBC、Hibernate等都提供了对应的事务管理器，但是具体的实现就是各个平台自己的事情了。

1) Spring JDBC事务

如果应用程序中直接使用JDBC来进行持久化，DataSourceTransactionManager会为你处理事务边界。为了使用     DataSourceTransactionManager，你需要使用如下的XML将其装配到应用程序的上下文定义中：

```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
     <property name="dataSource" ref="dataSource" />
</bean>
```

 实际上，DataSourceTransactionManager是通过调用java.sql.Connection来管理事务。通过调用连接的commit()方法来提交事务，同样，事务失败则通过调用rollback()方法进行回滚。

2) Hibernate事务

如果应用程序的持久化是通过Hibernate实现的，那么你需要使用HibernateTransactionManager。对于Hibernate3，需要在Spring上下文定义中添加如下的<bean>声明：

```xml

<bean id="transactionManager" class="org.springframework.orm.hibernate3.HibernateTransactionManager">
     <property name="sessionFactory" ref="sessionFactory" />
</bean>
 
sessionFactory属性需要装配一个Hibernate的session工厂，HibernateTransactionManager的实现细节是它将事务管理的职责委托给org.hibernate.Transaction对象，而后者是从Hibernate Session中获取到的。当事务成功完成时，HibernateTransactionManager将会调用Transaction对象#的commit()方法，反之，将会调用rollback()方法。
```

3) Java持久化API事务（JPA）

Hibernate多年来一直是事实上的Java持久化标准，但是现在Java持久化API作为真正的Java持久化标准进入大家的视野。如果你计划使用JPA的话，那你需要使用Spring的JpaTransactionManager来处理事务。你需要在Spring中这样配置JpaTransactionManager： 

```xml
<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
     <property name="sessionFactory" ref="sessionFactory" />
</bean>
```

JpaTransactionManager只需要装配一个JPA实体管理工厂（javax.persistence.EntityManagerFactory接口的任意实现）。

JpaTransactionManager将与由工厂所产生的JPA EntityManager合作来构建事务。



##### TransactionDefinition

事务管理器接口PlatformTransactionManager通过getTransaction(TransactionDefinition definition)方法来得到事务，这个方法里面的参数是TransactionDefinition类，这个类就定义了一些基本的事务属性。 

事务属性可以理解成事务的一些基本配置，描述了事务策略如何应用到方法上。

事务属性包含了5个方面:传播行为、隔离规则、回滚规则、事务超时、是否只读

