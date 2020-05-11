package test.valuepassing;

import java.util.ArrayList;
import java.util.List;

/**
 * java中值传递问题
 */
public class JavaValuePassing {


  public static void main(String[] args) {
    List<String> a = new ArrayList<>();
    // 改变值
    changeValue(a);
    System.out.println(a.toString());

    changeValue2(a);
    System.out.println(a.toString());
  }

  private static void changeValue2(List<String> a) {
    a.add("sss");
  }

  private static void changeValue(List<String> a) {
    a = new ArrayList<>();
    a.add("sw");
  }
}
