# Qm-Data

## 1. Preface

### 1.1 Introduce


数据持久层在框架中已经封装成为`QmData`，开发时直接使用`QmData`提供的方法进行数据操作。

它是基于`Mybatis`进行的一系列封装，由框架内部实现了`QmData`的接口。

### 1.2 Trait

- 自动`SQL`增删改查操作
- 高度自由的数据持久交互
- 永久开源

### 1.3 Realization Process

`Spring`中获取`SqlSessionTemplate`，进而使用`Mybatis`的`SqlSession`传递命名空间和参数列表进行一系列的封装。

---

**注意：** 

> 如果你还在使用`1.x`版本，内部还是使用`SqlSessionFactory`。

> **在`2.x`版本后将被替换为`SqlSessionTemplate`，因为它是线程安全的，并且对于Spring的事务机制进行友好的支持。**


### 1.4 Semi Auto SQL

在使用过程中，只需要对实体类进行一些必要的注解修饰，就可以利用实体类进行自动`SQL`增删改查，无需书写`SQL`完成业务数据库操作。也可以自行书写`SQL`完成复杂的业务数据库操作。

### 1.5 Help Document

|    Version    |                Document Help                 |
| :-----------: | :------------------------------------------: |
|  2.0.0-BETA   | [Reference Doc.](/qm-data/200/基础配置.html) |
| 1.1.1-RELEASE | [Reference Doc.](/qm-data/111/基础配置.html) |

### 1.6 Update Version Log

[**Show Version**](/qm-data/UpdateLog.html)

### 1.7 Open Source

[**https://github.com/starmcc/qm-data**](https://github.com/starmcc/qm-data)

## 2. Maven Warehouse

- [中央仓库查询](https://search.maven.org/)
- [阿里仓库查询](https://maven.aliyun.com/mvn/search)
- [Maven镜像仓库查询](https://mvnrepository.com/artifact/com.starmcc)

```xml
<dependency>
  <groupId>com.starmcc</groupId>
  <artifactId>qm-data</artifactId>
</dependency>
```

## 3. Environment

* `java - JDK 1.8` 
* `Maven 2.0` 
* `2.x` > `mybatis-spring-boot-starter 2.1.1` 
* `1.x` > `mybatis 3.4.6` 

## 4. Main Content

主要依赖`Mybatis`拓展的一个自动化SQL插件。