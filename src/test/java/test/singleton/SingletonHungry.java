package test.singleton;


/**
 * 代理模式之饿汉式
 */
public class SingletonHungry {

  // 在类加载的时候，就创建对象
  private final static SingletonHungry instance = new SingletonHungry();

  /**
   * 私有化构造器，防止外界创建对象F
   */
  private SingletonHungry() {

  }

  // 提供外界获取对象的方法
  public static SingletonHungry getInstance() {
    // 返回单利对象
    return instance;
  }
}
