/**
 *    Copyright 2009-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package test;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * 自定义插件 实现Interceptor接口
 * intercept（）: 拦截目标对象，目标对象执行前做前置处理
 *
 */

@Intercepts({ // 配置拦截器
  @Signature(
    type = StatementHandler.class, // 拦截的类
    method = "prepare", // 拦截的方法
    args = {Connection.class, Integer.class}) // 方法的参数，参数要按顺序写
})

public class MyPlugins implements Interceptor {
  /**
   * 拦截目标对象，目标对象执行前做前置处理
   * Invocation 主要用来存储目标类，方法以及方法参数
   * @param invocation
   * @return
   * @throws Throwable
   */
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    System.out.println("我是来做前置增强的....，哈哈哈.......");
    // 执行目标方法
    return invocation.proceed();
  }

  /**
   *  c创建代理对象
   * @param target
   * @return
   */
  @Override
  public Object plugin(Object target) {
    System.out.println("将目标对象包装成代理对象....");
    // 包装成代理对象，并返回;使用的jdk的动态代理
    return Plugin.wrap(target,this);
  }


  /**
   * 插件所需的参数
   * 获取配置文件的参数，插件初始化的时候，只加载一次
   * @param properties
   */
  @Override
  public void setProperties(Properties properties) {
    System.out.println(properties.get("name"));
  }
}
