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

[1]: https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction	"Transaction Management"
[2]: https://www.open-open.com/lib/view/open1350865116821.html	"Spring 事务机制详解"



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

3) Java持久化API事务（JpaTransactionManager）

Hibernate多年来一直是事实上的Java持久化标准，但是现在Java持久化API作为真正的Java持久化标准进入大家的视野。如果你计划使用JPA的话，那你需要使用Spring的JpaTransactionManager来处理事务。你需要在Spring中这样配置JpaTransactionManager： 

```xml
<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
     <property name="sessionFactory" ref="sessionFactory" />
</bean>
```

JpaTransactionManager只需要装配一个JPA实体管理工厂（javax.persistence.EntityManagerFactory接口的任意实现）。

JpaTransactionManager将与由工厂所产生的JPA EntityManager合作来构建事务。

4) Java原生API事务(JtaTransactionManager)

如果你没有使用以上所述的事务管理，或者是跨越了多个事务管理源（比如两个或者是多个不同的数据源），你就需要使用JtaTransactionManager：

```xml
<bean id="transactionManager" class="org.springframework.transaction.jta.JtaTransactionManager">
    <property name="transactionManagerName" value="java:/TransactionManager" />
</bean>
```

JtaTransactionManager将事务管理的责任委托给javax.transaction.UserTransaction和javax.transaction.TransactionManager对象，其中事务成功完成通过UserTransaction.commit()方法提交，事务失败通过UserTransaction.rollback()方法回滚。 



##### TransactionDefinition

事务管理器接口PlatformTransactionManager通过getTransaction(TransactionDefinition definition)方法来得到事务，这个方法里面的参数是TransactionDefinition类，这个类就定义了一些基本的事务属性。 

事务属性可以理解成事务的一些基本配置，描述了事务策略如何应用到方法上。

事务属性包含了5个方面:

![这里写图片描述](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210303144358.png)

**传播机制、隔离级别、是否只读、回滚规则、事务超时**

![image-20210303111000395](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210303111000.png)



**事务的传播机制**

事务的传播性一般用在事务嵌套的场景，比如一个事务方法里面调用了另外一个事务方法，那么两个方法是各自作为独立的方法提交还是内层的事务合并到外层的事务一起提交，这就是需要事务传播机制的配置来确定怎么样执行。

七种传播行为：

- PROPAGATION_REQUIRED：如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是最常见的选择。

  Spring默认的传播机制，能满足绝大部分业务需求，如果外层有事务，则当前事务加入到外层事务，一块提交，一块回滚。如果外层没有事务，新建一个事务执行

- PROPAGATION_SUPPORTS：支持当前事务，如果当前没有事务，就以非事务方式执行。

  如果外层有事务，则加入外层事务，如果外层没有事务，则直接使用非事务方式执行。完全依赖外层的事务

- PROPAGATION_MANDATORY：与NEVER相反，如果外层没有事务，则抛出异常

- PROPAGATION_REQUIRES_NEW：该事务传播机制是每次都会新开启一个事务，同时把外层事务挂起，当当前事务执行完毕，恢复上层事务的执行。如果外层没有事务，执行当前新开启的事务即可。需要使用 JtaTransactionManager作为事务管理器,访问TransactionManager

- PROPAGATION_NOT_SUPPORTED：该传播机制不支持事务，如果外层存在事务则挂起，执行完当前代码，则恢复外层事务，无论是否异常都不会回滚当前的代码。需要使用 JtaTransactionManager作为事务管理器,访问TransactionManager

- PROPAGATION_NEVER：以非事务方式执行，如果当前存在事务，则抛出异常。 该传播机制不支持外层事务，即如果外层有事务就抛出异常

