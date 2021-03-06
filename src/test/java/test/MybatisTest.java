package test; /**
 * Copyright 2009-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//import com.github.pagehelper.Page;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis源码分析
 */
public class MybatisTest {


  /**
   * p批量删除
   */
  @Test
  public void testDeletBatch() throws Exception {
    InputStream inputStream = Resources.getResourceAsStream("test/mybatis-config.xml");
    SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = builder.openSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<User> idList = new ArrayList<>();
    for (User user : mapper.selectUserList()) {
      idList.add(user);
    }
    mapper.deleteUserAll(idList);

  }

  /**
   * 测试插入
   */
  @Test
  public void testInsert() throws Exception {
    InputStream inputStream = Resources.getResourceAsStream("test/mybatis-config.xml");

    SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = builder.openSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    User user2 = new User();
    user2.setUsername("战三");
    user2.setAge(20);
//    插入操作
    mapper.insertUser(user2);
    sqlSession.commit();
    sqlSession.close();
  }


  /**
   * 测试批量插入
   */
  @Test
  public void testBatch() throws Exception {
    InputStream inputStream = Resources.getResourceAsStream("test/mybatis-config.xml");

    SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = builder.openSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<User> userList = new ArrayList<>();
    User user2;
    for (int i = 0; i < 10; i++) {
//      使用自增主键
      user2 = new User();
      user2.setUsername("战三" + i);
      user2.setAge(20 + i);
      userList.add(user2);
    }
//    插入操作
    mapper.inserUserAll(userList);
    sqlSession.commit();
    sqlSession.close();
  }


  /***
   * 传统方式
   */
  @Test
  public void test1() throws Exception {

    // 1. 加载配置文件
    InputStream inputStream = Resources.getResourceAsStream("test/mybatis-config.xml");

    // 创建SqlSessionFactory 初始化 解析mybatis-config.xml、UserMappper.xml配置文件，创建Configguretion对象
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    // 返回DefaultSqlSession对象，创建Executor对象 处理拦截器
    SqlSession sqlSession = sessionFactory.openSession();
    //使用jdk动态代理创建代理对象
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

//   ################分页查询#########################
    PageHelper.startPage(1, 2);
//    #########################################


    // 预编译sql，处理参数，执行查询，处理结果集，插件处理
    // StatementType类型
    //1、STATEMENT:直接操作sql，不进行预编译，获取数据：$—Statement
    //2、PREPARED:预处理，参数，进行预编译，获取数据：#—–PreparedStatement:默认
    //3、CALLABLE:执行存储过程————CallableStatement
    List<User> list = mapper.selectUserList();
    for (User o : list) {
      System.out.println(o);
    }
    PageInfo<User> pageInfo = new PageInfo<User>(list);
    System.out.println("总条数: " + pageInfo.getTotal());
    System.out.println("总页数: " + pageInfo.getPages());
    System.out.println("当前页: " + pageInfo.getPageNum());
    System.out.println("每页显示长度: " + pageInfo.getPageSize());
    System.out.println("是否第一页: " + pageInfo.isIsFirstPage());
    System.out.println("是否最后一页: " + pageInfo.isIsLastPage());
  }
}
