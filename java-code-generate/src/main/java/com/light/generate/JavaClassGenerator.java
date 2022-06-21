package com.light.generate;

import groovy.lang.GroovyClassLoader;
import groovy.text.StreamingTemplateEngine;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JavaClassGenerator {

  private static final StreamingTemplateEngine ENGINE = new StreamingTemplateEngine();

  private JavaClassGenerator() {

  }

  /**
   * 使用StreamingTemplateEngine引擎来生成对应的内容，支持超过64K大小的map数据
   *
   * @param template groovyTemplate模板
   * @param map      变量
   */
  public static String generate(String template, Map<?, ?> map)
      throws IOException, ClassNotFoundException {
    final File file = new File(template);
    return ENGINE
        .createTemplate(file)
        .make(map).toString();
  }

  /**
   * 静态加载类，要求对应的java代码必须有无参构造器
   */
  public static Object generateClass(String javaCodeString)
      throws IOException, InstantiationException, IllegalAccessException {
    try (final GroovyClassLoader groovyClassLoader = new GroovyClassLoader();) {
      final Class<?> aClass = groovyClassLoader.parseClass(javaCodeString);
      return aClass.newInstance();
    }
  }

}