- PROPAGATION_NESTED：如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。

  嵌套事务一个非常重要的概念就是内层事务依赖于外层事务。**外层事务失败时，会回滚内层事务所做的动作。而内层事务操作失败并不会引起外层事务的回滚。**

  使用JDBC 3.0驱动时,仅仅支持DataSourceTransactionManager作为事务管理器。需要JDBC 驱动的java.sql.Savepoint类。

  有一些JTA的事务管理器实现可能也提供了同样的功能。

  使用PROPAGATION_NESTED，还需要把PlatformTransactionManager的nestedTransactionAllowed属性设为true;而 nestedTransactionAllowed属性值默认为false;

虽然有7种，但是常用的就第一种REQUIRED和第四种REQUIRES_NEW 



**PROPAGATION_NESTED 与PROPAGATION_REQUIRES_NEW** 

都像一个嵌套事务，如果不存在一个活动的事务，都会开启一个新的事务。

区别：

- 使用 PROPAGATION_REQUIRES_NEW时，内层事务与外层事务就像两个独立的事务一样，一旦内层事务进行了提交后，外层事务不能对其进行回滚。两个事务互不影响。两个事务不是一个真正的嵌套事务。同时它需要JTA事务管理器的支持。
- 使用PROPAGATION_NESTED时，外层事务的回滚可以引起内层事务的回滚。而内层事务的异常并不会导致外层事务的回滚，它是一个真正的嵌套事务。DataSourceTransactionManager使用savepoint支持PROPAGATION_NESTED时，需要JDBC 3.0以上驱动及1.4以上的JDK版本支持。其它的JTA TrasactionManager实现可能有不同的支持方式。
- PROPAGATION_REQUIRES_NEW 启动一个新的, 不依赖于环境的 "内部" 事务. 这个事务将被完全 commited 或 rolled back 而不依赖于外部事务, 它拥有自己的隔离范围, 自己的锁, 等等. 当内部事务开始执行时, 外部事务将被挂起, 内务事务结束时, 外部事务将继续执行。
- PROPAGATION_NESTED 开始一个 "嵌套的" 事务,  它是已经存在事务的一个真正的子事务. 潜套事务开始执行时,  它将取得一个 savepoint. 如果这个嵌套事务失败, 我们将回滚到此 savepoint. 嵌套事务是外部事务的一部分, 只有外部事务结束后它才会被提交。



传播规则回答了这样一个问题：**一个新的事务应该被启动还是被挂起，或者是一个方法是否应该在事务性上下文中运行。**



**事务的隔离级别**

事务的隔离级别定义一个事务可能受其他并发务活动活动影响的程度，可以把事务的隔离级别想象为这个事务对于事物处理数据的自私程度。

在一个典型的应用程序中，多个事务同时运行，经常会为了完成他们的工作而操作同一个数据。并发虽然是必需的，但是会导致以下问题：

1. 脏读（Dirty read）
   脏读发生在一个事务读取了被另一个事务改写但尚未提交的数据时。如果这些改变在稍后被回滚了，那么第一个事务读取的数据就会是无效的。

2. 不可重复读（Nonrepeatable read）<u>不可重复读重点在修改</u>
   不可重复读发生在一个事务执行相同的查询两次或两次以上，但每次查询结果都不相同时。这通常是由于另一个并发事务在两次查询之间更新了数据。

3. 幻读（Phantom reads）<u>幻读重点在新增或删除</u>
   幻读和不可重复读相似。当一个事务（T1）读取几行记录后，另一个并发事务（T2）插入了一些记录时，幻读就发生了。在后来的查询中，第一个事务（T1）就会发现一些原来没有的额外记录。

   

五个隔离级别：

- ISOLATION_DEFAULT：**这是一个PlatfromTransactionManager默认的隔离级别，使用<u>数据库默认的事务隔离级别</u>.**

另外四个与JDBC的隔离级别相对应；

- ISOLATION_READ_UNCOMMITTED：这是事务最低的隔离级别，它允许别外一个事务可以看到这个事务未提交的数据，允许读取尚未提交的更改。 

这种隔离级别会产生脏读，不可重复读和幻像读。

