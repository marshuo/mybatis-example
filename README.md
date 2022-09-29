这是一个mybatis 学习项目

包括整理的mybatis笔记和对应的源码。

# MyBatis笔记

## 1.简介

### 1.1 什么是 MyBatis？

- MyBatis 是一款优秀的持久层框架
- 它支持自定义 SQL、存储过程以及高级映射
- MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作
- MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

#### 1.1.1 持久层

- 最靠近数据库的层，完成数据库的访问
- 持久层的作用： 完成数据持久化。
- 持久化：就是把内存中的数据存储到数据库中。

#### 1.1.2 为什么需要持久层

- 内存价格相对昂亏
- 数据重要性
- 完成持久化工作

### 1.2 获取MyBatis

- Maven

  ```xml
  <!--mybatis-->
  <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.10</version>
  </dependency>
  ```
- GitHub：https://github.com/mybatis/mybatis-3/releases
- 官方中文文档：https://mybatis.org/mybatis-3/zh/index.html

### 1.3 MyBatis重要性

- 使用广泛
- 简化JDBC代码

## 2.  第一个Mybatis程序

- 学习步骤：
  1. 环境搭建
  2. 引入MyBatis相关依赖
  3. 编码代码
  4. 测试

### 2.1 创建MySql数据库表

```MySQL
CREATE DATABASE `mybatis`;

USE `mybatis`;

CREATE TABLE `users`(
    `id` INT(20) NOT NULL PRIMARY KEY,
    `name` VARCHAR(30) DEFAULT NULL,
    `pwd` VARCHAR(30) DEFAULT NULL
)ENGINE=INNODB DEFAULT CHARSET=utf8


INSERt INTO `users` VALUES
( 1,'张三','123456'),
( 2,'李四','123'),
( 3,'王五','abc')
```

### 2.2 驱动与依赖

```xml
<!--mybatis-->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.10</version>
</dependency>

<!--mysql-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.30</version>
</dependency>

<!--junit-->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>

<!--lombok-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
</dependency>
```

### 2.3 MyBatis工具类

#### 2.3.1 xml核心配置文件

##### 2.3.1.1 官方声明

- MyBatis 包含一个名叫 Resources 的工具类，它包含一些实用方法，使得从类路径或其它位置加载资源文件更加容易。

  ```java
  String resource = "org/mybatis/example/mybatis-config.xml";
  InputStream inputStream = Resources.getResourceAsStream(resource);
  SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
  ```
- 【默认】XML核心配置文件 => 【mybatis-config.xml】

  XML 配置文件中包含了对 MyBatis 的核心设置，包括获取数据库连接的数据源（DataSource）以及决定事务作用域和控制方式的事务管理器（TransactionManager）。

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <!-- configuration标签 => 声明MyBatis核心配置 -->
  <configuration>
      <!-- environments标签 => 设置MyBatis选用的环境信息 -->
      <environments default="development">
          <environment id="development">
              <!-- transactionManager标签 => 事务管理 -->
              <transactionManager type="JDBC"/>
              <!-- dataSource标签 => 配置数据源属性 -->
              <dataSource type="POOLED">
                  <property name="driver" value="${driver}"/>
                  <property name="url" value="${url}"/>
                  <property name="username" value="${username}"/>
                  <property name="password" value="${password}"/>
              </dataSource>
          </environment>
      </environments>
      <mappers>
          <mapper resource="org/mybatis/example/BlogMapper.xml"/>
      </mappers>
  </configuration>
  ```

#### 2.3.1.2 自定义核心配置文件

- 按照本地环境信息，自定义配置 => 【注意】==amp;== （转义字符amp;）

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <!-- configuration标签 => 声明MyBatis核心配置 -->
  <configuration>
      <!-- environments标签 => 设置MyBatis选用的环境信息 -->
      <environments default="development">
          <environment id="development">
              <!-- transactionManager标签 => 事务管理 -->
              <transactionManager type="JDBC"/>
              <!-- dataSource标签 => 配置数据源属性 -->
              <dataSource type="POOLED">
                  <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                  <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=CST"/>
                  <property name="username" value="root"/>
                  <property name="password" value="123456"/>
              </dataSource>
          </environment>
      </environments>
  </configuration>
  ```

### 2.3.2 SqlSessionFactory

#### 2.3.2.1 官方声明

　　​

> 即：SqlSessionFactory（MyBatis应用必需）实例 ---需要---> SqlSessionFactoryBuilder ---需要---> XML核心配置文件【mybatis-config.xml】

- 官方默认MyBatis应用类

  ```java
  // 定义XML核心配置文件路径信息
  String resource = "org/mybatis/example/mybatis-config.xml";
  // 读取XML核心配置文件路径信息
  InputStream inputStream = Resources.getResourceAsStream(resource);
  // 获得实例化SQLSessionFactory
  SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
  ```

#### 2.3.2.2 自定义配置

- 按照本地环境信息，自定义配置

  ```java
  private static SqlSessionFactory sqlSessionFactory;
  static {
      try {
          // 定义XML核心配置文件路径信息
          String resource = "mybatis-config.xml";
          // 读取XML核心配置文件路径信息
          InputStream inputStream = Resources.getResourceAsStream(resource);
          // 获得实例化SQLSessionFactory
          sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  ```

### 2.3.3 SqlSession

#### 2.3.3.1 官方声明

　　​

> 即：通过SQLSessionFactory获得SqlSession对象，使用SqlSession对象执行所有SQL方法。

#### 2.3.3.2 SqlSession接口方法

- 调用SqlSessionFactory.openSession()方法，返回SqlSession对象

  ```java
  // 静态方法获取SqlSession对象，通过SqlSessionFactory.openSession()方法
  public static SqlSession getSqlSession(){
      return sqlSessionFactory.openSession();
  }
  ```

### 2.3.4 完整工具类实现

- 将以上对象结合

  ```java
  package com.marshuo.utils;

  import org.apache.ibatis.io.Resources;
  import org.apache.ibatis.session.SqlSession;
  import org.apache.ibatis.session.SqlSessionFactory;
  import org.apache.ibatis.session.SqlSessionFactoryBuilder;

  import java.io.IOException;
  import java.io.InputStream;

  /**
   * @author mars
   * @date 2022/09/20
   */
  public class MyBatisUtils {
      private static SqlSessionFactory sqlSessionFactory;
    
      static {
          try {
              String resource = "mybatis-config.xml";
              InputStream inputStream = Resources.getResourceAsStream(resource);
              sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }

      public static SqlSession getSqlSession() {
          return sqlSessionFactory.openSession();
      }
  }

  ```

### 2.4 实体类Pojo

- 创建实体类Users，匹配数据库所需字段。

  ```java
  package com.marshuo.entity;

  import lombok.AllArgsConstructor;
  import lombok.Data;
  import lombok.NoArgsConstructor;

  /**
   * @author mars
   * @date 2022/09/20
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class User {
      private Integer id;
      private String name;
      private String password;
  }
  ```

### 2.5 Dao层【接口】

- 操作实体类，完成与数据库的操作。

  ```java
  package com.marshuo.mapper;

  import com.marshuo.entity.User;

  import java.util.List;

  /**
   * @author mars
   * @date 2022/09/20
   */
  public interface UserMapper {
      User getUserById(int id);
  }

  ```

### 2.6 Mapper

### 2.6.1 Mapper配置绑定

#### 2.6.1.1 官方声明

- 官方实例 => UsersMappers

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <!-- mapper标签： 【namespace】： 指定dao层，绑定Dao -->
  <mapper namespace="org.mybatis.example.BlogMapper">
    <!-- SQL语句执行区 -->
    <select id="selectBlog" resultType="Blog">
      select * from Blog where id = #{id}
    </select>
  </mapper>
  ```

#### 2.6.1.2 自定义配置

- 按照本地环境信息，自定义配置

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.marshuo.mapper.UserMapper">
      <!-- resultType="User"，这里没有使用全限定类名，
        是因为在mybatis-config.xml中的typeAliases中定义了<package name="com.marshuo.entity"/> -->
      <select id="getUserById" resultType="User">
          select * from User where id = #{id}
      </select>
  </mapper>
  ```

### 2.6.2 添加mapper注册

- 修改mybatis-config.xml中的\<mappers>\</mappers>标签，将其【resource】属性修改为【绑定Dao】的mapper.xml文件所在路径。

  - 路径结构

    ‍
  - 自定义配置

    ```xml
    <!--如图所示配置-->
    <mappers>
        <mapper resource="mappers/usersMapper.xml"/>
    </mappers>
    ```

### 2.7 从SqlSessionFactory中获取

#### 2.7.1 官方声明

- 创建Dao测试类 => TestUserMapper ，测试mybatis

  - 方式一：

    ```java
    try (SqlSession session = sqlSessionFactory.openSession()) {
      BlogMapper mapper = session.getMapper(BlogMapper.class);
      Blog blog = mapper.selectBlog(101);
    }
    ```
  - 方式二 【旧版】：

    ```java
    //通过 SqlSession 实例来直接执行已映射的 SQL 语句。
    try (SqlSession session = sqlSessionFactory.openSession()) {
      Blog blog = (Blog) session.selectOne("org.mybatis.example.BlogMapper.selectBlog", 101);
    }
    ```

### 2.7.2 自定义配置

- 创建Dao测试类 => TestUserMapper ，测试mybatis

  - 方式一：

    ```java
    SqlSession sqlSession = MyBatisUtils.getSqlSession();

    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    User user = mapper.getUserById(1);
    ```
  - 方式二 【旧版】：

    ```java
    // 调用MyBatisUtils.getSqlSession()方法，获取SqlSession对象
    SqlSession sqlSession = MyBatisUtils.getSqlSession();

    // 使用全限定名映射SQL Mapper文件，并按照结果集类型来使用对应的方法接收返回结果集
    User user = sqlSession.selectOne("com.marshuo.mapper.UserMapper.getUserById", 1);
    ```
- 自定义配置

  ```java
  package com.marshuo;

  import com.marshuo.entity.User;
  import com.marshuo.mapper.UserMapper;
  import com.marshuo.utils.MyBatisUtils;
  import org.apache.ibatis.session.SqlSession;
  import org.junit.Test;

  import java.util.ArrayList;
  import java.util.List;

  /**
   * @author mars
   * @date 2022/09/20
   */
  public class TestUserMapper {
    
      @Test
      public void testGetUserById() {
          try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
              UserMapper mapper = sqlSession.getMapper(UserMapper.class);
              User user = mapper.getUserById(1);
              System.out.println("user = " + user);
          }
      }
    

  }

  ```

### 2.8 MyBatis总结

- SqlSessionFactoryBuilder  => 【实体类】
- SqlSessionFactory => 【接口】
- SqlSession => 【继承了Cloneable实体类】
- namespace => 【命名空间路径】

　　‍

## 3 CRUD

- 按目录结构完成【CRUD】的添加

　　‍

### 3.1 测试

1. 在Dao层中，添加 【update】 & 【delete】 & 【insert】的方法名

   ```java
   package com.marshuo.mapper;

   import com.marshuo.entity.User;

   import java.util.List;

   /**
    * @author mars
    * @date 2022/09/20
    */
   public interface UserMapper {
       User getUserById(int id);
     
       List<User> getUserList();
     
       int addUser(User user);
     
       int editUser(User user);
     
       int deleteUserById(int id);
   }
   ```
