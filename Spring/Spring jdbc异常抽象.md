## Spring JDBC 异常抽象

### 异常处理

 使用传统JDBC API时，通过java.sql.SQLException类型来包括一切的异常情况，并没有异常具体告知，如果要得到具体异常内容要通过sqlexception的getErrorcode得到errorcode然后从具体数据库提供商提供的errorcode列表对比，得到最终的错误信息，同时异常为checked异常，需要客户端捕获。

 spring jdbc提供了统一异常处理机制，这套机制的基类为DataAccessException，是RuntimeException的一种类型，因此不需要客户端处理 ，Spring 会将数据操作的异常转换为 DataAccessException

![image-20210304104539074](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210304104539.png)



 

### JDBC异常转换过程

```java
@Nullable
private <T> T execute(StatementCallback<T> action, boolean closeResources) throws DataAccessException {
    Assert.notNull(action, "Callback object must not be null");

    Connection con = DataSourceUtils.getConnection(obtainDataSource());
    Statement stmt = null;
    try {
        stmt = con.createStatement();
        applyStatementSettings(stmt);
        T result = action.doInStatement(stmt);
        handleWarnings(stmt);
        return result;
    }
    catch (SQLException ex) {
        // Release Connection early, to avoid potential connection pool deadlock
        // in the case when the exception translator hasn't been initialized yet.
        String sql = getSql(action);
        JdbcUtils.closeStatement(stmt);
        stmt = null;
        DataSourceUtils.releaseConnection(con, getDataSource());
        con = null;
        //异常转换
        throw translateException("StatementCallback", sql, ex);
    }
    finally {
        if (closeResources) {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }
}


protected DataAccessException translateException(String task, @Nullable String sql, SQLException  ex) {
    //获取异常转换器并进行转换，将SQLException 转换成转义的DataAccessException
    DataAccessException dae = getExceptionTranslator().translate(task, sql, ex);
    return (dae != null ? dae : new UncategorizedSQLException(task, sql, ex));
}


/**
	 * Return the exception translator for this instance.
	 * <p>Creates a default {@link SQLErrorCodeSQLExceptionTranslator}
	 * for the specified DataSource if none set, or a
	 * {@link SQLStateSQLExceptionTranslator} in case of no DataSource.
	 * @see #getDataSource()
	 */
public SQLExceptionTranslator getExceptionTranslator() {
    SQLExceptionTranslator exceptionTranslator = this.exceptionTranslator;
    if (exceptionTranslator != null) {
        return exceptionTranslator;
    }
    synchronized (this) {
        exceptionTranslator = this.exceptionTranslator;
        if (exceptionTranslator == null) {
            DataSource dataSource = getDataSource();
            if (shouldIgnoreXml) {
                exceptionTranslator = new SQLExceptionSubclassTranslator();
            }
            else if (dataSource != null) {
                //根据DataSource创建SQLErrorCodeSQLExceptionTranslator
                exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            }
            else {
                exceptionTranslator = new SQLStateSQLExceptionTranslator();
            }
            this.exceptionTranslator = exceptionTranslator;
        }
        return exceptionTranslator;
    }
}


/**
	 * Boolean flag controlled by a {@code spring.xml.ignore} system property that instructs Spring to
	 * ignore XML, i.e. to not initialize the XML-related infrastructure.
	 * <p>The default is "false".
	 */
private static final boolean shouldIgnoreXml = SpringProperties.getFlag("spring.xml.ignore");
```

