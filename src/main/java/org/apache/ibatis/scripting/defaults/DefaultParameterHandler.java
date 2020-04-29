/**
 *    Copyright 2009-2019 the original author or authors.
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
package org.apache.ibatis.scripting.defaults;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public class DefaultParameterHandler implements ParameterHandler {

  //  TypeHandlerRegistry 对象，管理 MyBat 工 s 中的 全部 TypeHandler 对象
  private final TypeHandlerRegistry typeHandlerRegistry;
  //MappedSt a tement 对象，其中记录 SQL 节点相应的配置信息
  private final MappedStatement mappedStatement;
  private final Object parameterObject;
  //   对应的 BoundSql 对象，需妥设置参数的 PreparedStatement 对象 ， 就是根据该 BoundSql 中 记录的 SQL
//  语句创建的， BoundSql 中也记录了对应参数的名称和相关属性
  private final BoundSql boundSql;
  private final Configuration configuration;

  public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
    this.mappedStatement = mappedStatement;
    this.configuration = mappedStatement.getConfiguration();
    this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
    this.parameterObject = parameterObject;
    this.boundSql = boundSql;
  }

  @Override
  public Object getParameterObject() {
    return parameterObject;
  }

  /**
   * 为 SQL 语句绑定实参
   * @param ps
   */
  @Override
  public void setParameters(PreparedStatement ps) {
    ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());

//     TypeHandlerRegistry 对象，管理 MyBat 工 s 中的 全部 TypeHandler 对象
    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
//    检测 p a rameterMapping s 集合是否为空（略）
    if (parameterMappings != null) {
      for (int i = 0; i < parameterMappings.size(); i++) {
        ParameterMapping parameterMapping = parameterMappings.get(i);
//        ／／过滤掉存储过程中的输出参数
        if (parameterMapping.getMode() != ParameterMode.OUT) {
//          记录绑定的实参
          Object value;
//           ／／获取参数名称
          String propertyName = parameterMapping.getProperty();
          if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
//            ／／获取对应的实参位
            value = boundSql.getAdditionalParameter(propertyName);
          } else if (parameterObject == null) {
//            整个实参为空
            value = null;
          } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
//            实参可以直接通过 TypeHandler 转换成 JdbcType
            value = parameterObject;
          } else {
//            获取对象中相应的属性位或查找 Map 对象中位
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            value = metaObject.getValue(propertyName);
          }
//          ／／获取 ParameterMapping 中设置的 TypeHandler 对象
          TypeHandler typeHandler = parameterMapping.getTypeHandler();
          JdbcType jdbcType = parameterMapping.getJdbcType();
          if (value == null && jdbcType == null) {
            jdbcType = configuration.getJdbcTypeForNull();
          }
          try {
          //通过 TypeHandler . setParametera （）方法会调用 PreparedStatement.set*()方法
          //为 SQL 语句绑定相应的实参，读者可以回顾第 2 章 TypeHandler 小节的内容
            typeHandler.setParameter(ps, i + 1, value, jdbcType);
          } catch (TypeException | SQLException e) {
            throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
          }
        }
      }
    }
  }

}
