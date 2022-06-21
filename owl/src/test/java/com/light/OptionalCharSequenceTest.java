package com.light;

import com.light.owl.OptionalCharSequence;
import com.light.owl.exceptions.BlankCharSequenceException;
import com.light.owl.exceptions.EmptyCharSequenceException;
import org.junit.Test;

public class OptionalCharSequenceTest {

  @Test
  public void test() {
    final OptionalCharSequence of = OptionalCharSequence.of("");
    assert of.isPresent();
    assert of.isBlank();
    assert of.isEmpty();
  }

  @Test
  public void test2() {
    final OptionalCharSequence of = OptionalCharSequence.ofNullable(null);
    assert !of.isPresent();
    assert of.isBlank();
    assert of.isEmpty();
  }

  @Test
  public void test3() {
    final OptionalCharSequence of = OptionalCharSequence.ofNullable(" ");
    assert of.isPresent();
    assert !of.isEmpty();
    assert of.isBlank();
  }

  @Test
  public void test4() {
    final OptionalCharSequence of = OptionalCharSequence.ofNullable("");
    final CharSequence charSequence = of.notEmptyOrElse("123");
    assert charSequence.equals("123");
  }

  @Test
  public void test5() {
    final OptionalCharSequence of = OptionalCharSequence.ofNullable("   ");
    final CharSequence charSequence = of.notBlankOrElse("123");
    assert charSequence.equals("123");
  }

  @Test
  public void test6() {
    final OptionalCharSequence of = OptionalCharSequence.ofNullable("123");
    final OptionalCharSequence filter = of.filter(v -> v.equals("123"));
    assert filter.isPresent();
    assert filter.get().equals("123");

  }

  @Test
  public void test7() {
    final OptionalCharSequence of = OptionalCharSequence.ofNullable("");
    final OptionalCharSequence filter = of.notEmptyFilter(v -> v.equals("123"));
    assert filter.isEmpty();
    assert filter.isPresent();
    assert filter.isBlank();

  }

  @Test
  public void test8() {
    final OptionalCharSequence of = OptionalCharSequence.ofNullable("   ");
    final OptionalCharSequence filter = of.notBlankFilter(v -> v.equals("123"));
    assert !filter.isEmpty();
    assert filter.isPresent();
    assert filter.isBlank();

  }

  @Test(expected = EmptyCharSequenceException.class)
  public void testEmptyException() {
    OptionalCharSequence.ofNullable("").notEmptyOrElseThrow();
  }

  @Test(expected = BlankCharSequenceException.class)
  public void testBlankException() {
    OptionalCharSequence.ofNullable("  ").notBlankOrElseThrow();
  }

  @Test(expected = EmptyCharSequenceException.class)
  public void testSupplierException() {
    OptionalCharSequence.ofNullable("  ")
        .notBlankOrElseThrow(EmptyCharSequenceException::new);
  }
}