```java
SQLErrorCodeSQLExceptionTranslator


	/**
	 * Create an SQL error code translator for the given DataSource.
	 * Invoking this constructor will cause a Connection to be obtained
	 * from the DataSource to get the meta-data.
	 * @param dataSource the DataSource to use to find meta-data and establish
	 * which error codes are usable
	 * @see SQLErrorCodesFactory
	 */
	public SQLErrorCodeSQLExceptionTranslator(DataSource dataSource) {
		this();
		setDataSource(dataSource);
	}
	
	
	/**
	 * Constructor for use as a JavaBean.
	 * The SqlErrorCodes or DataSource property must be set.
	 */
	public SQLErrorCodeSQLExceptionTranslator() {
		setFallbackTranslator(new SQLExceptionSubclassTranslator()); //设置备用Translator：SQLExceptionSubclassTranslator
	}


	public SQLExceptionSubclassTranslator() {
		setFallbackTranslator(new SQLStateSQLExceptionTranslator()); //设置备用Translator：SQLStateSQLExceptionTranslator
	}

/**当一个异常需要转换时，先调用SQLErrorCodeSQLExceptionTranslator进行转换，SQLErrorCodeSQLExceptionTranslator无法转换时，便会调用它的备用转换器SQLExceptionSubclassTranslator进行转换，SQLExceptionSubclassTranslator也无法转换时，便会调用它的备用转换器SQLStateSQLExceptionTranslator进行转换。即 

SQLErrorCodeSQLExceptionTranslator --〉SQLExceptionSubclassTranslator--〉SQLStateSQLExceptionTranslator

SQLExceptionSubclassTranslator是Spring2.5新加入的异常转换器，作用是支持JDBC4.0新增的一些SQL异常 ，适用于JDK6及以上版本。 
*/
	/**
	 * Set the DataSource for this translator.
	 * <p>Setting this property will cause a Connection to be obtained from
	 * the DataSource to get the meta-data.
	 * @param dataSource the DataSource to use to find meta-data and establish
	 * which error codes are usable
	 * @see SQLErrorCodesFactory#getErrorCodes(javax.sql.DataSource)
	 * @see java.sql.DatabaseMetaData#getDatabaseProductName()
	 */
	public void setDataSource(DataSource dataSource) {
		//实例化SQLErrorCodes:
        this.sqlErrorCodes =
				SingletonSupplier.of(() -> SQLErrorCodesFactory.getInstance().resolveErrorCodes(dataSource));
		this.sqlErrorCodes.get();  // try early initialization - otherwise the supplier will retry later
	}


	//SQLErrorCodesFactory是个工厂类，它的作用是加载并实例化一个名为 sql-error-codes.xml
    // 首先，定义了两个路径变量，一个名为 SQL_ERROR_CODE_OVERRIDE_PATH，指向项目的根目录，从变量名可以看出，这个路径是供开发者扩展用的，用法就是把自定义的sql-error-codes.xml放到项目根目录下就可以了，另一个SQL_ERROR_CODE_DEFAULT_PATH 指向的就是默认路径了。 

	/**
	 * The name of custom SQL error codes file, loading from the root
	 * of the class path (e.g. from the "/WEB-INF/classes" directory).
	 */
	public static final String SQL_ERROR_CODE_OVERRIDE_PATH = "sql-error-codes.xml";

	/**
	 * The name of default SQL error code files, loading from the class path.
	 */
	public static final String SQL_ERROR_CODE_DEFAULT_PATH = "org/springframework/jdbc/support/sql-error-codes.xml";

	/**
	 * Create a new instance of the {@link SQLErrorCodesFactory} class.
	 * <p>Not public to enforce Singleton design pattern. Would be private
	 * except to allow testing via overriding the
	 * {@link #loadResource(String)} method.
	 * <p><b>Do not subclass in application code.</b>
	 * @see #loadResource(String)
	 */
	protected SQLErrorCodesFactory() {
		Map<String, SQLErrorCodes> errorCodes;

		try {
			DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();
			lbf.setBeanClassLoader(getClass().getClassLoader());
			XmlBeanDefinitionReader bdr = new XmlBeanDefinitionReader(lbf);

			// Load default SQL error codes. 先从默认路径加载
			Resource resource = loadResource(SQL_ERROR_CODE_DEFAULT_PATH);
			if (resource != null && resource.exists()) {
				bdr.loadBeanDefinitions(resource);
			}
			else {
				logger.info("Default sql-error-codes.xml not found (should be included in spring-jdbc jar)");
			}

			// Load custom SQL error codes, overriding defaults. 再从扩展路径加载 如果根目录存在配置文件，会覆盖默认配置 
			resource = loadResource(SQL_ERROR_CODE_OVERRIDE_PATH);
			if (resource != null && resource.exists()) {
				bdr.loadBeanDefinitions(resource);
				logger.debug("Found custom sql-error-codes.xml file at the root of the classpath");
			}

			// Check all beans of type SQLErrorCodes. 实例化SQLErrorCodes
			errorCodes = lbf.getBeansOfType(SQLErrorCodes.class, true, false);
			if (logger.isTraceEnabled()) {
				logger.trace("SQLErrorCodes loaded: " + errorCodes.keySet());
			}
		}
		catch (BeansException ex) {
			logger.warn("Error loading SQL error codes from config file", ex);
			errorCodes = Collections.emptyMap();
		}

		this.errorCodesMap = errorCodes;
	}
```

