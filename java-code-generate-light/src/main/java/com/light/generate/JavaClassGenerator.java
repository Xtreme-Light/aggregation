package com.light.generate;

import com.light.generate.meta.ClassMeta;
import com.light.generate.meta.FieldMeta;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * 使用javaassit 动态加载代码。
 */
public class JavaClassGenerator {

  private static final ClassPool CLASS_POOL = ClassPool.getDefault();

  /**
   * plain pojo 生成器
   */
  public static Class<?> generatePlainPojo(ClassMeta classMeta)
      throws NotFoundException, CannotCompileException {

    final CtClass ctClass = buildCtlClass(classMeta);

//    ctClass.writeFile();

    return ctClass.toClass();
  }

  private static CtClass buildCtlClass(ClassMeta classMeta)
      throws NotFoundException, CannotCompileException {
    final CtClass ctClass = CLASS_POOL.makeClass(classMeta.getFullyQualifiedName());

    List<CtClass> $temp = new ArrayList<>();
    StringBuilder constructBody = new StringBuilder();
    int i = 1;
    for (FieldMeta fieldMeta : classMeta.getFieldMetaList()) {
      final CtClass type = CLASS_POOL.get(fieldMeta.getType().getCanonicalName());
      $temp.add(type);
      final CtField ctField = new CtField(type,
          fieldMeta.getName(), ctClass);
      ctField.setModifiers(Modifier.PRIVATE);
      constructBody.append(StringConst.THIS_POINT)
          .append(fieldMeta.getName())
          .append(StringConst.EQUAL)
          .append(StringConst.REFERENCE).append(i++)
          .append(StringConst.SEMICOLON);
      ctClass.addField(ctField);
      final String captureName = captureName(fieldMeta.getName());
      ctClass.addMethod(CtNewMethod.setter(StringConst.SET + captureName, ctField));
      ctClass.addMethod(CtNewMethod.getter(StringConst.GET + captureName, ctField));
    }

    final CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
    ctConstructor.setBody(StringConst.EMPTY_BODY);
    ctClass.addConstructor(ctConstructor);

    final CtConstructor ctConstructor1 = new CtConstructor($temp.toArray(new CtClass[0]), ctClass);

    ctConstructor1.setBody(constructBody.toString());

    ctClass.addConstructor(ctConstructor1);
    return ctClass;
  }


  private static String captureName(String name) {
    final char[] chars = name.toCharArray();
    chars[0] -= 32;
    return String.valueOf(chars);
  }


  /**
   * TODO 暂时不支持内部类
   */
  @SafeVarargs
  public static Class<?> populate(ClassMeta classMeta, Class<?> interfaceClass,
      Supplier<String>... suppliers)
      throws NotFoundException, CannotCompileException {
    final Method[] declaredMethods = interfaceClass.getDeclaredMethods();
    if (declaredMethods.length != suppliers.length) {
      throw new RuntimeException("不匹配的长度");
    }
    final CtClass ctClass = buildCtlClass(classMeta);
    ctClass.setInterfaces(new CtClass[]{CLASS_POOL.getCtClass(interfaceClass.getCanonicalName())});
    for (int i = 0; i < declaredMethods.length; i++) {
      final Method declaredMethod = declaredMethods[i];
      final Parameter[] parameters = declaredMethod.getParameters();

      List<CtClass> parameterList = new ArrayList<>();
      for (Parameter parameter : parameters) {
        final Class<?> type = parameter.getType();
        final CtClass $parameter = CLASS_POOL.get(type.getCanonicalName());
        parameterList.add($parameter);
      }
      final Class<?>[] exceptionTypes = declaredMethod.getExceptionTypes();
      List<CtClass> exceptionTypeList = new ArrayList<>();

      for (Class<?> exceptionType : exceptionTypes) {
        final CtClass $exception = CLASS_POOL.get(exceptionType.getCanonicalName());
        exceptionTypeList.add($exception);
      }
      final Supplier<String> supplier = suppliers[i];
      ctClass.addMethod(
          CtNewMethod.make(CLASS_POOL.get(declaredMethod.getReturnType().getCanonicalName()),
              declaredMethod.getName(), parameterList.toArray(new CtClass[0]),
              exceptionTypeList.toArray(new CtClass[0]), supplier.get(), ctClass));

    }
    return ctClass.toClass();
  }
}
