package com.light.owl;

import com.light.owl.exceptions.EmptyCollectionException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * 本类用于对collection 的快速判断和lambda 用法
 *
 * @param <T>
 * @param <E>
 */
public final class OptionalCollection<T extends Collection<E>, E> {

  private static final OptionalCollection<?, ?> EMPTY = new OptionalCollection<>(null);

  private final T value;


  public static <T extends Collection<E>, E> OptionalCollection<T, E> empty() {
    @SuppressWarnings("unchecked")
    OptionalCollection<T, E> t = (OptionalCollection<T, E>) EMPTY;
    return t;
  }

  private OptionalCollection(T value) {
    this.value = value;
  }

  public static <T extends Collection<E>, E> OptionalCollection<T, E> of(T value) {
    return new OptionalCollection<>(Objects.requireNonNull(value));
  }


  public static <T extends Collection<E>, E> OptionalCollection<T, E> ofNullable(T value) {
    return value == null ? empty() : new OptionalCollection<>(value);
  }

  public void ifNotEmpty(Consumer<? super T> action) {
    if (value != null && !value.isEmpty()) {
      action.accept(value);
    }
  }

  /**
   * 过滤器，
   */
  public Stream<? extends E> filter(
      Predicate<? super E> predicate) {
    Objects.requireNonNull(predicate);
    if (value == null || value.isEmpty()) {
      return Stream.empty();
    } else {
      return
          value.stream().filter(predicate);
    }
  }

  /**
   * 映射器，非空集合映射，映射新的OptionalCollection<List<U>, U>
   */
  public <U> Stream<? extends U> map(Function<? super E, ? extends U> mapper) {
    Objects.requireNonNull(mapper);
    if (value == null || value.isEmpty()) {
      return Stream.empty();
    } else {
      return value.stream().map(mapper);
    }
  }

  public T notEmptyOrElse(T other) {
    if (value != null && !value.isEmpty()) {
      return value;
    } else {
      return other;
    }
  }

  /**
   * 如果不为空 do action，else do emptyAction
   */
  public void ifNotEmptyOrElse(Consumer<? super T> action, Runnable emptyAction) {
    if (value != null && !value.isEmpty()) {
      action.accept(value);
    } else {
      emptyAction.run();
    }
  }

  public boolean isEmpty() {
    return value == null || value.isEmpty();
  }

  public T get() {
    if (value == null) {
      throw new NoSuchElementException("No value present");
    }
    return value;
  }

  /**
   * 转stream流
   */
  public Stream<E> stream() {
    if (value == null || value.isEmpty()) {
      return Stream.empty();
    } else {
      return value.stream();
    }
  }

  public T orElseThrow() {
    if (value == null) {
      throw new EmptyCollectionException("空白集合");
    }
    return value;
  }

  public T notEmptyOrElseThrow() {
    if (value == null || value.isEmpty()) {
      throw new EmptyCollectionException("空白集合");
    }
    return value;
  }

  public <X extends Throwable> T notEmptyOrElseThrow(
      Supplier<? extends X> exceptionSupplier) throws X {
    if (value != null && !value.isEmpty()) {
      return value;
    } else {
      throw exceptionSupplier.get();
    }
  }

}