至此，SQLErrorCodeSQLException就初始化完成了。



接着调用translate 方法：

```
getExceptionTranslator().translate(task, sql, ex)
```

translate 方法定义在父类AbstractFallbackSQLExceptionTranslator中，是个模板方法

```java

	/**
	 * Pre-checks the arguments, calls {@link #doTranslate}, and invokes the
	 * {@link #getFallbackTranslator() fallback translator} if necessary.
	 */
	@Override
	@Nullable
	public DataAccessException translate(String task, @Nullable String sql, SQLException ex) {
		Assert.notNull(ex, "Cannot translate a null SQLException");
		//原始转换器进行转换,doTranslate对应的子类Override
		DataAccessException dae = doTranslate(task, sql, ex);
		if (dae != null) {
			// Specific exception match found.
			return dae;
		}

 
		// Looking for a fallback...转换不了，查找备用转换器
		SQLExceptionTranslator fallback = getFallbackTranslator();
		if (fallback != null) {
			return fallback.translate(task, sql, ex);
		}

		return null;
	}
```



SQLErrorCodeSQLExceptionTranslator 》doTranslate(String task, @Nullable String sql, SQLException ex)

1.如果异常是BatchUpdateException，需要通过sqlEx.getNextException()获取到SQLException，statement.executeBatch()可能会抛出BatchUpdateException,JDK API 声明未能正确执行发送到数据库的命令之一或者尝试返回结果集合，则抛BatchUpdateException. 

2.customTranslate是个空方法，开发者可以继承SQLErrorCodeSQLExceptionTranslator并重写customTranslate()，然后调用jdbcTemplate.setExceptionTranslator()覆盖默认的转换器即可。 

```java
    SQLException sqlEx = ex;
    if (sqlEx instanceof BatchUpdateException && sqlEx.getNextException() != null) {
        SQLException nestedSqlEx = sqlEx.getNextException();
        if (nestedSqlEx.getErrorCode() > 0 || nestedSqlEx.getSQLState() != null) {
            sqlEx = nestedSqlEx;
        }
    }
	// First, try custom translation from overridden method 
    DataAccessException dae = customTranslate(task, sql, sqlEx);
    if (dae != null) {
        return dae;
    }
		
```

2.查找是否存在CustomSqlExceptionTranslator，如果用户自己定义，override SQLExceptionTranslator 》translate方法

```java
    // Next, try the custom SQLException translator, if available.
    SQLErrorCodes sqlErrorCodes = getSqlErrorCodes();
    if (sqlErrorCodes != null) {
        SQLExceptionTranslator customTranslator = sqlErrorCodes.getCustomSqlExceptionTranslator();
        if (customTranslator != null) {
            DataAccessException customDex = customTranslator.translate(task, sql, sqlEx);
            if (customDex != null) {
                return customDex;
            }
        }
    }
```

SQLErrorCodes

```java
	@Nullable
	private SQLExceptionTranslator customSqlExceptionTranslator; 
	
	//通过反射
	public void setCustomSqlExceptionTranslatorClass(@Nullable Class<? extends SQLExceptionTranslator> customTranslatorClass) {
		if (customTranslatorClass != null) {
			try {
				this.customSqlExceptionTranslator =
						ReflectionUtils.accessibleConstructor(customTranslatorClass).newInstance();
			}
			catch (Throwable ex) {
				throw new IllegalStateException("Unable to instantiate custom translator", ex);
			}
		}
		else {
			this.customSqlExceptionTranslator = null;
		}
	}
	
	public void setCustomSqlExceptionTranslator(@Nullable SQLExceptionTranslator customSqlExceptionTranslator) {
		this.customSqlExceptionTranslator = customSqlExceptionTranslator;
	}
```



