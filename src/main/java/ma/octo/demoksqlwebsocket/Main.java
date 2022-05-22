package ma.octo.demoksqlwebsocket;

import ma.octo.demoksqlwebsocket.vo.UserVo;

import java.lang.reflect.Field;

public class Main {
  public static void main(String[] args) {
    Field[] declaredFields = UserVo.class.getDeclaredFields();
    for (Field declaredField : declaredFields) {
      System.out.println("Field  Name " +   declaredField.getName());
      System.out.println("Field TYpe " + declaredField.getType());
    }
  }
}

