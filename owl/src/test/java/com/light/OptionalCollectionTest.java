package com.light;

import com.light.owl.OptionalCollection;
import com.light.owl.exceptions.EmptyCollectionException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class OptionalCollectionTest {

  @Test(expected = NullPointerException.class)
  public void testException() {
    OptionalCollection.of(null);
  }

  @Test(expected = EmptyCollectionException.class)
  public void testException3() {
    OptionalCollection.ofNullable(null).orElseThrow();
  }

  @Test(expected = EmptyCollectionException.class)
  public void testException4() {
    OptionalCollection.ofNullable(new ArrayList<>()).notEmptyOrElseThrow();
  }

  @Test
  public void testException2() {
    final Cargo cargo1 = new Cargo("杯子", "美国", 100);
    final List<? extends String> collect = OptionalCollection.of(List.of(cargo1))
        .map(Cargo::getName).collect(Collectors.toList());
    assert collect.size() == 1;
  }

  @Test
  public void test() {
    final Cargo cargo1 = new Cargo("杯子", "美国", 100);
    final Cargo cargo2 = new Cargo("被子", "韩国", 99);
    final List<Cargo> arrayList = new ArrayList<>();
    arrayList.add(cargo1);
    arrayList.add(cargo2);
    final OptionalCollection<List<Cargo>, Cargo> of = OptionalCollection.ofNullable(arrayList);

    assert !of.isEmpty();
    final List<String> collect = of.map(Cargo::getName).collect(Collectors.toList());
    assert collect.get(0).equals("杯子");

    of.ifNotEmpty(System.out::println);

    final List<Cargo> cargos = of.notEmptyOrElse(new ArrayList<>());
    assert cargos.size() == 2;

    final List<? extends Cargo> collect1 = of.filter(v -> v.weight == 100)
        .collect(Collectors.toList());
    assert !collect1.isEmpty();
    assert collect1.size() == 1;

    of.ifNotEmptyOrElse(System.out::println, () -> {
      System.out.println(" got empty");
    });

  }


  public static class Cargo {

    private Cargo() {

    }

    public Cargo(String name, String target, int weight) {
      this.name = name;
      this.target = target;
      this.weight = weight;
    }

    private String name;
    private String target;
    private int weight;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getTarget() {
      return target;
    }

    public void setTarget(String target) {
      this.target = target;
    }

    public int getWeight() {
      return weight;
    }

    public void setWeight(int weight) {
      this.weight = weight;
    }

    @Override
    public String toString() {
      return "Cargo{" +
          "name='" + name + '\'' +
          ", target='" + target + '\'' +
          ", weight=" + weight +
          '}';
    }
  }

  public static class InternationalCargo extends Cargo {

    public InternationalCargo(String name, String target, int weight, boolean international) {
      super(name, target, weight);
      this.international = international;
    }

    private boolean international;

    public boolean isInternational() {
      return international;
    }

    public void setInternational(boolean international) {
      this.international = international;
    }

    @Override
    public String toString() {
      return "InternationalCargo{" +
          "international=" + international +
          ", name='" + super.name + '\'' +
          ", target='" + super.target + '\'' +
          ", weight=" + super.weight +
          '}';
    }
  }
}