一个是普通的set方法，一个是可以通过class反射生成实例，这个属性也是给开发者扩展的，开发者可以在SQLErrorCodes中设置自定义的转换器实例，或者调用setCustomSqlExceptionTranslatorClass()方法传入class由框架自动生成实例。



自定义注册Translator：

使用CustomSQLExceptionTranslatorRegistry 》registerTranslator(String dbName, SQLExceptionTranslator translator) 为每个数据库种类注册自定义异常处理转义类

使用CustomSQLExceptionTranslatorRegistrar》setTranslators(Map<String, SQLExceptionTranslator> translators) 批量注册

<img src="https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210304115743.png" alt="image-20210304115743478" style="zoom: 70%;" />

3.根据SQLException循环调用查出对应的errorCode，再根据errorCode 匹配 sqlErrorCodes 的 错误码数组，抛出对应的sql异常。

```java
	// Check SQLErrorCodes with corresponding error code, if available.
    if (sqlErrorCodes != null) {
        String errorCode;
        /**
        指定用 sql state 或者 sql error code 来转换异常，框架默认使用 sql error code ，但是有的数据库spring是用sql state 来转换的 ，例如PostgreSQL，如下配置摘自 sql-error-codes.xml:
   
        <bean id="PostgreSQL" class="org.springframework.jdbc.support.SQLErrorCodes">
            <property name="useSqlStateForTranslation">
                <value>true</value> //设为true，则使用
            </property>
            <property name="badSqlGrammarCodes">
                <value>03000,42000,42601,42602,42622,42804,42P01</value>
            </property>
            <property name="duplicateKeyCodes">
                <value>23505</value>
            </property>
            <property name="dataIntegrityViolationCodes">
                <value>23000,23502,23503,23514</value>
            </property>
            <property name="dataAccessResourceFailureCodes">
                <value>53000,53100,53200,53300</value>
            </property>
            <property name="cannotAcquireLockCodes">
                <value>55P03</value>
            </property>
            <property name="cannotSerializeTransactionCodes">
                <value>40001</value>
            </property>
            <property name="deadlockLoserCodes">
                <value>40P01</value>
            </property>
		</bean>
    	*/
        if (sqlErrorCodes.isUseSqlStateForTranslation()) {
            errorCode = sqlEx.getSQLState();
        }
        else {
            /**
            这里也是因为有些方法可能抛出SQLException的子异常 ，作者用while循环读取当前异常的上一个异常，直到找到SQLException为止,这样做的好处			是,假如以后有新增的子类异常,都可以通过循环找到SQLException,举个栗子，例如　DataTruncation，它的继承体系是：

  			-- java.sql.SQLException
              -- java.sql.SQLWarning
              	-- java.sql.DataTruncation

			第一次循环找到的是 java.sql.SQLWarning，第二次循环的时候就找到SQLException了。然后获取error。 
			*/
            // Try to find SQLException with actual error code, looping through the causes.
            // E.g. applicable to java.sql.DataTruncation as of JDK 1.6.
            SQLException current = sqlEx;
            while (current.getErrorCode() == 0 && current.getCause() instanceof SQLException) {
                current = (SQLException) current.getCause();
            }
            errorCode = Integer.toString(current.getErrorCode());
        }
		
        if (errorCode != null) {
            // Look for defined custom translations first.
            CustomSQLErrorCodesTranslation[] customTranslations = sqlErrorCodes.getCustomTranslations();
            if (customTranslations != null) {
                for (CustomSQLErrorCodesTranslation customTranslation : customTranslations) {
                    if (Arrays.binarySearch(customTranslation.getErrorCodes(), errorCode) >= 0 &&
                        customTranslation.getExceptionClass() != null) {
                        DataAccessException customException = createCustomException(
                            task, sql, sqlEx, customTranslation.getExceptionClass());
                        if (customException != null) {
                            logTranslation(task, sql, sqlEx, true);
                            return customException;
                        }
                    }
                }
            }
            ...
        }
    }
```

SQLErrorCodes