2. 在XML映射器中，添加【update】 & 【delete】 & 【insert】方法的绑定

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.marshuo.mapper.UserMapper">

       <select id="getUserById" resultType="com.marshuo.entity.User">
           select * from User where id = #{id}
       </select>

       <select id="getUserList" resultType="com.marshuo.entity.User">
           select * from User
       </select>

       <insert id="addUser" parameterType="com.marshuo.entity.User">
           insert into user (id, name, password) values (#{id}, #{name}, #{password})
       </insert>

       <update id="editUser" parameterType="com.marshuo.entity.User">
           update user set name=#{name}, password=#{password}
           where id = #{id}
       </update>

       <delete id="deleteUserById" >
           delete from user
           where id = #{id}
       </delete>
   </mapper>
   ```
3. 在测试类中，添加【update】 & 【delete】 & 【insert】对应的方法

   1. 添加@Test注解 => 开启单元测试
   2. 调用MyBatis工具类，创建SqlSessionFactoryBuilder => SqlSessionFactory => SqlSession
   3. 调用SqlSession对象中的commit()方法 => 提交事务
   4. 调用SqlSession对象中的shutdown()方法 => 释放资源

   ```java
   package com.marshuo;

   import com.marshuo.entity.User;
   import com.marshuo.mapper.UserMapper;
   import com.marshuo.utils.MyBatisUtils;
   import org.apache.ibatis.session.SqlSession;
   import org.junit.Test;

   import java.util.ArrayList;
   import java.util.List;

   /**
    * @author mars
    * @date 2022/09/20
    */
   public class UserMapperTest {
     
       @Test
       public void testGetUserById() {
           try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
               UserMapper mapper = sqlSession.getMapper(UserMapper.class);
               User user = mapper.getUserById(1);
               System.out.println("user = " + user);
           }
       }
     
       @Test
       public void testSelectUserList() {
           try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
               UserMapper mapper = sqlSession.getMapper(UserMapper.class);
               List<User> userList = mapper.getUserList();
               for (User user : userList) {
                   System.out.println("user = " + user);
               }
           }
       }
     
       @Test
       public void testAddUser() {
           try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
               UserMapper mapper = sqlSession.getMapper(UserMapper.class);
               User user = new User(5, "赵六", "123");
               mapper.addUser(user);
               // 提交事务，增删改都要提交事务，否则不生效
               sqlSession.commit();
           }
       }
     
       @Test
       public void testEditUser() {
           try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
               UserMapper mapper = sqlSession.getMapper(UserMapper.class);
               User user = new User(1, "张三斤", "abc");
               mapper.editUser(user);
               sqlSession.commit();
           }
       }
     
       @Test
       public void testDeleteUser() {
           try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
               UserMapper mapper = sqlSession.getMapper(UserMapper.class);
               int id = 3;
               mapper.deleteUserById(id);
               sqlSession.commit();
           }
       }

   }
   ```

## 4 批量插入

- 使用Map类型传递参数 => 【parameterType="map"】
  - 将【传递的参数】作为key，将【目标值】作为value
  - 调用put(\<key\>,\<value\>)方法

### 4.1 测试

### 4.1.1 批量插入

#### 4.1.1.1 官方声明

- List集合传参，XML映射器中使用\<foreach\>\<\foreach\>遍历List集合

  ‍

  - 参数解释：
    - foreach的主要作用在构建in条件中，它可以在SQL语句中进行迭代一个集合。foreach元素的属性主要有 collection，item，separator，index，open，close。
    - collection：指定要遍历的集合。表示传入过来的参数的数据类型。该属性是必须指定的，要做 foreach 的对象。在使用foreach的时候最关键的也是最容易出错的就是collection属性。在不同情况 下，该属性的值是不一样的，主要有一下3种情况：
      a. 如果传入的是单参数且参数类型是一个List的时候，collection属性值为list
      b. 如果传入的是单参数且参数类型是一个array数组的时候，collection的属性值为array
      c. 如果传入的参数是多个的时候，我们就需要把它们封装成一个Map了，当然单参数也可以封装成map。Map 对象没有默认的键
    - item：表示集合中每一个元素进行迭代时的别名。将当前遍历出的元素赋值给指定的变量，然后用#{变量名}，就能取出变量的值，也就是当前遍历出的元素。
    - separator：表示在每次进行迭代之间以什么符号作为分隔符。select * from tab where id in(1,2,3)相当于1,2,3之间的","
    - index：索引。index指定一个名字，用于表示在迭代过程中，每次迭代到的位置。遍历list的时候index就是索引，遍历map的时候index表示的就是map的key，item就是map的值。
    - open表示该语句以什么开始，close表示以什么结束。

#### 4.1.1.2 自定义配置

- Dao层 => 【UsersDao】

  ```java
  package com.marshuo.mapper;

  import com.marshuo.entity.User;

  import java.util.List;

  /**
   * @author mars
   * @date 2022/09/20
   */
  public interface UserMapper {

      int addUserList(List<User> userList);

  }

  ```
- XML映射器 => 【usersMapper.xml】

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.camemax.dao.UsersDao">

      <insert id="addUserList" >
          insert into user (id, name, password) values
          <!-- foreach 标签：
              -【item】属性： 表示集合中每一个元素进行迭代时的别名
              - 【collection】属性： 参数类型是一个List的时候，collection属性值为list
              - 【separator】属性： 表示在每次进行迭代之间以什么符号作为分隔符。  
          -->
          <foreach item="item" collection="list" separator=",">
              (#{item.id}, #{item.name}, #{item.password})
          </foreach>
      </insert>
  </mapper>
  ```
- 测试类 => 【UsersDaoTest.java】

  ```java
  package com.marshuo;

  import com.marshuo.entity.User;
  import com.marshuo.mapper.UserMapper;
  import com.marshuo.utils.MyBatisUtils;
  import org.apache.ibatis.session.SqlSession;
  import org.junit.Test;

  import java.util.ArrayList;
  import java.util.List;

  /**
   * @author mars
   * @date 2022/09/20
   */
  public class UserMapperTest {


      @Test
      public void testAddUserList() {
          try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
              UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            
              List<User> users = new ArrayList<>();
              users.add(new User(7, "钱七", "123"));
              users.add(new User(8, "孙八", "456"));
              users.add(new User(9, "李九", "abc"));
            
              int ret = mapper.addUserList(users);
              System.out.println("ret = " + ret);
              sqlSession.commit();
    
          }
      }
  }
  ```

## 5 模糊查找

- 实现MyBatis模糊查找
- 为了防止SQL注入，则在处理层传输带【%】的参数给XML映射器

### 5.1 测试

- Dao层 => 【UsersDao】

  ```java
  package com.marshuo.mapper;

  import com.marshuo.entity.User;

  import java.util.List;

  /**
   * @author mars
   * @date 2022/09/20
   */
  public interface UserMapper {
      List<User> getUserListByName(String name);
  }

  ```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.marshuo.mapper.UserMapper">
    <!-- 模糊查询 -->
    <select id="getUserListByName" resultType="com.marshuo.entity.User">
        select * from user where name like "%"#{name}"%"
    </select>