- ISOLATION_READ_COMMITTED：（Oracle 默认级别）保证一个事务修改的数据提交后才能被另外一个事务读取。另外一个事务不能读取该事务未提交的数据。

这种事务隔离级别可以避免脏读出现，但是可能会出现不可重复读和幻像读。

- ISOLATION_REPEATABLE_READ：（MYSQL默认级别）这种事务隔离级别可以防止脏读，不可重复读。但是可能出现幻像读。

  对相同字段的多次读取的结果是一致的，除非数据被当前事务本身改变。可防止脏读和不可重复读，但幻读仍可能发生。

  它除了保证一个事务不能读取另一个事务未提交的数据外，还保证了避免下面的情况产生。

```
不可重复读：一个事务读取了一行数据，第二个事务修改了此行数据，第一个事物重复读取了此行，两次查询得到了两个值。
```

- ISOLATION_SERIALIZABLE：这是花费最高代价但是最可靠的事务隔离级别。事务被处理为顺序执行。完全服从ACID的隔离级别，确保不发生脏读、不可重复读和幻影读。这在所有隔离级别中也是最慢的，因为它通常是通过完全锁定当前事务所涉及的数据表来完成的。

  除了防止脏读，不可重复读外，还避免了幻像读。 

```
幻像读：一个事务读取了满足where条件的所有行数据，第二个事务插入了满足where条件的数据行，第一个事物又一次通过where条件查询了一次，发现第二次查询多出来幻象行数据（由第二个事务提交插入的数据）
```



**只读**

如果一个事务只对数据库执行读操作，那么该数据库就可能利用那个事务的只读特性，采取某些优化措施。通过把一个事务声明为只读，可以给后端数据库一个机会来应用那些它认为合适的优化措施。

由于只读的优化措施是在一个事务启动时由后端数据库实施的， 因此，只有对于那些具有可能启动一个新事务的传播行为（PROPAGATION_REQUIRES_NEW、PROPAGATION_REQUIRED、 ROPAGATION_NESTED）的方法来说，将事务声明为只读才有意义。



**事务超时**

为了使一个应用程序很好地执行，它的事务不能运行太长时间。因此，声明式事务的下一个特性就是它的超时。

假设事务的运行时间变得格外的长，由于事务可能涉及对数据库的锁定，所以长时间运行的事务会不必要地占用数据库资源。这时就可以声明一个事务在特定秒数后自动回滚，不必等它自己结束。

由于超时时钟在一个事务启动的时候开始的，因此，只有对于那些具有可能启动一个新事务的传播行为（PROPAGATION_REQUIRES_NEW、PROPAGATION_REQUIRED、ROPAGATION_NESTED）的方法来说，声明事务超时才有意义。



**回滚规则**

在默认设置下，事务只在出现运行时异常（runtime exception | Error）时回滚，而在出现受检查异常（checked exception）时不回滚（这一行为和EJB中的回滚行为是一致的）。
不过，可以声明在出现特定受检查异常时像运行时异常一样回滚。同样，也可以声明一个事务在出现特定的异常时不回滚，即使特定的异常是运行时异常。



#### 编程式事务

##### 编程式和声明式事务的区别

Spring提供了对编程式事务和声明式事务的支持，编程式事务允许用户在代码中精确定义事务的边界，而声明式事务（基于AOP）有助于用户将操作与事务规则进行解耦。
简单地说，编程式事务侵入到了业务代码里面，但是提供了更加详细的事务管理；而声明式事务由于基于AOP，所以既能起到事务管理的作用，又可以不影响业务代码的具体实现。

##### 如何实现编程式事务

Spring提供两种方式的编程式事务管理，分别是：使用TransactionTemplate和直接使用PlatformTransactionManager。

######  使用TransactionTemplate