```java
@Nullable
private CustomSQLErrorCodesTranslation[] customTranslations;
```

CustomSQLErrorCodesTranslation

```java
    /**
    定义errorCodes和DataAccessException对应关系
    */
	private String[] errorCodes = new String[0];

    @Nullable
    private Class<?> exceptionClass;


	/**
	 * 从trhow语句来看，这个类应该必须实现DataAccessException，这时，开发者就可以通过调用SQLErrorCodeSQLExceptionTranslator．		getSqlErrorCodes（）．setCustomTranslations（）设置自定义异常转换逻辑。 
	 * Set the exception class for the specified error codes.
	 */
	public void setExceptionClass(@Nullable Class<?> exceptionClass) {
		if (exceptionClass != null && !DataAccessException.class.isAssignableFrom(exceptionClass)) {
			throw new IllegalArgumentException("Invalid exception class [" + exceptionClass +
					"]: needs to be a subclass of [org.springframework.dao.DataAccessException]");
		}
		this.exceptionClass = exceptionClass;
	}
```



```java
    // Next, look for grouped error codes.
    if (Arrays.binarySearch(sqlErrorCodes.getBadSqlGrammarCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new BadSqlGrammarException(task, (sql != null ? sql : ""), sqlEx);
    }
    else if (Arrays.binarySearch(sqlErrorCodes.getInvalidResultSetAccessCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new InvalidResultSetAccessException(task, (sql != null ? sql : ""), sqlEx);
    }
    else if (Arrays.binarySearch(sqlErrorCodes.getDuplicateKeyCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new DuplicateKeyException(buildMessage(task, sql, sqlEx), sqlEx);
    }
    else if (Arrays.binarySearch(sqlErrorCodes.getDataIntegrityViolationCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new DataIntegrityViolationException(buildMessage(task, sql, sqlEx), sqlEx);
    }
    else if (Arrays.binarySearch(sqlErrorCodes.getPermissionDeniedCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new PermissionDeniedDataAccessException(buildMessage(task, sql, sqlEx), sqlEx);
    }
    else if (Arrays.binarySearch(sqlErrorCodes.getDataAccessResourceFailureCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new DataAccessResourceFailureException(buildMessage(task, sql, sqlEx), sqlEx);
    }
    else if (Arrays.binarySearch(sqlErrorCodes.getTransientDataAccessResourceCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new TransientDataAccessResourceException(buildMessage(task, sql, sqlEx), sqlEx);
    }
    else if (Arrays.binarySearch(sqlErrorCodes.getCannotAcquireLockCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new CannotAcquireLockException(buildMessage(task, sql, sqlEx), sqlEx);
    }
    else if (Arrays.binarySearch(sqlErrorCodes.getDeadlockLoserCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new DeadlockLoserDataAccessException(buildMessage(task, sql, sqlEx), sqlEx);
    }
    else if (Arrays.binarySearch(sqlErrorCodes.getCannotSerializeTransactionCodes(), errorCode) >= 0) {
        logTranslation(task, sql, sqlEx, false);
        return new CannotSerializeTransactionException(buildMessage(task, sql, sqlEx), sqlEx);
    }
```

![image-20210304121431358](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210304121431.png)

SQLErrorCodes的错误码数组加载org.springframework.jdbc.support 的配置文件sql-error-codes.xml

```xml
<bean id="MySQL" class="org.springframework.jdbc.support.SQLErrorCodes">
    <property name="databaseProductNames">
        <list>
            <value>MySQL</value>
            <value>MariaDB</value>
        </list>
    </property>
    <property name="badSqlGrammarCodes">
        <value>1054,1064,1146</value>
    </property>
    <property name="duplicateKeyCodes">
        <value>1062</value>
    </property>
    <property name="dataIntegrityViolationCodes">
        <value>630,839,840,893,1169,1215,1216,1217,1364,1451,1452,1557</value>
    </property>
    <property name="dataAccessResourceFailureCodes">
        <value>1</value>
    </property>
    <property name="cannotAcquireLockCodes">
        <value>1205,3572</value>
    </property>
    <property name="deadlockLoserCodes">
        <value>1213</value>
    </property>
</bean>
```