</mapper>
```

- 测试类 => 【UsersDaoTest】

  ```java
  package com.marshuo;

  import com.marshuo.entity.User;
  import com.marshuo.mapper.UserMapper;
  import com.marshuo.utils.MyBatisUtils;
  import org.apache.ibatis.session.SqlSession;
  import org.junit.Test;

  import java.util.ArrayList;
  import java.util.List;

  /**
   * @author mars
   * @date 2022/09/20
   */
  public class UserMapperTest {
      // 模糊查询
      @Test
      public void testGetUserListByName() {
          try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
              UserMapper mapper = sqlSession.getMapper(UserMapper.class);
              List<User> userList = mapper.getUserListByName("张");
    
              for (User user : userList) {
                  System.out.println("user = " + user);
              }
          }
      }
  }

  ```

## 6 [配置解析](https://mybatis.org/mybatis-3/zh/configuration.html)

- 面向核心配置文件（官方默认【mybatis-config.xml】）
- 核心配置文件结构：
  - configuration（配置）
    - [properties（属性）](https://mybatis.org/mybatis-3/zh/configuration.html#properties)
    - [settings（设置）](https://mybatis.org/mybatis-3/zh/configuration.html#settings)
    - [typeAliases（类型别名）](https://mybatis.org/mybatis-3/zh/configuration.html#typeAliases)
    - [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)
    - [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)
    - [plugins（插件）](https://mybatis.org/mybatis-3/zh/configuration.html#plugins)
    - [environments（环境配置）](https://mybatis.org/mybatis-3/zh/configuration.html#environments)
      - environment（环境变量）
        - transactionManager（事务管理器）
        - dataSource（数据源）
    - [databaseIdProvider（数据库厂商标识）](https://mybatis.org/mybatis-3/zh/configuration.html#databaseIdProvider)
    - [mappers（映射器）](https://mybatis.org/mybatis-3/zh/configuration.html#mappers)

### 6.1 环境配置（environment）

- 【官方声明】MyBatis 可以配置成适应多种环境，这种机制有助于将 SQL 映射应用于多种数据库之中
- **【官方声明】不过要记住：尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境。**

  - 通过核心配置文件中的\<environments\>标签中的【default】属性指定\<enviroment\>标签中的【id】属性，完成多环境下的单环境选择。
  - 默认环境和环境 ID 顾名思义。 环境可以随意命名，但务必保证默认的环境 ID 要匹配其中一个环境 ID。

  ‍

### 6.2 事务管理器（**transactionManager**）

- 【官方声明】：在 MyBatis 中有两种类型的事务管理器（也就是 type="[**JDBC|MANAGED**]"）：
  - **JDBC** – 这个配置直接使用了 JDBC 的提交和回滚设施，它依赖从数据源获得的连接来管理事务作用域。
  - **MANAGED** – 这个配置几乎没做什么。它从不提交或回滚一个连接，而是让容器来管理事务的整个生命周期（比如 JEE 应用服务器的上下文）。 默认情况下它会关闭连接。然而一些容器并不希望连接被关闭，因此需要将 closeConnection 属性设置为 false 来阻止默认的关闭行为。
- 【官方提示】：如果你正在使用 **Spring + MyBatis**，则没有必要配置事务管理器，因为 Spring 模块会使用自带的管理器来覆盖前面的配置。
- MyBatis默认事务管理器 => JDBC

### 6.3 数据源（DataSource）

- 【官方声明】：dataSource 元素使用标准的 JDBC 数据源接口来配置 JDBC 连接对象的资源。
- 【官方声明】：大多数 MyBatis 应用程序会按示例中的例子来配置数据源。虽然数据源配置是可选的，但如果要启用延迟加载特性，就必须配置数据源。
- 【官方声明】：有三种内建的数据源类型（也就是 type="[**UNPOOLED|POOLED|JNDI**]"）

  - **UNPOOLED**– 这个数据源的实现会每次请求时打开和关闭连接。虽然有点慢，但对那些数据库连接可用性要求不高的简单应用程序来说，是一个很好的选择。 性能表现则依赖于使用的数据库，对某些数据库来说，使用连接池并不重要，这个配置就很适合这种情形。
  - **POOLED**– 这种数据源的实现利用“池”的概念将 JDBC 连接对象组织起来，避免了创建新的连接实例时所必需的初始化和认证时间。 这种处理方式很流行，能使并发 Web 应用快速响应请求。
  - **JNDI** – 这个数据源实现是为了能在如 EJB 或应用服务器这类容器中使用，容器可以集中或在外部配置数据源，然后放置一个 JNDI 上下文的数据源引用。
- MyBatis默认数据源类型 => 【POOLED】
- 数据源类型： dbcp c3p0 druid hikari

### 6.4 属性（properties）

- 【官方声明】：属性可以在外部进行配置，并可以进行动态替换。

  - 外部.properties文件

    - 外部.properties文件 => 【db.properties】

      ```properties
      driver=com.mysql.cj.jdbc.Driver
      url=jdbc:mysql://localhost:3306/mybatis?useSSL=true&useUnicode=true&characterEncoding=utf-8
      ```
    - 核心配置文件 => 【mybatis-config.xml】

      ```xml
      <?xml version="1.0" encoding="UTF-8" ?>
      <!DOCTYPE configuration
              PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
              "https://mybatis.org/dtd/mybatis-3-config.dtd">
      <configuration>
          <properties resource="db.properties">
          </properties>

          <typeAliases>
              <!--<typeAlias type="com.marshuo.entity.User" alias="User"/>-->
              <package name="com.marshuo.entity"/>
          </typeAliases>

          <environments default="development">
              <environment id="development">
                  <transactionManager type="JDBC"/>
                  <dataSource type="POOLED">
                      <property name="driver" value="${driver}"/>
                      <property name="url"
                                value="${url}"/>
                      <property name="username" value="${username}"/>
                      <property name="password" value="${password}"/>
                  </dataSource>
              </environment>

              <environment id="test">
                  <transactionManager type="JDBC"/>
                  <dataSource type="POOLED">
                      <property name="driver" value="${driver}"/>
                      <property name="url"
                                value="${url}"/>
                      <property name="username" value="${user}"/>
                      <property name="password" value="${password}"/>
                  </dataSource>
              </environment>
          </environments>

          <mappers>
              <!--<mapper resource="com\marshuo\mapper\UserMapper.xml"/>-->
              <!-- 如果要使用package注意，比如mapper接口在 com.marshuo.mapper包下面，
              那么必须在resources下面建立和包名相同的目录 com\marshuo\mapper目录，然后 *mapper.xml -->
              <package name="com.marshuo.mapper"/>
          </mappers>
      </configuration>
      ```
  - 内部properties属性

    - 内部添加\<properties\>标签

      ```xml
      <?xml version="1.0" encoding="UTF-8" ?>
      <!DOCTYPE configuration
              PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
              "https://mybatis.org/dtd/mybatis-3-config.dtd">
      <configuration>
          <properties resource="db.properties">
              <property name="username" value="root"/>
              <property name="password" value="123456"/>
          </properties>

          <typeAliases>
              <!--<typeAlias type="com.marshuo.entity.User" alias="User"/>-->
              <package name="com.marshuo.entity"/>
          </typeAliases>

          <environments default="development">
              <environment id="development">
                  <transactionManager type="JDBC"/>
                  <dataSource type="POOLED">
                      <property name="driver" value="${driver}"/>
                      <property name="url"
                                value="${url}"/>
                      <property name="username" value="${username}"/>
                      <property name="password" value="${password}"/>
                  </dataSource>
              </environment>

              <environment id="test">
                  <transactionManager type="JDBC"/>
                  <dataSource type="POOLED">
                      <property name="driver" value="${driver}"/>
                      <property name="url"
                                value="${url}"/>
                      <property name="username" value="${user}"/>
                      <property name="password" value="${password}"/>
                  </dataSource>
              </environment>
          </environments>

          <mappers>
              <!--<mapper resource="com\marshuo\mapper\UserMapper.xml"/>-->
              <!-- 如果要使用package注意，比如mapper接口在 com.marshuo.mapper包下面，
              那么必须在resources下面建立和包名相同的目录 com\marshuo\mapper目录，然后 *mapper.xml -->
              <package name="com.marshuo.mapper"/>
          </mappers>
      </configuration>
      ```

## 6.5 设置（Settings）

- 【官方声明】：这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。
- 【官方声明】：一个配置完整的 settings 元素的示例如下：

  ```xml
  <settings>
    <setting name="cacheEnabled" value="true"/>
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="multipleResultSetsEnabled" value="true"/>
    <setting name="useColumnLabel" value="true"/>
    <setting name="useGeneratedKeys" value="false"/>
    <setting name="autoMappingBehavior" value="PARTIAL"/>
    <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
    <setting name="defaultExecutorType" value="SIMPLE"/>
    <setting name="defaultStatementTimeout" value="25"/>
    <setting name="defaultFetchSize" value="100"/>
    <setting name="safeRowBoundsEnabled" value="false"/>
    <setting name="mapUnderscoreToCamelCase" value="false"/>
    <setting name="localCacheScope" value="SESSION"/>
    <setting name="jdbcTypeForNull" value="OTHER"/>
    <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
  </settings>
  ```

### 6.5.1 类别名（typeAliases）

- 【官方声明】：类型别名可为 Java 类型设置一个缩写名字。 它仅用于 XML 配置，意在降低冗余的全限定类名书写。

  ```xml
  <!-- 官方示例 -->
  <typeAliases>
    <typeAlias alias="Author" type="domain.blog.Author"/>
    <typeAlias alias="Blog" type="domain.blog.Blog"/>
    <typeAlias alias="Comment" type="domain.blog.Comment"/>
    <typeAlias alias="Post" type="domain.blog.Post"/>
    <typeAlias alias="Section" type="domain.blog.Section"/>
    <typeAlias alias="Tag" type="domain.blog.Tag"/>
  </typeAliases>
  ```
- 支持两种形式 ： [ **@Alias** || **XML映射器**]

  1. 【官方示例】：【注解】@Alias

     ```java
     @Alias("author")
     public class Author {
         ...
     }
     ```
  2. 【官方示例】：XML映射器 => [ **包名** | **全限定名**]

     ```xml
     <!-- 包名 -->
     <typeAliases>
       <!--除非使用注解，否则不支持自定义别名-->
       <package name="com.marshuo.entity"/>
     </typeAliases>

     <!-- 类路径 -->
     <typeAliases>
         <!-- 支持自定义别名Alias -->
         <typeAlias type="com.marshuo.entity.User" alias="User"/>
     </typeAliases>
     ```
- 【官方声明】：常见的 Java 类型内建的类型别名。它们都是不区分大小写的，注意，为了应对原始类型的命名重复，采取了特殊的命名风格。

  |别名|映射的类型|
  | :---------| :---------|
  |_byte|byte|
  |_long|long|
  |_short|short|
  |_int|int|
  |_integer|int|
  |_double|double|
  |_float|float|
  |_boolean|boolean|
  |string|String|
  |byte|Byte|
  |long|Long|
  |short|Short|
  |int|Integer|
  |integer|Integer|
  |double|Double|
  |float|Float|
  |boolean|Boolean|
  |date|Date|
  |decimal|BigDecimal|
  |bigdecimal|BigDecimal|
  |object|Object|
  |map|Map|
  |hashmap|HashMap|
  |list|List|
  |arraylist|ArrayList|
  |collection|Collection|
  |iterator|Iterator|
- 别名测试：

  - 包名+类注解@Alias

    ‍
  - 自定义别名

    ‍

### 6.5.2 映射器(mapper)

- 定义SQL映射语句，指定MyBatis寻找SQL语句。
- 【官方声明】：指定映射文件路径

  1. 使用相对于类路径的资源引用 【推荐】：

     ```xml
     <mappers>
       <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
       <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
       <mapper resource="org/mybatis/builder/PostMapper.xml"/>
     </mappers>
     ```
  2. 使用映射器接口实现类的完全限定类名

     ```xml
     <mappers>
       <mapper class="org.mybatis.builder.AuthorMapper"/>
       <mapper class="org.mybatis.builder.BlogMapper"/>
       <mapper class="org.mybatis.builder.PostMapper"/>
     </mappers>
     ```
  3. 将包内的映射器接口实现全部注册为映射器

     注意：

     * 接口和xml文件必须同名
     * 接口的包名和xml文件所在目录名必须相同。

       比如：UserMapper接口放在com.marshuo.mappers包下面，那么UserMapper.xml就要放在resources的com/mars/mappers/目录下。

     ‍

     ```xml
     <mappers>
       <package name="org.mybatis.builder"/>
     </mappers>
     ```
  4. 使用完全限定资源定位符（URL） 【不推荐使用】

     ```xml
     <mappers>
       <mapper url="file:///var/mappers/AuthorMapper.xml"/>
       <mapper url="file:///var/mappers/BlogMapper.xml"/>
       <mapper url="file:///var/mappers/PostMapper.xml"/>
     </mappers>
     ```
- 测试

  1. 使用相对于类路径的资源引用：

     - 资源目录：
     - pom.xml中添加配置

       ```xml
       <!-- 过滤资源resource,使得resources路径能够被读取 -->
       <build>
           <resources>
               <resource>
                   <directory>src/main/resources</directory>
                   <includes>
                       <include>**/*.properties</include>
                       <include>**/*.xml</include>
                   </includes>
                   <filtering>true</filtering>
               </resource>
               <resource>
                   <directory>src/main/java</directory>
                   <includes>
                       <include>**/*.properties</include>
                       <include>**/*.xml</include>
                   </includes>
                   <filtering>true</filtering>
               </resource>
           </resources>
       </build>
       ```
     - mapper配置：

       ```xml
       <?xml version="1.0" encoding="UTF-8" ?>
       <!DOCTYPE configuration
               PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
               "http://mybatis.org/dtd/mybatis-3-config.dtd">
       <!-- configuration标签 => 声明MyBatis核心配置 -->
       <configuration>
           <typeAliases>
               <typeAlias type="com.camemax.pojo.Users" alias="users"/>
           </typeAliases>
           <!-- environments标签 => 设置MyBatis选用的环境信息 -->
           <environments default="development">
               <environment id="development">
                   <!-- transactionManager标签 => 事务管理 -->
                   <transactionManager type="JDBC"/>
                   <!-- dataSource标签 => 配置数据源属性 -->
                   <dataSource type="POOLED">
                       <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                       <property name="url" value="jdbc:mysql://localhost:3306/mybatis?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=CST"/>
                       <property name="username" value="root"/>
                       <property name="password" value="123"/>
                   </dataSource>
               </environment>
           </environments>
           <mappers>
               <mapper resource="mapper/UsersMapper.xml"/>
           </mappers>
       </configuration>
       ```
  2. 使用映射器接口实现类的完全限定类名

     - 资源目录：
     - mapper配置：

       ```xml
       <?xml version="1.0" encoding="UTF-8" ?>
       <!DOCTYPE configuration
               PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
               "http://mybatis.org/dtd/mybatis-3-config.dtd">
       <!-- configuration标签 => 声明MyBatis核心配置 -->
       <configuration>
           <typeAliases>
               <typeAlias type="com.camemax.pojo.Users" alias="users"/>
           </typeAliases>
           <!-- environments标签 => 设置MyBatis选用的环境信息 -->
           <environments default="development">
               <environment id="development">
                   <!-- transactionManager标签 => 事务管理 -->
                   <transactionManager type="JDBC"/>
                   <!-- dataSource标签 => 配置数据源属性 -->
                   <dataSource type="POOLED">
                       <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                       <property name="url" value="jdbc:mysql://localhost:3306/mybatis?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=CST"/>
                       <property name="username" value="root"/>
                       <property name="password" value="123"/>
                   </dataSource>
               </environment>
           </environments>
           <mappers>
       <!--        <mapper resource="mapper/UsersMapper.xml"/>-->
               <mapper class="com.camemax.dao.UsersMapper"/>
           </mappers>
       </configuration>
       ```
  3. 将包内的映射器接口实现全部注册为映射器

     - 资源目录：
     - mapper配置：

       ```xml
       <?xml version="1.0" encoding="UTF-8" ?>
       <!DOCTYPE configuration
               PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
               "http://mybatis.org/dtd/mybatis-3-config.dtd">
       <!-- configuration标签 => 声明MyBatis核心配置 -->
       <configuration>
           <typeAliases>
               <typeAlias type="com.camemax.pojo.Users" alias="users"/>
           </typeAliases>
           <!-- environments标签 => 设置MyBatis选用的环境信息 -->
           <environments default="development">
               <environment id="development">
                   <!-- transactionManager标签 => 事务管理 -->
                   <transactionManager type="JDBC"/>
                   <!-- dataSource标签 => 配置数据源属性 -->
                   <dataSource type="POOLED">
                       <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                       <property name="url" value="jdbc:mysql://localhost:3306/mybatis?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=CST"/>
                       <property name="username" value="root"/>
                       <property name="password" value="123"/>
                   </dataSource>
               </environment>
           </environments>
           <mappers>
       <!--        <mapper resource="mapper/UsersMapper.xml"/>-->
       <!--        <mapper class="com.camemax.dao.UsersMapper"/>-->
               <package name="com.camemax.dao"/>
           </mappers>
       </configuration>
       ```
- 结论

  - 【使用相对于类路径的资源引用】：
    1. 支持放在resources目录下，但需要解除maven中的resources资源限制。
    2. \<mapper>标签中的【resource】属性指向mapper映射器所在的相对路径，并使用【/】分割
  - 【使用映射器接口实现类的完全限定类名】与 【将包内的映射器接口实现全部注册为映射器】：都需要将映射器接口实现类（dao）与mapper映射器放在==同一个包内==且==文件名相同==

## 6.7 作用域和生命周期

- MyBatis中，作用域与生命周期主要针对：【SqlSessionFactoryBuilder】、【SqlSessionFactory】、【SqlSession】
- 【官方声明】

### 6.7.1 SqlSessionFactoryBuilder

- 【官方声明】

  > 这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域（也就是局部方法变量）。 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但最好还是不要一直保留着它，以保证所有的 XML 解析资源可以被释放给更重要的事情。
  >
- 作用域 => 【方法作用域（局部方法变量）】

  ### 6.7.2 SqlSessionFactory
- 【官方声明】

  > SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。因此 SqlSessionFactory 的最佳作用域是应用作用域。 有很多方法可以做到，最简单的就是使用单例模式或者静态单例模式。
  >
- 作用域 => 应用作用域
- 推荐使用【单例模式】或者【静态单例模式】
- 可以想象成 => 【数据库连接池】

　　6.7.3 SqlSession

- 【官方声明】

  > 每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。 也绝不能将 SqlSession 实例的引用放在任何类型的托管作用域中，比如 Servlet 框架中的 HttpSession。 如果你现在正在使用一种 Web 框架，考虑将 SqlSession 放在一个和 HTTP 请求相似的作用域中。 换句话说，每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它。 这个关闭操作很重要，为了确保每次都能执行关闭操作，你应该把这个关闭操作放到 finally 块中。
  >
- 作用域 => 【请求】或者【方法作用域（局部方法变量）】
- 一个web请求就可以开启一个SqlSession
- 及时释放资源，否则被占用
- 非线程安全，不能共享

## 7 [结果映射（resultMap）](https://mybatis.org/mybatis-3/zh/sqlmap-xml.html#Result_Maps)

- 【官方声明】

  > `resultMap` 元素是 MyBatis 中最重要最强大的元素。它可以让你从 90% 的 JDBC `ResultSets` 数据提取代码中解放出来，并在一些情形下允许你进行一些 JDBC 不支持的操作。实际上，在为一些比如连接的复杂语句编写映射代码的时候，一份 `resultMap` 能够代替实现同等功能的数千行代码。ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。
  >
- 【数据库字段】与【类属性字段】存在以下两种情况

  1. 命名相同：返回对应字段值
  2. 命名不相同：将导致查询不到指定字段值，返回'null'
- 解决【数据库字段】与【类属性字段】不相同

  1. SQL语句中实现字段别名

     - 【官方示例】

     ‍
  2. mapper映射器中\<resultMap>标签绑定

     - 【官方实例】

       ‍
     - 测试

       - 修改实体类【Users】

         ```java
         package com.camemax.pojo;

         //实体类Users
         public class Users {
             // 【id】 => 【userId】
             private int userId;
             // 【name】 => 【userName】
             private String userName;
             // 【password】 => 【userPasswd】
             private String userPasswd;

             public Users(){};

             public Users(int id, String name, String pwd) {
                 this.userId = id;
                 this.userName = name;
                 this.userPasswd = pwd;
             }

             public int getId() {
                 return userId;
             }

             public void setId(int id) {
                 this.userId = id;
             }

             public String getName() {
                 return userName;
             }

             public void setName(String name) {
                 this.userName = name;
             }

             public String getPwd() {
                 return userPasswd;
             }

             public void setPwd(String pwd) {
                 this.userPasswd = pwd;
             }

             @Override
             public String toString() {
                 return "Users{" +
                         "id=" + userId +
                         ", name='" + userName + '\'' +
                         ", pwd='" + userPasswd + '\'' +
                         '}';
             }
         }
         ```
       - 修改mapper映射器 【UsersMapper】

         ```xml
         <?xml version="1.0" encoding="UTF-8" ?>
         <!DOCTYPE mapper
                 PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                 "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
         <mapper namespace="com.camemax.dao.UsersMapper">

             <!-- 查询指定用户信息 -->
             <resultMap id="resultMapUser" type="users">
                 <!-- 类属性【userId】映射为数据库中的【id】字段 -->
                 <id property="userId" column="id"/>
                 <!-- 类属性【userName】映射为数据库中的【name】字段 -->
                 <result property="userName" column="name" />
                 <!-- 类属性【userPasswd】映射为数据库中的【password】字段 -->
                 <result property="userPasswd" column="password" />
             </resultMap>

             <!-- 【resultMap】属性指向<resultMap>标签 -->
             <select id="getUserInfoByUserId" resultType="users" parameterType="_int" resultMap="resultMapUser">
                 select * from mybatis.users
                 where id = #{id}
             </select>
         </mapper>
         ```

## 8 [日志](https://mybatis.org/mybatis-3/zh/logging.html)

## 8.1 日志工厂

- 【官方声明】

  > Mybatis 通过使用内置的日志工厂提供日志功能。内置日志工厂将会把日志工作委托给下面的实现之一：
  >
  > - SLF4J
  > - Apache Commons Logging
  > - Log4j 2
  > - Log4j
  > - JDK logging
  >
  > MyBatis 内置日志工厂会基于运行时检测信息选择日志委托实现。它会（按上面罗列的顺序）使用第一个查找到的实现。当没有找到这些实现时，将会禁用日志功能。
  >
  > 不少应用服务器（如 Tomcat 和 WebShpere）的类路径中已经包含 Commons Logging。注意，在这种配置环境下，MyBatis 会把 Commons Logging 作为日志工具。这就意味着在诸如 WebSphere 的环境中，由于提供了 Commons Logging 的私有实现，你的 Log4J 配置将被忽略。这个时候你就会感觉很郁闷：看起来 MyBatis 将你的 Log4J 配置忽略掉了（其实是因为在这种配置环境下，MyBatis 使用了 Commons Logging 作为日志实现）。如果你的应用部署在一个类路径已经包含 Commons Logging 的环境中，而你又想使用其它日志实现，你可以通过在 MyBatis 配置文件 mybatis-config.xml 里面添加一项 setting 来选择其它日志实现。
  >
- 可选参数：**SLF4J** | **LOG4J** | **LOG4J2** | **JDK_LOGGING** | **COMMONS_LOGGING** | **STDOUT_LOGGING** | **NO_LOGGING**

### 8.1.1 日志配置

　　注意：修改核心配置文件的时候，settings是有特定位置的，放在properties后面，typealias的前面，如果放的位置不正确，会有错误提示。

- 配置mapper映射器，添加\<settings> - \<setting>标签

  * 测试 => 添加标准日志工厂**STDOUT_LOGGING**

    ‍

### 常用的日志框架

　　常用的日志框架，主要是

* LOG4J 不推荐使用，太老了，被淘汰了

* LOGBACK 可选，公司老项目会有使用到。

* LOG4J2 比较新的日志框架，性能较好，目前推荐使用

　　‍

### 8.1.2 Log4J2

* ‍
* 【官方声明】：使用方式

  1. 导入Maven依赖

      ```xml
      <!-- log4j2 -->
      <dependency>
          <groupId>org.apache.logging.log4j</groupId>
          <artifactId>log4j-core</artifactId>
          <version>2.19.0</version>
      </dependency>
      <dependency>
          <groupId>org.apache.logging.log4j</groupId>
          <artifactId>log4j-api</artifactId>
          <version>2.19.0</version>
      </dependency>
      ```
  2. 映射器开启日志功能

      ```xml
      <configuration>
        <settings>
          ...
          <setting name="logImpl" value="LOG4J2"/>
          ...
        </settings>
      </configuration>
      ```
  3. log4j2.xml（比较全的配置文件）

      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <!--日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
      <!--Configuration后面的status，这个用于设置log4j2自身内部的信息输出，可以不设置，当设置成trace时，你会看到log4j2内部各种详细输出-->
      <!--monitorInterval：Log4j能够自动检测修改配置 文件和重新配置本身，设置间隔秒数-->
      <configuration status="WARN" monitorInterval="30">
          <!--先定义所有的appender-->
          <appenders>
              <!--这个输出控制台的配置-->
              <console name="Console" target="SYSTEM_OUT">
                  <!--输出日志的格式-->
                  <PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n"/>
              </console>
              <!--文件会打印出所有信息，这个log每次运行程序会自动清空，由append属性决定，这个也挺有用的，适合临时测试用-->
              <File name="log" fileName="log/test.log" append="false">
                  <PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n"/>
              </File>
              <!-- 这个会打印出所有的info及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->
              <RollingFile name="RollingFileInfo" fileName="${sys:user.home}/logs/info.log"
                           filePattern="${sys:user.home}/logs/$${date:yyyy-MM}/info-%d{yyyy-MM-dd}-%i.log">
                  <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
                  <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
                  <PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n"/>
                  <Policies>
                      <TimeBasedTriggeringPolicy/>
                      <SizeBasedTriggeringPolicy size="100 MB"/>
                  </Policies>
              </RollingFile>
              <RollingFile name="RollingFileWarn" fileName="${sys:user.home}/logs/warn.log"
                           filePattern="${sys:user.home}/logs/$${date:yyyy-MM}/warn-%d{yyyy-MM-dd}-%i.log">
                  <ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY"/>
                  <PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n"/>
                  <Policies>
                      <TimeBasedTriggeringPolicy/>
                      <SizeBasedTriggeringPolicy size="100 MB"/>
                  </Policies>
                  <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件，这里设置了20 -->
                  <DefaultRolloverStrategy max="20"/>
              </RollingFile>
              <RollingFile name="RollingFileError" fileName="${sys:user.home}/logs/error.log"
                           filePattern="${sys:user.home}/logs/$${date:yyyy-MM}/error-%d{yyyy-MM-dd}-%i.log">
                  <ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY"/>
                  <PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n"/>
                  <Policies>
                      <TimeBasedTriggeringPolicy/>
                      <SizeBasedTriggeringPolicy size="100 MB"/>
                  </Policies>
              </RollingFile>
          </appenders>
          <!--然后定义logger，只有定义了logger并引入的appender，appender才会生效-->
          <loggers>
              <!--过滤掉spring和mybatis的一些无用的DEBUG信息-->
              <logger name="org.springframework" level="INFO"></logger>
              <logger name="org.mybatis" level="INFO"></logger>
              <root level="all">
                  <appender-ref ref="Console"/>
                  <appender-ref ref="RollingFileInfo"/>
                  <appender-ref ref="RollingFileWarn"/>
                  <appender-ref ref="RollingFileError"/>
              </root>
          </loggers>
      </configuration>
      ```

      ‍

