package com.light.generate;

import com.light.generate.meta.ClassMeta;
import com.light.generate.meta.ClassMeta.Builder;
import com.light.generate.meta.FieldMeta;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.Test;

public class JavaClassGeneratorTest {


  @Test
  public void testGeneratePlainPojo()
      throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {

    final FieldMeta fieldMeta = new FieldMeta(String.class, "name", Modifier.PRIVATE);
    final Builder builder = new Builder();
    final ClassMeta temp = builder.className("Temp")
        .packageName("com.light.generate.meta")
        .fieldMetaList(Collections.singletonList(fieldMeta))
        .build();

    final Class<?> aClass = JavaClassGenerator.generatePlainPojo(temp);
    assert aClass.getCanonicalName().equals("com.light.generate.meta.Temp");
    final Object o = aClass.newInstance();
    final Method setName = aClass.getDeclaredMethod("setName", String.class);
    setName.invoke(o, "tom");

    final Method getName = aClass.getDeclaredMethod("getName");
    final String invoke = (String) getName.invoke(o);

    assert "tom".equals(invoke);

  }

  @Test
  public void testInterface()
      throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    final FieldMeta fieldMeta = new FieldMeta(String.class, "name", Modifier.PRIVATE);
    final Builder builder = new Builder();
    final ClassMeta temp = builder.className("Temp")
        .packageName("com.light.generate.meta")
        .fieldMetaList(Collections.singletonList(fieldMeta))
        .build();

    final Class<?> populate = JavaClassGenerator.populate(temp, Echo.class,
        () -> "{System.out.println(\"hello\");return \"hello\";}");
    final Object o = populate.newInstance();
    final Method say = populate.getDeclaredMethod("say");
    final String invoke = (String) say.invoke(o);
    assert invoke.equals("hello");

  }


}