采用TransactionTemplate和采用其他Spring模板，如JdbcTempalte和HibernateTemplate是一样的方法。它使用回调方法，把应用程序从处理取得和释放资源中解脱出来。如同其他模板，TransactionTemplate是线程安全的。代码片段：

```java
 TransactionTemplate tt = new TransactionTemplate(); // 新建一个TransactionTemplate
    Object result = tt.execute(
        new TransactionCallback(){  
            public Object doTransaction(TransactionStatus status){  
                updateOperation();  
                return resultOfUpdateOperation();  
            }  
    }); // 执行execute方法进行事务管理 
```

TransactionTemplate -》execute源码：

```java
public <T> T execute(TransactionCallback<T> action) throws TransactionException {
    Assert.state(this.transactionManager != null, "No PlatformTransactionManager set");//获取trancationManager
	
    if (this.transactionManager instanceof CallbackPreferringPlatformTransactionManager) {
        return ((CallbackPreferringPlatformTransactionManager) this.transactionManager).execute(this, action);
    }
    else {
        //根据传播机制获取当前事务或创建一个新的事务
        TransactionStatus status = this.transactionManager.getTransaction(this);
        T result;
        try {
            //执行业务操作
            result = action.doInTransaction(status);
        }
        catch (RuntimeException | Error ex) { //回滚事务
            // Transactional code threw application exception -> rollback
            rollbackOnException(status, ex);
            throw ex;
        }
        catch (Throwable ex) {
            // Transactional code threw unexpected exception -> rollback
            rollbackOnException(status, ex);//回滚事务
            throw new UndeclaredThrowableException(ex, "TransactionCallback threw undeclared checked exception");
        }
        //提交事务
        this.transactionManager.commit(status);
        return result;
    }
}
```



###### 使用PlatformTransactionManager

```java
PlatformTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(); //定义一个某个框架平台的TransactionManager，如JDBC、Hibernate
dataSourceTransactionManager.setDataSource(this.getJdbcTemplate().getDataSource()); // 设置数据源
DefaultTransactionDefinition transDef = new DefaultTransactionDefinition(); // 定义事务属性
transDef.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED); // 设置传播行为属性
TransactionStatus status = dataSourceTransactionManager.getTransaction(transDef); // 获得事务状态
try {
    // 数据库操作
    dataSourceTransactionManager.commit(status);// 提交
} catch (Exception e) {
    dataSourceTransactionManager.rollback(status);// 回滚
}
```



#### 声明式事务

 Spring配置文件中关于事务配置总是由三个组成部分，分别是**DataSource**、**TransactionManager**和**代理机制**这三部分，无论哪种配置方式，一般变化的只是代理机制这部分。

 DataSource、TransactionManager这两部分只是会根据数据访问方式有所变化，比如使用Hibernate进行数据访问时，DataSource实际为SessionFactory，TransactionManager的实现为HibernateTransactionManager。

![image-20210303162541618](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210303163057.png)



 根据代理机制的不同，总结了五种Spring事务的配置方式，配置文件如下：

   第一种方式：每个Bean都有一个代理

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context-2.5.xsd
           http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd">

    <bean id="sessionFactory" 
            class="org.springframework.orm.hibernate3.LocalSessionFactoryBean"> 
        <property name="configLocation" value="classpath:hibernate.cfg.xml" /> 
        <property name="configurationClass" value="org.hibernate.cfg.AnnotationConfiguration" />
    </bean> 

    <!-- 定义事务管理器（声明式的事务） --> 
    <bean id="transactionManager"
        class="org.springframework.orm.hibernate3.HibernateTransactionManager">
        <property name="sessionFactory" ref="sessionFactory" />
    </bean>
   
    <!-- 配置DAO -->
    <bean id="userDaoTarget" class="com.bluesky.spring.dao.UserDaoImpl">
        <property name="sessionFactory" ref="sessionFactory" />
    </bean>
   
    <bean id="userDao" 
        class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean"> 
           <!-- 配置事务管理器 --> 
           <property name="transactionManager" ref="transactionManager" />    
        <property name="target" ref="userDaoTarget" /> 
         <property name="proxyInterfaces" value="com.bluesky.spring.dao.GeneratorDao" />
        <!-- 配置事务属性 --> 
        <property name="transactionAttributes"> 
            <props> 
                <prop key="*">PROPAGATION_REQUIRED</prop>
            </props> 
        </property> 
    </bean> 
