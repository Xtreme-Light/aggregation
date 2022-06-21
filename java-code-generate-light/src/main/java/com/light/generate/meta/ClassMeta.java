package com.light.generate.meta;

import com.light.generate.StringConst;
import java.util.List;

public class ClassMeta {

  private final String className;

  private final String packageName;

  private final List<FieldMeta> fieldMetaList;

  private final String fullyQualifiedName;

  public String getClassName() {
    return className;
  }

  public String getFullyQualifiedName() {
    return fullyQualifiedName;
  }

  private ClassMeta(String className, String packageName, List<FieldMeta> fieldMetaList) {
    this.className = className;
    this.packageName = packageName;
    this.fieldMetaList = fieldMetaList;
    this.fullyQualifiedName = packageName + StringConst.DOT + className;
  }

  public String getPackageName() {
    return packageName;
  }


  public List<FieldMeta> getFieldMetaList() {
    return fieldMetaList;
  }


  public static class Builder {

    private String className;

    private String packageName;

    private List<FieldMeta> fieldMetaList;

    public Builder() {
    }

    public Builder className(String className) {
      this.className = className;
      return this;
    }

    public Builder packageName(String packageName) {
      this.packageName = packageName;
      return this;
    }

    public Builder fieldMetaList(List<FieldMeta> fieldMetaList) {
      this.fieldMetaList = fieldMetaList;
      return this;
    }

    public ClassMeta build() {

      return new ClassMeta(className, packageName, fieldMetaList);
    }

  }
}