## 8.2 日志类使用

- 导入Apache-Log4J包

  ```java
  import org.apache.logging.log4j.LogManager;
  import org.apache.logging.log4j.Logger;
  ```
- 使用反射当前对象来创建当前Logger对象

  ```java
  // 创建静态变量Logger对象 => logger
  // 使用当前类.class反射创建logger对象
  static Logger logger = LogManager.getLogger(UsersDaoTest.class)
  ```
- 设置输出等级与输出信息

  ```java
  @Test
  public void log4jTest(){
      logger.info("info: 日志输出等级【Info】");
      logger.debug("debug: 日志输出等级【DEBUG】");
      logger.error("error: 日志输出等级【ERROR】");
  }
  ```
- 日志打印输出

  ‍

## 9 分页

- 减少单次获取到的数据量
- 实现方式

  - SQL语句 => limit

    ```sql
    // select * from mybatis.users limit <startIndex>,<returnSize>
    // limit x,-1 的bug已经被修复
    select * from mybatis.users limit 2,2
    ```

    - 测试

      1. 创建接口方法

         ```java
         List<Users> getUsersInfoByLimit();
         ```
      2. Mapper配置

         ```xml
         <resultMap id="getUsersInfoByLimit" type="MyBatisAliasUsers">
                 <id property="userId" column="id" />
                 <result property="userName" column="username" />
                 <result property="userPassword" column="password" />
                 <result property="userEmail" column="email" />
                 <result property="userGender" column="gender" />
             </resultMap>
             <!-- 使用map传入limit所需要的起始位置以及返回值 -->
             <select id="getUsersInfoByLimit" resultMap="getUsersInfoByLimit" parameterType="map">
                 select * from school.users limit #{startIndex},#{returnSize}
             </select>
         ```
      3. 测试类配置

         ```java
         @Test
         public void getUsersInfoByLimit(){

             MyBatisUtils myBatisUtils = new MyBatisUtils();
             SqlSession sqlSession = myBatisUtils.getSqlSession();
             UsersDao mapper = sqlSession.getMapper(UsersDao.class);

             // 指定HashMap传值给映射器Mapper
             // startIndex => 2
             // returnSize => 2
             HashMap<String,Integer> limitMap = new HashMap<String, Integer>();
             limitMap.put("startIndex",2);
             limitMap.put("returnSize",2);

             List<Users> users = mapper.getUsersInfoByLimit(limitMap);
             for (Users user : users) {
                 System.out.println(user);
             }

             sqlSession.close();
         }
         ```
      4. 日志输出返回结果
  - RowBounds 【不推荐】
  - PageHelper

