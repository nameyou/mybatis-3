package test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class CacheTest {


  @Test
  public void test2() throws Exception {
    Integer i = 100;
    System.out.println(i);

  }

  /**
   * mybatis源码分析
   *
   * @throws Exception
   */
  @Test
  public void test1() throws Exception {
    // 1.加载配置文件
    InputStream inputStream = Resources.getResourceAsStream("test/mybatis-config.xml");
    // 2. 创建SqlSessionFactory对象实际创建的是DefaultSqlSessionFactory对象
    SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(inputStream);
    // 3. 创建SqlSession对象实际创建的是DefaultSqlSession对象
    SqlSession sqlSession = builder.openSession();
    // 4. 创建代理对象
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    // 5. 执行查询语句
    List<User> users = mapper.selectUserList();
    for (User user : users) {
      System.out.println(user);
    }
  }


  /**
   * 测试一级缓存
   */
  @Test
  public void testCacheOne() throws Exception {
    InputStream inputStream = Resources.getResourceAsStream("test/mybatis-config.xml");
    SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(inputStream);
    // 第一次获取sqlsession对象
    SqlSession sqlSession = builder.openSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    System.out.println("第一次查询....");
    User user = mapper.selectUserById(1);
    System.out.println(user);
    // 修改数据
    user.setUsername("哈哈1");
    user.setAge(12);
    int i = mapper.updateUserByUserId(user);
    sqlSession.commit();


    // 第二次查询
    System.out.println("第二次查询....");
    UserMapper mapper1 = sqlSession.getMapper(UserMapper.class);
    User user1 = mapper1.selectUserById(1);
    System.out.println(user1);
    sqlSession.close();
  }


  /**
   * 使用二级缓存实体类要实现序列化接口
   *
   * @throws Exception
   */
  @Test
  public void testCache2() throws Exception {
    InputStream inputStream = Resources.getResourceAsStream("test/mybatis-config.xml");
    SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = build.openSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    System.out.println("===============第一次查询===================");
    User user = mapper.selectUserById(1);
    sqlSession.close();
    System.out.println(user);
    // 第二次查询
    System.out.println("===============第二次查询===================");
    SqlSession sqlSession2 = build.openSession();
    UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
    User user2 = mapper2.selectUserById(1);
    System.out.println(user2);

    System.out.println("===============更新数据===================");
    user.setUsername("嘿嘿");
    user.setAge(24);
    mapper2.updateUserByUserId(user);
//    sqlSession2.commit();
    sqlSession2.close();
    // 第三次查询
    System.out.println("===============第三次查询==================");
    SqlSession sqlSession3 = build.openSession();
    UserMapper mapper1 = sqlSession3.getMapper(UserMapper.class);
    User user1 = mapper1.selectUserById(1);
    System.out.println(user1);
    sqlSession3.close();
  }
}
