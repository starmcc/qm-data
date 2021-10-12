# Qm-Data

## 1. Preface

### 1.1 Introduce

数据持久层在框架中已经封装成为`QmData`，开发时直接使用`QmData`提供的方法进行数据操作。

它是基于`Mybatis`进行的一系列封装，由框架内部实现了`QmData`的接口。

### 1.2 Trait

- 自动`SQL`增删改查操作
- 普通增删改查无需编写XML和DAO
- 高度自由的数据持久交互
- 永久开源

### 1.3 Realization Process

`Spring`中获取`SqlSessionTemplate`，进而使用`Mybatis`的`SqlSession`传递命名空间和参数列表进行一系列的封装。


### 1.4 Semi Auto SQL

在使用过程中，只需要对实体类进行一些必要的注解修饰，就可以利用实体类进行自动`SQL`增删改查，无需书写`SQL`完成业务数据库操作。

也可以自行书写`SQL`完成复杂的业务数据库操作。

### 1.5 Help Document

[**Show Help Document**](https://www.starmcc.com/qm-data/)

### 1.6 Update Version Log

[**Show Version**](https://www.starmcc.com/qm-data/UpdateLog.html)

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
  <version>x.x.x-RELEASE</version>
</dependency>
```

## 3. Environment

* `mybatis-spring-boot-starter 2.2.0`

## 4. Main Content

主要依赖`Mybatis`拓展的一个自动化SQL工具集。