## 10 注解

- 【官方声明】：可以使用Java注解进行配置

  - 接口注解配置

    ```java
    package org.mybatis.example;
    public interface BlogMapper {
      @Select("SELECT * FROM blog WHERE id = #{id}")
      Blog selectBlog(int id);
    }
    ```
- 测试

  1. dao层接口添加注解

     ```java
     @Select("select * from school.users")
     List<Users> getUsersInfoByAnnotation();
     ```
  2. 核心配置文件添加映射

     ```xml
     <!-- 核心配置文件绑定Dao层接口 -->
     <mappers>
         <mapper class="com.camemax.dao.UsersDao"/>
     </mappers>
     ```
  3. 测试类

     ```java
     @Test
     public void getUsersInfoByAnnotation(){
         MyBatisUtils myBatisUtils = new MyBatisUtils();
         SqlSession sqlSession = myBatisUtils.getSqlSession();
         UsersDao mapper = sqlSession.getMapper(UsersDao.class);

         List<Users> users = mapper.getUsersInfoByAnnotation();
         for (Users user : users) {
             System.out.println(user);
         }

         sqlSession.close();
     }
     ```
- 优缺点：

  - 优点：省去复杂的mapper映射器中的sql代码相关配置
  - 缺点：无法执行复杂的SQL，例如：存在字段异常不匹配时，使用注解执行SQL容易出现找不到值的情况（查询结果为'null'）

  ‍

## 11 MyBatis本质、底层与执行流程

- 本质：反射机制实现MyBatis三大类的创建
- 底层：使用动态代理接管dao层接口操作
- 执行流程：MyBatis工具类 => 【MyBatisUtils】，按照【官方使用步骤】：

  1. 获取核心配置文件【mybatis-config.xml】中的配置

     ```java
     try{
         // 指定配置文件路径
         String resource = "mybatis-config.xml";
         // 读取局部变量【resource】中的核心配置文件，并将其所有配置转化为input流
         // getResourceAsStream需要try...catch
         InputStream inputStream = Resources.getResourceAsStream(resource);
     }catch(Exception e){
         e.printStackTrace();
     }
     ```
  2. 实例化SqlSessionFactoryBuilder构造器

     ```java
     // 调用SqlSessionFactoryBuilder()类的build()方法创建SqlSessionFactory对象
     /*
         public class SqlSessionFactoryBuilder{
             ..
              SqlSessionFactory build(InputStream inputStream) {
         return build(inputStream, null, null);
       }
             ..
         }
     */
     SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
     ```
  3. 解析【1.获取核心配置文件中的配置】中配置好的文件解析流inputStream

     ```java
     // SqlSessionFactoryBuilder.build()重载
       public SqlSessionFactory build(InputStream inputStream) {
         return build(inputStream, null, null);
       }

     // SqlSessionFactoryBuilder.build()终会执行该build()方法：
     // XMLConfigBuilder.parse()完成对核心配置文件的解析
     public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
         try {
           XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
           return build(parser.parse());
         } catch (Exception e) {
           throw ExceptionFactory.wrapException("Error building SqlSession.", e);
         } finally {
           ErrorContext.instance().reset();
           try {
             reader.close();
           } catch (IOException e) {
             // Intentionally ignore. Prefer previous error.
           }
         }
       }
     ```
  4. 实例化一个【按1.配置核心配置文件】的DefaultSqlSessionFactory

     ```java
     // 使用Configuration类存放所有XML配置信息，并传递给SqlSessionFactory对象
     public SqlSessionFactory build(Configuration config) {
         return new DefaultSqlSessionFactory(config);
     }
     ```

     1. 创建SqlSession对象
  5. 创建executor执行器

     - delegate = (SimpleExecutor)
     - tcm = (TranscationalCacheManager)
       - transcationalCache
     - autoCommit = **true** | **false**
     - dirty = **true** | **false**
     - cursorList = **null**
  6. 完成CRUD操作
  7. 判断事务

     - 成功提交
     - 失败回滚executor

## 12 多对一查询

- SQL返回的值需要使用到类时的处理方式
- 模拟测试：多个学生对应一个老师

  1. MySQL测试表【Teachers】、【Students】
  2. 测试实体类【Teachers】、【Students】
  3. dao层【TeachersMapper】、【StudentsMapper】
  4. XML映射文件【teachersMapper.xml】、【studentsMapper.xml】
  5. 核心配置文件=>【mybatis-config.xml】绑定dao接口、注册XML映射文件
  6. 输出测试
- 整体目录结构

  ‍

### 12.1 环境搭建

#### 12.1.1 MySQL创建测试数据

```mysql

#教师表
DROP TABLE IF exists teachers;
create table teachers(
    `tid` int(10),
    `tname` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`tid`)
    )ENGINE=INNODB DEFAULT CHARSET=utf8;

#学生表
DROP TABLE IF exists students;
create table students(
    `id` int(10) ,
    `name` varchar(20) DEFAULT NULL,
    `tid` int(10) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teachers` (`tid`)  
    )ENGINE=INNODB DEFAULT CHARSET=utf8;

    insert into teachers (`tid`,`tname`) values (1,'小王老师');

    insert into students (`id`,`name`,`tid`) values (1,'小红',1);
    insert into students (`id`,`name`,`tid`) values (2,'小黄',1);
    insert into students (`id`,`name`,`tid`) values (3,'小黑',1);
    insert into students (`id`,`name`,`tid`) values (4,'小白',1);
    insert into students (`id`,`name`,`tid`) values (5,'小紫',1);
```

### 12.1.2 实体类与接口

- 学生相关

  - 【Students】实体类

    ```java
    package com.marshuo.entity;

    import lombok.Data;

    /**
     * @author mars
     * @date 2022/09/24
     */
    @Data
    public class Student {
        private int id;
        private String name;
        private Teacher teacher;
    }

    ```
  - 【StudentsMapper】接口

    ```java
    package com.marshuo.mapper;

    import com.marshuo.entity.Student;

    /**
     * @author mars
     * @date 2022/09/24
     */
    public interface StudentMapper {
        Student getStudentById(int id);
    }

    ```
- 教师相关

  - 【Teachers】实体类

    ```java
    package com.marshuo.entity;

    import lombok.Data;

    /**
     * @author mars
     * @date 2022/09/24
     */
    @Data
    public class Teacher {
        private int tid;
        private String tname;
    }
    ```
  - 【TeachersMapper】接口

    ```java
    package com.marshuo.mapper;

    import com.marshuo.entity.Teacher;

    /**
     * @author mars
     * @date 2022/09/24
     */
    public interface TeacherMapper {
        Teacher getTeacherById(int tid);
    }

    ```

### 12.1.3 Mapper映射器

- mybatis-config.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "https://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
      <properties resource="db.properties">
          <property name="username" value="root"/>
          <property name="password" value="123456"/>
      </properties>

      <settings>
          <setting name="logImpl" value="STDOUT_LOGGING"/>
      </settings>

      <typeAliases>
          <!--<typeAlias type="com.marshuo.entity.User" alias="User"/>-->
          <package name="com.marshuo.entity"/>
      </typeAliases>

      <environments default="development">
          <environment id="development">
              <transactionManager type="JDBC"/>
              <dataSource type="POOLED">
                  <property name="driver" value="${driver}"/>
                  <property name="url"
                            value="${url}"/>
                  <property name="username" value="${username}"/>
                  <property name="password" value="${password}"/>
              </dataSource>
          </environment>
      </environments>
    
      <mappers>
          <package name="com.marshuo.mapper"/>
      </mappers>
  </configuration>
  ```

### 12.2 按查询嵌套处理【子查询】

- StudentMapper.xml

  ```xml
      <!-- 方法1： 子查询 -->
      <resultMap id="StudentResultMap" type="Student">
          <id property="id" column="id"/>
          <result property="name" column="name"/>
          <!-- 复杂属性，对象使用association，集合使用collection -->
          <association property="teacher" column="tid"  javaType="Teacher" select="getTeacherById"/>
      </resultMap>

      <select id="getStudentById" resultMap="StudentResultMap">
          select * from students
          where id = #{id}
      </select>

      <select id="getTeacherById" resultType="Teacher">
          select * from teachers where tid = #{tid}
      </select>
  ```

　　‍

### 12.3 按结果嵌套处理【关联】

* StudentMapper.xml

  ```xml
      <resultMap id="StudentResultMap" type="Student">
          <id property="id" column="id"/>
          <result property="name" column="name"/>
          <association property="teacher" javaType="Teacher">
              <id property="tid" column="tid"/>
              <result property="tname" column="tname"/>
          </association>
      </resultMap>

      <select id="getStudentById" resultMap="StudentResultMap">
          select S.*, T.tname  from students S
          left join teachers T on S.tid = T.tid
          where id = #{id}
      </select>
  ```

  ‍

## 13 一对多查询

- 模拟测试：一名老师有多名学生 => 【面向教师】
- 本质：使用\<collection>标签完成一对多的输出

## 13.1 基于[12.1环境搭建](#12.1 环境搭建)做出的修改

1. dao层  => 【TeacherMapper】

   ```java
   package com.marshuo.mapper;

   import com.marshuo.entity.Teacher;

   /**
    * @author mars
    * @date 2022/09/24
    */
   public interface TeacherMapper {
       Teacher getTeacherById(int tid);
   }
   ```
