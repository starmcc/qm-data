# Qm Data

## 描述

### 前言

数据持久层在框架中已经封装成为`QmData`，开发时直接使用`QmData`提供的方法进行数据操作。

它是基于`Mybatis`进行的一系列封装，由框架内部实现了`QmData`的接口。

### 特点

* 自动SQL增删改查操作(ORM)
* 高度自由的数据持久交互
* 永久开源

### 实现过程

`Spring`中获取`SqlSessionFactory`，进而使用`Mybatis`的`SqlSession`传递命名空间和参数列表进行一系列的封装。

### 半自动SQL

在使用过程中，只需要对实体类进行一些必要的注解修饰，就可以利用实体类进行自动SQL增删改查，无需书写SQL完成业务数据库操作。也可以自行书写SQL完成复杂的业务数据库操作。



## 基础配置

> **(1)** 查看最新版本依赖库 **[点击查看最新版本(Releases)](<https://github.com/starmcc/qm-data/releases>)**

> **(2)**  `maven`需要增加`mybatis`依赖，添加`QmData`依赖库。
>
> 可在中央仓库查询：https://search.maven.org/

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>x.x.x</version>
</dependency>
<!-- 下方为依赖库引入 -->
<dependency>
  <groupId>com.starmcc</groupId>
  <artifactId>qm-data</artifactId>
  <version>x.x.x</version>
</dependency>
```

> **(3)** 基于`SpringBoot` 获取 `Mybatis-SqlSessionFactory`，创建专属`QmData`的`configuration`配置文件

```java
@Configuration
public class QmDataConfig {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public QmData initQmData(){
        // 使用QmDataFactory.createInstance 构建 QmData实例交由Spring管理
        return QmDataFactory.createInstance(sqlSessionFactory);
    }
}
```

> **(4)** 配置`mybatis`基础扫描，以`application.yml`配置文件为例

```yaml
mybatis: 
  # 指定实体类包路径
  type-aliases-package: com.xx.xx.xx.entity
  # 指定QmData基础Maapper路径
  # 建议使用classpath*:**/*Mapper.xml
  # 可扫描整个项目中带有Mapper.xml结尾的文件
  mapper-locations: 
   - 'classpath:com/starmcc/qmdata/mapper/*.xml'
  configuration:
   #是否启动数据库下划线自动映射实体
   map-underscore-to-camel-case: true
   # resultMap 自动映射级别设置
   auto-mapping-behavior: full
```

**注意mapper-locations需要扫描上述指定的路径**



## 进入开发流程

### *Step.1*

> 开发过程中，在`Service`中注入`QmData`。

```java
@Service
public class UserServiceImpl implements UserService {

	@Resource
	private QmData qmData;
}
```

### *Step.2*

#### 使用通用方法

> 调用`QmData`中提供的通用方法

```Java
@Override
public List<User> getList(User user) {
    return qmData.autoSelectList(user, User.class);
}
```

#### 使用基础方法

##### 【1】创建对应业务的Mapper文件

> 请根据`yml`全局配置文件中的`mybatis`配置项`mapper-locations`的扫描路径存放`Mapper`文件。
>
> ```
> mapper-locations:
> - 'classpath*:**/*Mapper.xml' # 该行配置请勿修改
> ```
>
> **如需特殊配置，该行配置请勿修改。请自行增加一条扫描路径。**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace是自定义的，但请规范它，规范在帮助文档中有所提及 -->
<mapper namespace="UserManager-UserServiceImpl-0-Mapper">
    
	<select id="getUser" resultType="hashmap">
		select * from `qm_user`
	</select>
    
</mapper>
```

> **这里说明namespace的约定规范**
>
> `Mybatis`自动扫描`Mapper`后，将该`Mapper`注册到`Spring`中。
>
> 如果名字不进行规范，会有一些预料不到的错误发生，`spring`内管理的`bean`相互冲突是我们不想看到的。
>
> 如果不按照格式，则直接抛出异常。

```java
namespace="调用模块-调用类名-编号-Mapper"  //大驼峰 + 规范化
```

##### 【2】Service调用QmData基础方法

```java
@Service
public class UserServiceImpl implements UserService {

	@Resource
	QmData qmData;

	private static final String NAMESCAPSE = "Info-UserServiceImpl-0-Mapper";

	@Override
	public List<User> getList(String userName) {
		return qmData.selectList(NAMESCAPSE + "getUser", userName);
	}
}
```

### *Step.3*

> 实体类中使用框架提供的注解`@QmTable`、`@QmId`，这些注解类似于`JPA`注解的使用

```java
@Table(name="qm_user")
public class User {
	@Id
	private Integer id;
	private String userName;
	private String password;
	private Integer roleId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", roleId=" + roleId + "]";
	}
}
```

> 框架目前只提供3个注解，下面有详细的注解说明，自动SQL是通过实体类的注解进行SQL封装。

### *Step.4*

> 编写控制器接口，访问测试成功获取数据。



## 实体类注解说明

> 目前支持的注解

---

### @Table

> 注明该实体类对应数据库的表名，使用`QmData`的`auto`系列必须注明该注解。一共有两个参数，`name`/`style`，如果不提供任何参数，则默认以类名作为表名。

#### 设置表名

* 属性(`name`)

  > 该属性标注实体类对应的表名，使用`QmData`的`auto`方法必须提供该注解。标注在实体类的class上即可。

#### 设置实体类风格

* 属性(`style`)

  > 该属性标注该实体类字段风格类型，参数类型`Style`枚举类。
  >
  > **默认为`Style.UNDERLINE`即下划线模式**，如要修改为驼峰模式则提供`Style.HUMP`。

---

### @Id

> 注明该字段为主键字段，使用`QmData`的`autoUpdate`、`autoDelete`方法必须提供该注解，而这些方法是通过id进行对数据表的操作。

#### 设置主键别名

* 属性(`name`)

  > 当类中主键属性名与数据库的主键字段名不一致时，使用`name`属性可改变`QmData`识别的主键字段名，默认为使用当前属性名作为主键。

---

### @Param

> 当该字段不需要识别或名称与数据库不一致时，可以使用该注解进行标注。

#### 设置字段别名

* 属性(`name`)

  > 当类中属性名与数据库的字段名不一致时，使用`name`属性可改变`QmData`识别的字段名，默认为使用当前属性名作为字段名。

#### 排除字段


* 属性(`except`)

  > 如果在实体类中需要排除某些字段不进行操作，则给`except`设置为`true`即可。默认为`flase`。



## 已知问题解决方案

### 打包后Mapper文件缺失问题

在集成后，如果mapper的xml摆放在项目包中，spring boot 没有将其打包导致的问题，可以在pom.xml文件中的build节点下添加如下配置

```xml
<resources>
    <resource>
        <directory>src/main/java</directory>
        <!--配置添加xml,mybatis的xml需要打包-->
        <includes>
            <include>**/*.xml</include>
        </includes>
    </resource>
    <resource>
        <directory>src/main/resources</directory>
        <!--配置添加resource资源文件下所有文件-->
        <includes>
            <include>**/*.*</include>
        </includes>
    </resource>
</resources>
```



### 出现mapper文件namespace重复问题

使用`qm-data`时，`namespace`将不在是根据包路径进行扫描，而是通过自定义的方式进行规定，请合理规范化该属性，避免出现重复问题出现，后续会对这一块进行整体优化。