</beans>
```

`TransactionProxyFactoryBean`

HISTORICAL NOTE: This class was originally designed to cover the typical case of declarative transaction demarcation: namely, <u>wrapping a singleton target object with a transactional proxy, proxying all the interfaces that the target implements</u>. <u>However, in Spring versions 2.0 and beyond, the functionality provided here is superseded by the more convenient tx: XML namespace.</u> See the declarative transaction management  section of the Spring reference documentation to understand modern options for managing transactions in Spring applications. <u>For these reasons, users should favor the tx: XML namespace as well as the @Transactional and @EnableTransactionManagement annotations.</u>







事务的属性可同通过注解方式或配置文件配置:

- 注解方式：

  @Transactional只能被应用到public方法上,对于其它非public的方法,如果标记了@Transactional也不会报错,但方法没有事务功能.
  默认情况下,一个有事务方法, 遇到RuntimeException 时会回滚 . 遇到 受检查的异常 是不会回滚 的. 要想所有异常都回滚,要加上 @Transactional( rollbackFor={Exception.class,其它异常}) 

  

![image-20210303120312358](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210303120312.png)

```java
@Transactional(
    readOnly = default false;, //读写事务
    timeout = -1 ,     //事务的超时时间，-1为无限制
    noRollbackFor = ArithmeticException.class, //遇到指定的异常不回滚
    isolation =default Isolation.DEFAULT;, //事务的隔离级别，此处使用后端数据库的默认隔离级别
    propagation = default Propagation.REQUIRED; //事务的传播行为 
)
```

- 配置文件( aop拦截器方式):

  ```xml
  
  <tx:advice id="advice" transaction-manager="txManager">
      <tx:attributes>
          <!-- tx:method的属性:
                  * name 是必须的,表示与事务属性关联的方法名(业务方法名),对切入点进行细化。通配符 
                       （*）可以用来指定一批关联到相同的事务属性的方法。
                        如：'get*'、'handle*'、'on*Event'等等.
                  * propagation：不是必须的,默认值是REQUIRED表示事务传播行为,
                    包括REQUIRED,SUPPORTS,MANDATORY,REQUIRES_NEW,NOT_SUPPORTED,NEVER,NESTED
                  * isolation：不是必须的 默认值DEFAULT ，表示事务隔离级别(数据库的隔离级别)
                  * timeout：不是必须的 默认值-1(永不超时)，表示事务超时的时间（以秒为单位）
                  * read-only：不是必须的 默认值false不是只读的表示事务是否只读？
                  * rollback-for： 不是必须的表示将被触发进行回滚的 Exception(s)；以逗号分开。
                     如：'com.foo.MyBusinessException,ServletException'
                  * no-rollback-for：不是必须的表示不被触发进行回滚的 Exception(s),以逗号分开。                                        
                     如：'com.foo.MyBusinessException,ServletException'   
                  任何 RuntimeException 将触发事务回滚，但是任何 checked Exception 将不触发事务回滚                     
              -->
          <tx:method name="save*" propagation="REQUIRED" isolation="DEFAULT" read-only="false"/>
          <tx:method name="update*" propagation="REQUIRED" isolation="DEFAULT" read-only="false"/>
          <tx:method name="delete*" propagation="REQUIRED" isolation="DEFAULT" read-only="false"  rollback-for="Exception"/>
          <!-- 其他的方法之只读的 -->
          <tx:method name="*" read-only="true"/>
      </tx:attributes>
  </tx:advice>
  ```

  