2. 实现类 => 【Teachers】

   ```java
   package com.marshuo.entity;

   import lombok.Data;

   import java.util.List;

   /**
    * @author mars
    * @date 2022/09/24
    */
   @Data
   public class Teacher {
       private int tid;
       private String tname;
       private List<Student> studentList;
   }

   ```
3. 实体类 => 【Students】

   ```
   package com.marshuo.entity;

   import lombok.Data;

   /**
    * @author mars
    * @date 2022/09/24
    */
   @Data
   public class Student {
       private int id;
       private String name;
   }

   ```
4. 测试实现类 => 【TeacherMapperTest】

   ```java
       @Test
       public void testGetStudentById() {
           try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
               TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
               Teacher teacher = mapper.getTeacherById(1);
               System.out.println("teacher = " + teacher);
           }
       }
   ```

## 13.2 按查询嵌套处理 【子查询】

1. XML映射文件 => TeacherMapper.xml

   ```xml
       <!-- 子查询 -->
       <resultMap id="TeacherResultMap" type="Teacher">
           <id property="tid" column="tid"/>
           <result property="tname" column="tname"/>
           <collection property="studentList" ofType="Student" select="getStudents" column="tid">
               <id property="id" column="id"/>
               <result property="name" column="name"/>
           </collection>
       </resultMap>

       <select id="getTeacherById" resultMap="TeacherResultMap">
           select * from teachers
           where tid = #{tid}
       </select>

       <select id="getStudents" resultType="Student">
           select * from students
           where tid = #{tid}
       </select>
   ```
2. 输出结果

   ```java
   Opening JDBC Connection
   Created connection 1659367709.
   ==>  Preparing: select * from teachers where tid = ?
   ==> Parameters: 1(Integer)
   <==    Columns: tid, tname
   <==        Row: 1, 小王老师
   ====>  Preparing: select * from students where tid = ?
   ====> Parameters: 1(Integer)
   <====    Columns: id, name, tid
   <====        Row: 1, 小红, 1
   <====        Row: 2, 小黄, 1
   <====        Row: 3, 小黑, 1
   <====        Row: 4, 小白, 1
   <====        Row: 5, 小紫, 1
   <====      Total: 5
   <==      Total: 1
   teacher = Teacher(tid=1, tname=小王老师, studentList=[Student(id=1, name=小红), Student(id=2, name=小黄), Student(id=3, name=小黑), Student(id=4, name=小白), Student(id=5, name=小紫)])
   Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@62e7f11d]
   Returned connection 1659367709 to pool.
   ```

## 13.3 按结果嵌套处理 【关联查询】

1. XML映射文件 => TeacherMapper.xml

   ```xml
   <select id="getTeacherByIdHasStudents" resultMap="teacherGetStudentsByResult">
       select s.id studentId,s.name studentName,s.tid,t.tname teacherName,t.tid
       from students s,teachers t
       where s.tid = t.tid
       and t.tid = #{tid}
   </select>
   <resultMap id="teacherGetStudentsByResult" type="teachers">
       <id property="tid" column="tid"/>
       <result property="tname" column="teacherName"/>
       <collection property="teacherHasStudents" ofType="students">
           <id property="sid" column="studentId"/>
           <result property="sname" column="studentName"/>
           <result property="tid" column="tid" />
       </collection>
   </resultMap>
   ```
2. 测试结果

   ```java
   Opening JDBC Connection
   Created connection 1659367709.
   ==>  Preparing: select T.*, S.id, S.name from teachers T left join students S on T.tid = S.tid where T.tid = ?
   ==> Parameters: 1(Integer)
   <==    Columns: tid, tname, id, name
   <==        Row: 1, 小王老师, 1, 小红
   <==        Row: 1, 小王老师, 2, 小黄
   <==        Row: 1, 小王老师, 3, 小黑
   <==        Row: 1, 小王老师, 4, 小白
   <==        Row: 1, 小王老师, 5, 小紫
   <==      Total: 5
   teacher = Teacher(tid=1, tname=小王老师, studentList=[Student(id=1, name=小红), Student(id=2, name=小黄), Student(id=3, name=小黑), Student(id=4, name=小白), Student(id=5, name=小紫)])
   Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@62e7f11d]
   Returned connection 1659367709 to pool.
   ```

## 14.[动态SQL](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)

- 【官方声明】

  > 动态 SQL 是 MyBatis 的强大特性之一。如果你使用过 JDBC 或其它类似的框架，你应该能理解根据不同条件拼接 SQL 语句有多痛苦，例如拼接时要确保不能忘记添加必要的空格，还要注意去掉列表最后一个列名的逗号。利用动态 SQL，可以彻底摆脱这种痛苦。
  >
  > 使用动态 SQL 并非一件易事，但借助可用于任何 SQL 映射语句中的强大的动态 SQL 语言，MyBatis 显著地提升了这一特性的易用性。
  >
  > 如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。
  >

### 14.1 IF关键字

#### 14.1.1 环境搭建

1. MySQL建表 => 【blog】

    ```mysql
    drop table if exists blog;
    create table blog(
        `id` varchar(50) NOT NULL COMMENT '博客id',
        `title` varchar(100) NOT NULL COMMENT '博客标题',
        `author` varchar(30) NOT NULL COMMENT '博客作者',
        `create_time` datetime NOT NULL COMMENT '创建时间',
        `views` int(30) NOT NULL COMMENT '浏览量'
    )ENGINE=INNODB DEFAULT CHARSET=utf8;
    ```
2. 准备工作&&创建测试数据

    1. 创建实体类 => 【Blogs】

        ```java
        package com.marshuo.entity;

        import lombok.Data;

        import java.util.Date;

        /**
         * @author mars
         * @date 2022/09/24
         */
        @Data
        public class Blog {
            private int id;
            private String title;
            private String author;
            private Date createTime;
            private int views;
        }

        ```
    2. 创建dao层接口 => BlogsMapper

        ```java
        package com.marshuo.mapper;

        import com.marshuo.entity.Blog;

        import java.util.List;

        /**
         * @author mars
         * @date 2022/09/24
         */
        public interface BlogMapper {
            List<Blog> getBlogList();
            int addBlog(Blog blog);
        }

        ```
    3. XML映射文件 => 【BlogsMapper.xml】

        ```xml
        <?xml version="1.0" encoding="UTF-8" ?>
        <!DOCTYPE mapper
                PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
        <mapper namespace="com.marshuo.mapper.BlogMapper">
            <select id="getBlogList" resultType="Blog">
                select * from blog
            </select>

            <insert id="addBlog" parameterType="Blog">
                insert into blog values (#{id}, #{title}, #{author}, #{createTime}, #{views})
            </insert>
        </mapper>
        ```
    4. 核心配置文件 => 【mybatis-config.xml】

        ```xml
        <?xml version="1.0" encoding="UTF-8" ?>
        <!DOCTYPE configuration
                PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
                "https://mybatis.org/dtd/mybatis-3-config.dtd">
        <configuration>
            <properties resource="db.properties">
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </properties>

            <settings>
                <setting name="logImpl" value="STDOUT_LOGGING"/>
            </settings>

            <typeAliases>
                <!--<typeAlias type="com.marshuo.entity.User" alias="User"/>-->
                <package name="com.marshuo.entity"/>
            </typeAliases>

            <environments default="development">
                <environment id="development">
                    <transactionManager type="JDBC"/>
                    <dataSource type="POOLED">
                        <property name="driver" value="${driver}"/>
                        <property name="url"
                                  value="${url}"/>
                        <property name="username" value="${username}"/>
                        <property name="password" value="${password}"/>
                    </dataSource>
                </environment>
            </environments>
          
            <mappers>
                <package name="com.marshuo.mapper"/>
            </mappers>
        </configuration>
        ```
    5. JDBC => 【db.properties】

        ```properties
        driver=com.mysql.cj.jdbc.Driver
        url=jdbc:mysql://localhost:3306/mybatis?useSSL=true&useUnicode=true&characterEncoding=utf-8
        ```
    6. 使用UUID类实现唯一字段值

        ```java
        package com.camemax.utils;

        import java.util.UUID;

        public class UUIDUtils {
            public static String createUUID() {
                String createUUID = UUID.randomUUID().toString().replaceAll("-", "");
                return createUUID;
            }
        }
        ```
3. 测试实现类  => 【DaoTest】 ： 完成测试数据创建工作

    ```java
    package com.marshuo;

    import com.marshuo.entity.Blog;
    import com.marshuo.mapper.BlogMapper;
    import com.marshuo.utils.MyBatisUtils;
    import com.marshuo.utils.UUIDUtils;
    import org.apache.ibatis.session.SqlSession;
    import org.junit.Test;

    import java.util.Date;
    import java.util.List;

    /**
     * @author mars
     * @date 2022/09/20
     */
    public class BlogMapperTest {
        @Test
        public void testAddBlog() {
            try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
                BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
                mapper.addBlog(new Blog(UUIDUtils.getId(), "这是一个博客", "marshuo", new Date(), 15));
      
            }
        }
      
        @Test
        public void testGetBlogList() {
            try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
                BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
                List<Blog> blogList = mapper.getBlogList();
                for (Blog blog : blogList) {
                    System.out.println("blog = " + blog);
                }
            }
        }
    }

    ```

#### 14.1.2 IF语句

1. 创建dao层接口 => BlogsMapper

    ```java
    public interface BlogMapper {
        List<Blog> getBlogsByDynamicSQL(Blog blog);
    }
    ```

2. XML映射文件 => 【BlogsMapper.xml】

   ```xml
   <select id="getBlogsByDynamicSQL">
       <!-- 注意： where 1 = 1 尽量不使用 -->
       select * from test.blog where 1 = 1
       <!-- <if>标签： 当向数据库发送的请求中，【title】字段不为空时，则添加【title】字段的查询过滤条件 -->
       <if test="title != null">
           and title = #{title}
       </if>
       <!-- <if>标签： 当向数据库发送的请求中，【author】字段不为空时，添加【author】字段的查询过滤条件 -->
       <if test="author != null">
           and author = #{author}
       </if>
   </select>
   ```
3. 测试实现类 => 【DaoTest】

   ```java
   @Test
   public void getBlogsByDynamicSQL(){

       MyBatisUtils mybatis = new MyBatisUtils();
       SqlSession sqlSession = mybatis.getSqlSession();
       BlogsMapper mapper = sqlSession.getMapper(BlogsMapper.class);

       List<Blogs> blogs = mapper.getBlogsByDynaticSQL();
       for (Blogs blog : blogs){
           System.out.print(blog);
       }

       sqlSession.close();
   }
   ```
4. 测试结果

   - 不加筛选条件，即传参的HashMap为null

     ‍
   - 添加筛选条件

     ‍

### 14.2 where、set

- 【官方实例】

  - 失败案例

    - 前面几个例子已经合宜地解决了一个臭名昭著的动态 SQL 问题。现在回到之前的 “if” 示例，设置成动态条件，看看会发生什么。

      ```xml
      <select id="getBlogsByIF" parameterType="Blog" resultType="Blog">
          select * from blog
          where
          <if test=" author != null ">
              author = #{author}
          </if>
          <if test=" title != null ">
              and title = #{title}
          </if>
      </select>
      ```
    - 如果没有匹配的条件会怎么样？最终这条 SQL 会变成这样：

      ```sql
      SELECT * FROM BLOG
      WHERE
      ```
    - 这会导致查询失败。如果匹配的只是第二个条件又会怎样？这条 SQL 会是这样:

      ```sql
      SELECT * FROM BLOG
      WHERE
      AND title title ‘someTitle’
      ```