备用转换器

执行SQLExceptionSubclassTranslator的doTranslate方法，该方法比较简单，下面贴出：

```java
@Override
	@Nullable
	protected DataAccessException doTranslate(String task, @Nullable String sql, SQLException ex) {
		if (ex instanceof SQLTransientException) {
			if (ex instanceof SQLTransientConnectionException) {
				return new TransientDataAccessResourceException(buildMessage(task, sql, ex), ex);
			}
			else if (ex instanceof SQLTransactionRollbackException) {
				return new ConcurrencyFailureException(buildMessage(task, sql, ex), ex);
			}
			else if (ex instanceof SQLTimeoutException) {
				return new QueryTimeoutException(buildMessage(task, sql, ex), ex);
			}
		}
		else if (ex instanceof SQLNonTransientException) {
			if (ex instanceof SQLNonTransientConnectionException) {
				return new DataAccessResourceFailureException(buildMessage(task, sql, ex), ex);
			}
			else if (ex instanceof SQLDataException) {
				return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
			}
			else if (ex instanceof SQLIntegrityConstraintViolationException) {
				return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
			}
			else if (ex instanceof SQLInvalidAuthorizationSpecException) {
				return new PermissionDeniedDataAccessException(buildMessage(task, sql, ex), ex);
			}
			else if (ex instanceof SQLSyntaxErrorException) {
				return new BadSqlGrammarException(task, (sql != null ? sql : ""), ex);
			}
			else if (ex instanceof SQLFeatureNotSupportedException) {
				return new InvalidDataAccessApiUsageException(buildMessage(task, sql, ex), ex);
			}
		}
		else if (ex instanceof SQLRecoverableException) {
			return new RecoverableDataAccessException(buildMessage(task, sql, ex), ex);
		}

		// Fallback to Spring's own SQL state translation...
		return null;
	}
```

如果还是未能转换，SQLExceptionSubclassTranslatorｄ的备用转换器是SQLStateSQLExceptionTranslator，程序又一次流转回AbstractFallbackSQLExceptionTranslator中的translate方法中的叹号部分：执行SQLStateSQLExceptionTranslator的doTranslate方法



```java
	@Override
	@Nullable
	protected DataAccessException doTranslate(String task, @Nullable String sql, SQLException ex) {
		// First, the getSQLState check...
		String sqlState = getSqlState(ex);
		if (sqlState != null && sqlState.length() >= 2) {
			String classCode = sqlState.substring(0, 2);
			if (logger.isDebugEnabled()) {
				logger.debug("Extracted SQL state class '" + classCode + "' from value '" + sqlState + "'");
			}
			if (BAD_SQL_GRAMMAR_CODES.contains(classCode)) {
				return new BadSqlGrammarException(task, (sql != null ? sql : ""), ex);
			}
			else if (DATA_INTEGRITY_VIOLATION_CODES.contains(classCode)) {
				return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
			}
			else if (DATA_ACCESS_RESOURCE_FAILURE_CODES.contains(classCode)) {
				return new DataAccessResourceFailureException(buildMessage(task, sql, ex), ex);
			}
			else if (TRANSIENT_DATA_ACCESS_RESOURCE_CODES.contains(classCode)) {
				return new TransientDataAccessResourceException(buildMessage(task, sql, ex), ex);
			}
			else if (CONCURRENCY_FAILURE_CODES.contains(classCode)) {
				return new ConcurrencyFailureException(buildMessage(task, sql, ex), ex);
			}
		}

		// For MySQL: exception class name indicating a timeout?
		// (since MySQL doesn't throw the JDBC 4 SQLTimeoutException)
		if (ex.getClass().getName().contains("Timeout")) {
			return new QueryTimeoutException(buildMessage(task, sql, ex), ex);
		}

		// Couldn't resolve anything proper - resort to UncategorizedSQLException.
		return null;
	}
```

根据sql state 进行转换，sql state 的值用静态变量定义在该类里：

