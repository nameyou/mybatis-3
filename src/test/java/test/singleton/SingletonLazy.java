package test.singleton;

/**
 * 单例模式之懒汉式
 */
public class SingletonLazy {

  // 懒汉式 需要对象的时候再去创建
  private static final SingletonLazy instance = null;


  // 私有化话构造器
  private SingletonLazy() {
  }

  /**
   * 对外暴露的获取单例对象的方法，
   * 存在多线程并发不安全的问题，可以给通过加锁synchronized关键字 锁会影响性能
   *
   * @return
   */
  public SingletonLazy getInstance() {
    // 判断是否已经创建了对象，没有就创建，有，就直接返回
    if (instance == null) {
      return new SingletonLazy();
    }
    return instance;
  }

}