- 成功案例

  - 这个查询也会失败。这个问题不能简单地用条件元素来解决。这个问题是如此的难以解决，以至于解决过的人不会再想碰到这种问题。MyBatis 有一个简单且适合大多数场景的解决办法。而在其他场景中，可以对其进行自定义以符合需求。而这，只需要一处简单的改动：

    ```xml
    <select id="getBlogsByIF" parameterType="Blog" resultType="Blog">
        select * from blog
        <where>
            <if test=" author != null ">
                author = #{author}
            </if>
            <if test=" title != null ">
                and title = #{title}
            </if>
        </where>
    </select>
    ```
- 【官方声明】

  - \<where>标签：

    - *where* 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 元素也会将它们去除。
  - \<trim>标签：

    - 如果 *where* 元素与你期望的不太一样，你也可以通过自定义 trim 元素来定制 *where* 元素的功能。比如，和 *where* 元素等价的自定义 trim 元素为：

      ```xml
      <trim prefix="WHERE" prefixOverrides="AND |OR ">
        <!-- 【prefixOverrides】属性：忽略通过管道符分隔的文本序列（注意此例中的空格是必要的）。会移除所有 prefixOverrides 属性中指定的内容，并且插入 prefix 属性中指定的内容。-->
      </trim>
      ```
  - \<set>标签：

    - 用于动态更新语句的类似解决方案叫做 *set*。*set* 元素可以用于动态包含需要更新的列，忽略其它不更新的列。

      ```xml
      <update id="updateAuthorIfNecessary">
        update Author
          <set>
            <if test="username != null">username=#{username},</if>
            <if test="password != null">password=#{password},</if>
            <if test="email != null">email=#{email},</if>
            <if test="bio != null">bio=#{bio}</if>
          </set>
        where id=#{id}
      </update>
      ```

#### 14.2.1 测试

1. Dao层接口添加实现方法 => 【BlogsMapper】

   ```java
       int updateBlog(Blog blog);
   ```
2. XML映射文件 => 【BlogsMapper.xml】

   ```xml
   <update id="updateBlog" parameterType="Blog" >
       update blog
       <set>
           <if test="title != null ">
               title = #{title},
           </if>
           <if test="author != null ">
               author = #{author},
           </if>
       </set>
       where id = #{id}
   </update>
   ```
3. 测试实现类 => 【BlogMapperTest】

   ```java
   @Test
   public void testUpdateBlog() {
       try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
           BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

           Blog blog = new Blog();
           blog.setId("24e9f0cf72194edf99b1abcf37c30a25");
           // blog.setTitle("呵呵哈哈");
           // mapper.updateBlog(blog);
         
           blog.setTitle("摇摇晃晃");
           blog.setAuthor("布尔");
           mapper.updateBlog(blog);
       }
   }
   ```
4. 测试结果

   1. 日志输出

      ‍
   2. 数据库验证

      - 原数据库数据

        ‍
      - 现数据库数据

        ‍

### 14.3 choose、when、otherwise

- 【官方声明】：有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。

  - 例子

    ```xml
    <select id="findActiveBlogLike"
         resultType="Blog">
      SELECT * FROM BLOG WHERE state = ‘ACTIVE’
      <choose>
        <when test="title != null">
          AND title like #{title}
        </when>
        <when test="author != null and author.name != null">
          AND author_name like #{author.name}
        </when>
        <otherwise>
          AND featured = 1
        </otherwise>
      </choose>
    </select>
    ```

#### 14.3.1 测试

##### 14.3.1.1 环境搭建

1. dao层接口添加方法 => 【BlogsMapper】

   ```java
   List<Blog> getBlogsByChoose(Blog blog);
   ```
2. xml映射文件 => 【BlogsMapper.xml】

   ```xml
   <select id="getBlogsByChoose" parameterType="Blog" resultType="Blog">
       select * from blog
       <where>
           <!-- choose when 相当于java的 switch case -->
           <choose>
               <when test=" author != null ">
                   author = #{author}
               </when>
               <when test=" title != null ">
                   title = #{title}
               </when>
               <otherwise>
               </otherwise>
           </choose>
       </where>
   </select>
   ```
3. 测试实现类 => 【BlogMapperTest】 (按测试情况进行配置 )

##### 14.3.1.2 测试结果

* 测试实现类 => 【BlogMapperTest】

  ```java
      @Test
      public void testGetBlogsByChoose() {
          try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
              BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
              Blog blog = new Blog();
              // 没有添加条件，查询到所有的记录
              List<Blog> blogList = mapper.getBlogsByChoose(blog);
              for (Blog blog1 : blogList) {
                  System.out.println("blog1 = " + blog1);
              }
    
              // 根据title进行查询
              blog.setTitle("Java很简单");
              blogList = mapper.getBlogsByChoose(blog);
              for (Blog blog1 : blogList) {
                  System.out.println("blog1 = " + blog1);
              }
    
              // 根据author进行查询，这个时候title条件不生效
              blog.setAuthor("marshuo");
              blogList = mapper.getBlogsByChoose(blog);
              for (Blog blog1 : blogList) {
                  System.out.println("blog1 = " + blog1);
              }
            
            
          }
      }
  ```

  * 输出结果  1行记录：返回条件成立的结果集，不执行\<otherwise>标签

    ​
* 结论

  > \<choose>标签会在多个条件都满足的情况下，仅会返回第一个传参的返回值。
  >
  > 但当其他条件都不满足时，可以添加\<otherwise>标签，用于返回一个固有值。
  >

### 14.4 Trim

- Trim可以自定义SQL语句中的规范，当\<where>标签、\<set>标签不满足时，可以使用Trim自定义。

#### 14.4.1 Trim自定义测试

- 使用Trim复写\<where>、\<set>规则

##### 14.4.1.1 \<trim>实现\<where>

1. Dao层接口 => 【BlogsMapper】

   ```java
   List<Blogs> queryBlogsByTrim(Map<String,String> map)
   ```
2. XML映射器 => 【BlogsMapper.xml】

   ```xml
   <select id="queryBlogsByTrim" parameterType="blogs">
       select * from test.blog
       <trim prefix="WHERE" prefixOverride="AND |OR ">
           <if test="titleMap != null"> AND title = #{titleMap}</if>
           <if test="authorMap != null"> OR author = #{authorMap}</if>
       </trim>
   </select>
   ```
3. 测试实现类 => 【BlogMapperTest】

   ```java
   @Test
   public void dynamicSqlSelectByTrim(){
       MyBatisUtils mybatis = new MyBatisUtils;
       SqlSession sqlSession = mybatis.getSqlSession();
       BlogsMapper mapper = sqlSession.getMapper(BlogsMapper.class);

       HashMap<String,String> map = new HashMap<String,String>();
       map.put("titleMap","MyBatis");
       map.put("authorMap","Altria");

       for (Blogs blog : mapper.queryBlogsByTrim(map)) {
           System.out.println(blog);
       }

       sqlSession.close();
   }
   ```
4. 输出结果

   ​

##### 14.4.1.2 \<trim>实现\<set>

1. Dao层接口 => 【BlogsMapper】

    ```java
    int updateBlogInfoByTrim(Map<String,String> map)
    ```
2. XML映射文件 => 【BlogsMapper.xml】

    ```xml
    <update id="updateBlogInfoByTrim" parameterType="map">
        update test.blog
        <trim prefix="SET" suffixOverride=",">
            <if test="titleMap != null"> title = #{titleMap},</if>
            <if test="authorMap != null"> author = #{authorMap},</if>
        </trim>
        where id = #{idMap}
    </update>
    ```
3. 测试实现类 => 【BlogMapperTest】

    ```java
    @Test
    public void dynamicSqlUpdateByTrim(){

        MyBatisUtils mybatis = new MyBatisUtils();
        SqlSession sqlSession = mybatis.getSqlSession();
        BlogsMapper mapper = mybatis.getMapper(BlogsMapper.class);

        Map<String,String> map = new HashMap<String,String>();
        map.put("authorMap","Altria");
        map.put("titleMap","Spring Framework Updated");
        map.put("idMap","5aa45402bc764755b3ae406be6b27d33");

        int i = mapper.updateBlogInfoByTrim(map);
        if( i > 0 ){
            System.out.println("Update Succeed!");
        }
    }
    ```
4. 测试结果

　　‍

### 14.5 SQL片段

#### 14.5.1 什么是SQL片段

　　就是一些可以共用的SQL语句，我们把他提取出来，就是SQL片段。

　　我们以之前IF语句为例：

###### 改造之前的语句： 【BlogMapper.xml】

```xml
<select id="getBlogsByIF" parameterType="Blog" resultType="Blog">
    select * from blog
    <where>
        <if test=" author != null ">
            author = #{author}
        </if>
        <if test=" title != null ">
            and title = #{title}
        </if>
    </where>
</select>
```

##### 改造之后的语句：

```xml
<!-- sql片段 -->
<sql id="where-author-title">
    <where>
        <if test=" author != null ">
            author = #{author}
        </if>
        <if test=" title != null ">
            and title = #{title}
        </if>
    </where>
</sql>

<!-- 引用SQL片段 -->
<select id="getBlogsByIF" parameterType="Blog" resultType="Blog">
    select * from blog
    <include refid="where-author-title"/>
</select>
```

> 结论：
>
> 1. 使用<sql>标签定义SQL片段，id 属性做为一个唯一标识。
> 2. 使用<include refid = ""/>引用sql片段，refid属性标识所引用的sql片段id

　　‍

### 14.6 for each

1. Dao层接口 => 【BlogsMapper】

    ```java
    List<Blog> getBolgsByForEach(List<String> titles);
    ```
2. XML映射文件 => 【BlogsMapper.xml】

    ```xml
        <select id="getBolgsByForEach" parameterType="list" resultType="Blog">
            select * from blog
            <where>
                <foreach collection="list" item="item" index="index"
                         open=" title in (" separator="," close=")">
                    #{item}
                </foreach>
            </where>
        </select>

    ```
3. 测试实现类 => 【BlogMapperTest】

```xml
@Test
public void testGetBlogsByForEach() {
    try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        List<String> list = new ArrayList<>();
        list.add("这是一个博客");
        list.add("摇摇晃晃");

        List<Blog> blogs = mapper.getBolgsByForEach(list);
        for (Blog blog : blogs) {
            System.out.println("blog = " + blog);
        }
    }
}

```

　　‍

## 15.缓存

- 【官方声明】

  > MyBatis 内置了一个强大的事务性查询缓存机制，它可以非常方便地配置和定制。 为了使它更加强大而且易于配置，我们对 MyBatis 3 中的缓存实现进行了许多改进。
  >
- 一级缓存（SqlSession级别）
- 二级缓存（mapper||namespace级别）

### 15.1 一级缓存

#### 15.1.1 什么是一级缓存？

> Mybatis对缓存提供支持，但是在没有配置的默认情况下，它只开启一级缓存，一级缓存只是相对于同一个SqlSession而言。

#### 15.1.2 一级缓存的作用

> 在参数和SQL完全一样的情况下，我们使用同一个SqlSession对象调用一个Mapper方法，往往只执行一次SQL，因为使用SelSession第一次查询后，MyBatis会将其放在缓存中，以后再查询的时候，如果没有声明需要刷新，并且缓存没有超时的情况下，SqlSession都会取出当前缓存的数据，而不会再次发送SQL到数据库。

##### 15.1.3 **一级缓存的生命周期有多长？**

1. MyBatis在开启一个数据库会话时，会 创建一个新的SqlSession对象，SqlSession对象中会有一个新的Executor对象。Executor对象中持有一个新的PerpetualCache对象；当会话结束时，SqlSession对象及其内部的Executor对象还有PerpetualCache对象也一并释放掉。
2. 如果SqlSession调用了close()方法，会释放掉一级缓存PerpetualCache对象，一级缓存将不可用。
3. 如果SqlSession调用了clearCache()，会清空PerpetualCache对象中的数据，但是该对象仍可使用。
4. SqlSession中执行了任何一个update操作(update()、delete()、insert()) ，都会清空PerpetualCache对象的数据，但是该对象可以继续使用