```java
	private static final Set<String> BAD_SQL_GRAMMAR_CODES = new HashSet<>(8);

	private static final Set<String> DATA_INTEGRITY_VIOLATION_CODES = new HashSet<>(8);

	private static final Set<String> DATA_ACCESS_RESOURCE_FAILURE_CODES = new HashSet<>(8);

	private static final Set<String> TRANSIENT_DATA_ACCESS_RESOURCE_CODES = new HashSet<>(8);

	private static final Set<String> CONCURRENCY_FAILURE_CODES = new HashSet<>(4);

	static {
		BAD_SQL_GRAMMAR_CODES.add("07");  // Dynamic SQL error
		BAD_SQL_GRAMMAR_CODES.add("21");  // Cardinality violation
		BAD_SQL_GRAMMAR_CODES.add("2A");  // Syntax error direct SQL
		BAD_SQL_GRAMMAR_CODES.add("37");  // Syntax error dynamic SQL
		BAD_SQL_GRAMMAR_CODES.add("42");  // General SQL syntax error
		BAD_SQL_GRAMMAR_CODES.add("65");  // Oracle: unknown identifier

		DATA_INTEGRITY_VIOLATION_CODES.add("01");  // Data truncation
		DATA_INTEGRITY_VIOLATION_CODES.add("02");  // No data found
		DATA_INTEGRITY_VIOLATION_CODES.add("22");  // Value out of range
		DATA_INTEGRITY_VIOLATION_CODES.add("23");  // Integrity constraint violation
		DATA_INTEGRITY_VIOLATION_CODES.add("27");  // Triggered data change violation
		DATA_INTEGRITY_VIOLATION_CODES.add("44");  // With check violation

		DATA_ACCESS_RESOURCE_FAILURE_CODES.add("08");  // Connection exception
		DATA_ACCESS_RESOURCE_FAILURE_CODES.add("53");  // PostgreSQL: insufficient resources (e.g. disk full)
		DATA_ACCESS_RESOURCE_FAILURE_CODES.add("54");  // PostgreSQL: program limit exceeded (e.g. statement too complex)
		DATA_ACCESS_RESOURCE_FAILURE_CODES.add("57");  // DB2: out-of-memory exception / database not started
		DATA_ACCESS_RESOURCE_FAILURE_CODES.add("58");  // DB2: unexpected system error

		TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JW");  // Sybase: internal I/O error
		TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JZ");  // Sybase: unexpected I/O error
		TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("S1");  // DB2: communication failure

		CONCURRENCY_FAILURE_CODES.add("40");  // Transaction rollback
		CONCURRENCY_FAILURE_CODES.add("61");  // Oracle: deadlock
	}
```

如果匹配到，返回对应的异常实例，如果匹配不到，接着有点特殊处理：

```java
	// For MySQL: exception class name indicating a timeout?
    // (since MySQL doesn't throw the JDBC 4 SQLTimeoutException)
    if (ex.getClass().getName().contains("Timeout")) {
    return new QueryTimeoutException(buildMessage(task, sql, ex), ex);
    }
```

意思大概是mysql不支持JDBC 4.0 规范定义SQLTimeoutException异常，只好用类名是否包含Timeout字样来判断。如果包含，则抛出QueryTimeoutException异常。否则返回空，程序流转回AbstractFallbackSQLExceptionTranslator中的translate方法中的叹号部分： 

```java
public DataAccessException translate(@Nullable String task, @Nullable String sql, SQLException ex) {
    Assert.notNull(ex, "Cannot translate a null SQLException");
    if (task == null) {
    	task = "";
    }
    if (sql == null) {
    	sql = "";
    }

    DataAccessException dex = doTranslate(task, sql, ex);
    if (dex != null) { //!!!
    	// Specific exception match found.
    	return dex;
    }
    // Looking for a fallback...
    SQLExceptionTranslator fallback = getFallbackTranslator();
    if (fallback != null) {
    	return fallback.translate(task, sql, ex);
    }
    // We couldn't identify it more precisely.
    return new UncategorizedSQLException(task, sql, ex);
} 
```



