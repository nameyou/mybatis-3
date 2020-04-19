package test.singleton;

/**
 * 静态内部类写法
 *  推荐写法
 */
public class Singleton {


  private Singleton() {
  }

  /**
   * 静态内部类 只在getInstance方法内使用
   */
  private static class SingletonHolder {
    public static Singleton instance = new Singleton();
  }

  // 返回接口
  public static Singleton getInstance() {
    return SingletonHolder.instance;
  }

}