#### 15.1.4 如何判断缓存目标？

1. 传入的statementId
2. 查询时要求的结果集中的结果范围
3. 这次查询所产生的最终要传递给JDBC java.sql.Preparedstatement的Sql语句字符串（boundSql.getSql() ）
4. 传递给java.sql.Statement要设置的参数值

##### 15.1.5 测试

1. 实体类【User】

    ```java
    package com.marshuo.entity;

    import lombok.Data;

    /**
     * @author mars
     * @date 2022/09/25
     */
    @Data
    public class User {
        private int id;
        private String name;
        private String password;
    }

    ```
2. Dao层接口添加测试方法 => 【UserMapper】

    ```xml
    User getUserById(int id);
    ```
3. XML映射文件 => 【BlogsMapper.xml】

    ```xml
    <select id="getUserById" resultType="User">
        select * from user where id = #{id}
    </select>
    ```
4. 测试实现类【UserMapperTest】

    1. 正常操作

        ```java
        @Test
        public void testGetUserById() {
            try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                User user = mapper.getUserById(1);
                System.out.println("user = " + user);

                User user1 = mapper.getUserById(1);
                System.out.println("user1 = " + user1);

                System.out.println("(user == user1) = " + (user == user1));
            }
        }
        ```
5. 日志输出

    1. 正常输出，第二次查询结果是从缓存拿到的。

        ```java
        Opening JDBC Connection
        Created connection 680712932.
        ==>  Preparing: select * from user where id = ?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, password
        <==        Row: 1, 张三斤, abc
        <==      Total: 1
        user = User(id=1, name=张三斤, password=abc)
        user1 = User(id=1, name=张三斤, password=abc)
        (user == user1) = true
        Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@2892dae4]
        Returned connection 680712932 to pool.

        Process finished with exit code 0

        ```

        ‍
6. 缓存失效的情况

    * 查询的条件不同

      * 测试代码

        ```java
            // 两次查询不同的用户，查看是否有缓存
            @Test
            public void testGetUserById2() {
                try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
                    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                    User user = mapper.getUserById(1);
                    System.out.println("user = " + user);
                
                    User user1 = mapper.getUserById(2);
                    System.out.println("user1 = " + user1);
                
                    System.out.println("(user == user1) = " + (user == user1));
                }
            }
        ```
      * 输出日志

        ```java
        Opening JDBC Connection
        Created connection 81412691.
        ==>  Preparing: select * from user where id = ?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, password
        <==        Row: 1, 张三斤, abc
        <==      Total: 1
        user = User(id=1, name=张三斤, password=abc)
        ==>  Preparing: select * from user where id = ?
        ==> Parameters: 2(Integer)
        <==    Columns: id, name, password
        <==        Row: 2, 李四, 123456
        <==      Total: 1
        user1 = User(id=2, name=李四, password=123456)
        (user == user1) = false
        Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@4da4253]
        Returned connection 81412691 to pool.

        Process finished with exit code 0

        ```
    * 增删改操作，会导致缓存刷新

      * 测试代码

        ```java
            // 第二次查询之前，去更新另一个用户的信息
            @Test
            public void testGetUserById3() {
                try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
                    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                    User user = mapper.getUserById(1);
                    System.out.println("user = " + user);
          
                    mapper.updateUserById(new User(9, "李大海", "aacc"));
                  
                    User user1 = mapper.getUserById(1);
                    System.out.println("user1 = " + user1);
              
                    System.out.println("(user == user1) = " + (user == user1));
                }
            }
        ```
      * 输出日志

        ```java
        Opening JDBC Connection
        Created connection 81412691.
        ==>  Preparing: select * from user where id = ?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, password
        <==        Row: 1, 张三斤, abc
        <==      Total: 1
        user = User(id=1, name=张三斤, password=abc)
        ==>  Preparing: update user SET name = ?, password = ? where id = ?
        ==> Parameters: 李大海(String), aacc(String), 9(Integer)
        <==    Updates: 1
        ==>  Preparing: select * from user where id = ?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, password
        <==        Row: 1, 张三斤, abc
        <==      Total: 1
        user1 = User(id=1, name=张三斤, password=abc)
        (user == user1) = false
        Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@4da4253]
        Returned connection 81412691 to pool.

        ```

      ‍

    * 不同的Mapper
    * 手动清理缓存

      * 测试代码

        ```java
            // 手动清理缓存
            @Test
            public void testGetUserById4() {
                try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
                    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                    User user = mapper.getUserById(1);
                    System.out.println("user = " + user);
              
                    // 清理缓存
                    sqlSession.clearCache();
              
                    User user1 = mapper.getUserById(1);
                    System.out.println("user1 = " + user1);
              
                    System.out.println("(user == user1) = " + (user == user1));
                }
            }
        ```
      * 输出日志

        ```java
        Opening JDBC Connection
        Created connection 81412691.
        ==>  Preparing: select * from user where id = ?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, password
        <==        Row: 1, 张三斤, abc
        <==      Total: 1
        user = User(id=1, name=张三斤, password=abc)
        ==>  Preparing: select * from user where id = ?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, password
        <==        Row: 1, 张三斤, abc
        <==      Total: 1
        user1 = User(id=1, name=张三斤, password=abc)
        (user == user1) = false
        Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@4da4253]
        Returned connection 81412691 to pool.

        ```

> 总结：
>
> 一级缓存默认开启，只在一次sqlSession中有效。一级缓存就是个Map。

　　‍

### 15.2 二级缓存

#### 15.2.1 什么是二级缓存？

> 二级缓存也叫全局缓存，一级缓存作用域太低了，所以诞生了二级缓存。
>
> MyBatis的二级缓存是Application级别的缓存，它可以提高对数据库查询的效率，以提高应用的性能。
>
> SqlSessionFactory层面上的二级缓存默认是不开启的，二级缓存的开席需要进行配置，实现二级缓存的时候，MyBatis要求==返回的POJO必须是可序列化==的（ 要求实现Serializable接口）

#### 15.2.2 二级缓存的作用

> * 映射语句文件中的所有select语句将会被缓存。
> * 映射语句文件中的所有insert、update和delete语句会刷新缓存。
> * 缓存会使用默认的Least Recently Used（LRU，最近最少使用的）算法来收回。
> * 根据时间表，比如No Flush Interval,（CNFI没有刷新间隔），缓存不会以任何时间顺序来刷新。
> * 缓存会存储列表集合或对象(无论查询方法返回什么)的1024个引用
> * 缓存会被视为是read/write(可读/可写)的缓存，意味着对象检索不是共享的，而且可以安全的被调用者修改，不干扰其他调用者或线程所做的潜在修改。
> * **提示** 缓存只作用于 cache 标签所在的映射文件中的语句。如果你混合使用 Java API 和 XML 映射文件，在共用接口中的语句将不会被默认缓存。你需要使用 @CacheNamespaceRef 注解指定缓存作用域。

#### 15.2.3 二级缓存的工作机制

* 一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中；
* 如果当前会话关闭了，这个会话对应的一级缓存就没了；但是我们想要的是，会话关闭了，一级缓存的数据被保存到二级缓存中；新的会话查询信息，可以从二级缓存中获取数据。
* 不同Mapper查询出的数据放在各自对应的缓存（map）中；

　　‍

#### 15.2.4 如何开启二级缓存

> 默认情况下，只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。 要启用全局的二级缓存，只需要在你的 SQL 映射文件中添加一行：

* 在mybatis的核心配置文件中，有一个配置项cacheEnabled，是用来控制二级缓存的开启，它的默认值是true。

  ```xml
  <settings>
      <!-- 显示打开全局缓存 -->
      <setting name="cacheEnabled" value="true"/>
  </settings>
  ```
* 在XML映射文件中添加以下代码，以开启【二级缓存】

  ```xml
  <cache/>
  ```

* 【官方声明】 => \<cache>标签的属性修改

  ```xml
  <cache
    eviction="FIFO"
    flushInterval="60000"
    size="512"
    readOnly="true"/>
  ```

  > 这个更高级的配置创建了一个 FIFO 缓存，每隔 60 秒刷新，最多可以存储结果对象或列表的 512 个引用，而且返回的对象被认为是只读的，因此对它们进行修改可能会在不同线程中的调用者产生冲突。
  >
  > 可用的清除策略有：
  >
  > * `LRU` – 最近最少使用：移除最长时间不被使用的对象。
  > * `FIFO` – 先进先出：按对象进入缓存的顺序来移除它们。
  > * `SOFT` – 软引用：基于垃圾回收器状态和软引用规则移除对象。
  > * `WEAK` – 弱引用：更积极地基于垃圾收集器状态和弱引用规则移除对象。
  >
  > 默认的清除策略是 LRU。
  >
  > flushInterval（刷新间隔）属性可以被设置为任意的正整数，设置的值应该是一个以毫秒为单位的合理时间量。 默认情况是不设置，也就是没有刷新间隔，缓存仅仅会在调用语句时刷新。
  >
  > size（引用数目）属性可以被设置为任意正整数，要注意欲缓存对象的大小和运行环境中可用的内存资源。默认值是 1024。
  >
  > readOnly（只读）属性可以被设置为 true 或 false。只读的缓存会给所有调用者返回缓存对象的相同实例。 因此这些对象不能被修改。这就提供了可观的性能提升。而可读写的缓存会（通过序列化）返回缓存对象的拷贝。 速度上会慢一些，但是更安全，因此默认值是 false。
  >

　　‍

### 15.2.3 测试

1. 实现类 => 【实现Serializable接口】

    ```java
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class User implements Serializable {
        private static final long serialVersionUID = 5608706612069775975L;
      
        private int id;
        private String name;
        private String password;
    }
    ```
2. XML配置文件 => 【开启二级缓存】

    * 默认配置

      ```xml
      <cache/>
      ```
    * 自定义配置

      ```xml
      <cache
            eviction="FIFO" 
            flushInterval="3000"
            size="512"
            readOnly="true"/>
      ```
3. 测试实现

    开启两个sqlSession查询同一个用户信息

    * 测试代码 （UserMapperTest）

      ```xml
          // 开启两个sqlSession，看是否缓存
          @Test
          public void testGetUserById() {
              try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
                  UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                  User user = mapper.getUserById(1);
                  System.out.println("user = " + user);
              }

              // 开启第二个sqlSession，看是否缓存
              try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
                  UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                  User user = mapper.getUserById(1);
                  System.out.println("user = " + user);
              }
            
          }
      ```
    * 输出日志

      ```xml
      Cache Hit Ratio [com.marshuo.mapper.UserMapper]: 0.0
      Opening JDBC Connection
      Created connection 1804379080.
      ==>  Preparing: select * from user where id = ?
      ==> Parameters: 1(Integer)
      <==    Columns: id, name, password
      <==        Row: 1, 张三斤, abc
      <==      Total: 1
      user = User(id=1, name=张三斤, password=abc)
      Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@6b8ca3c8]
      Returned connection 1804379080 to pool.
      As you are using functionality that deserializes object streams, it is recommended to define the JEP-290 serial filter. Please refer to https://docs.oracle.com/pls/topic/lookup?ctx=javase15&id=GUID-8296D8E8-2B93-4B9A-856E-0A65AF9B8C66
      Cache Hit Ratio [com.marshuo.mapper.UserMapper]: 0.5
      user = User(id=1, name=张三斤, password=abc)

      ```

    > 小结：
    >
    > * 二级缓存，在同一个mapper下有效
    > * 所有数据都会先放在一级缓存；当会话提交或者会话关闭，才会提交到二级缓存
    >

　　‍

　　‍