SQLStateSQLExceptionTranslator没有备用转换器,fallback=null，到此，转换工作就完成了，大概的流程就是先由　SQLErrorCodeSQLExceptionTranslator　进行转换，如果转换不到，再由SQLExceptionSubclassTranslator进行转换，如果转换不到，再由SQLStateSQLExceptionTranslator进行转换，如果还转换不到程序抛出异常。可以从以上的分析得出结论，３个转换器中，SQLErrorCodeSQLExceptionTranslator是最复杂的，功能也最强大的，有资料显示,sql error code 比sql state准确很多，而SQLExceptionSubclassTranslator也只是对JDBC4.0规范的的补充 。



### JDBC异常转换器之自定义异常转换器

SQLErrorCodeSQLExceptionTranslator.doTranslate()方法有多处接口可共开发人员进行扩展

#### 方法一：继承SQLErrorCodeSQLExceptionTranslator并overridecustomTransaction方法

![image-20210304151928008](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210304151928.png)



首先定义自定义异常类，自定义异常类需要继承DataAccessException

DataAccessException 有两个构造器，如果希望打印原异常信息，就覆盖带有Throwable参数的，如果覆盖只有msg参数的构造器，原异常信息不会被打印。



```java
public class CustomSQLException extends DataAccessException {
 
	public CustomSQLException(String msg, Throwable cause) {
		super(msg, cause);		
	}
 
} 
```

然后定义一个异常转换器，必须继承自SQLErrorCodeSQLExceptionTranslator，重写customTranslate()方法，

```java
public  class CustomSQLErrorCodeTranslator extends SQLErrorCodeSQLExceptionTranslator {
 
	@Override	
	protected DataAccessException customTranslate(String task, String sql,SQLException sqlEx) {		
	  return new CustomSQLException("扩展方法一") ;
	}	
 
}
```

用自定义转换器替换默认的SQLErrorCodeSQLExceptionTranslator转换器

```java
jdbcTemplate.setExceptionTranslator(new CustomSQLErrorCodeTranslator());
```



#### 方法二，自定义的异常处理转换器实现自SQLExceptionTranslator，既从如下扩展



![image-20210304151850489](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210304151850.png)

```java

public  class CustomSQLErrorCodeTranslator implements  SQLExceptionTranslator {
 
	public DataAccessException translate(String task, String sql,	SQLException ex) {		
		return new CustomSQLException("扩展方法二",ex);
	}
 
} 
```

将自定义转换器传给SQLErrorCodes的customSqlExceptionTranslator，

```java
if(jdbcTemplate.getExceptionTranslator() instanceof SQLErrorCodeSQLExceptionTranslator ){
	SQLErrorCodeSQLExceptionTranslator translator=(SQLErrorCodeSQLExceptionTranslator) jdbcTemplate.getExceptionTranslator();
	translator.getSqlErrorCodes().setCustomSqlExceptionTranslatorClass(CustomSQLErrorCodeTranslator.class);
}
```



####  方法三，利用SQLErrorCodes的customTranslations属性进行扩展，既从如下扩展：

![image-20210304153434775](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210304153434.png)

CustomSQLErrorCodesTranslation有两个属性，errorCodes和exceptionClass,既一个异常类对应一对应一组error code ,异常类我们还用CustomSQLException，codes随便写一些 。

```java
if(	jdbcTemplate.getExceptionTranslator() instanceof SQLErrorCodeSQLExceptionTranslator ){
    SQLErrorCodeSQLExceptionTranslator translator=(SQLErrorCodeSQLExceptionTranslator) jdbcTemplate.getExceptionTranslator();

    CustomSQLErrorCodesTranslation  tran=new CustomSQLErrorCodesTranslation();
    tran.setErrorCodes("8152");
    tran.setExceptionClass(CustomSQLException.class);	
    translator.getSqlErrorCodes().setCustomTranslations(tran);		    			
} 
```



#### 方法四，通过扩展 sql-error-codes.xml 进行扩展

```xml
<bean id="customDB" class="org.springframework.jdbc.support.SQLErrorCodes">
    <property name="databaseProductName">
        <value>customDB</value>
    </property>
    <property name="customTranslations">
        <list>
            <bean class="org.springframework.jdbc.support.CustomSQLErrorCodesTranslation">
                <property name="errorCodes">10000</property>
                <property name="exceptionClass">
                    test.CustomSQLException
                </property>
            </bean>
        </list>
    </property>
</bean> 
```

