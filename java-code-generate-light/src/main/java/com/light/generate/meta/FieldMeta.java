package com.light.generate.meta;

public class FieldMeta {

  private final Class<?> type;

  private final String name;

  private final int modifier;

  public FieldMeta(Class<?> type, String name, int modifier) {
    this.type = type;
    this.name = name;
    this.modifier = modifier;
  }

  public Class<?> getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public int getModifier() {
    return modifier;
  }

  // TODO 初始值


  public static void main(String[] args) {
    System.out.println(String.class.getName());
    System.out.println(byte.class.getName());
    System.out.println(byte.class.toString());
    System.out.println(Object[].class.toString());
    System.out.println(Object[].class.getCanonicalName());
    System.out.println(Object[].class.getCanonicalName());
  }
}
