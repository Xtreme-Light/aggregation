package com.light.spring.message;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

@SpringBootTest
class SpringCloudMessageApplicationTests {

  @Test
  void contextLoads() {
  }


  @EnableAutoConfiguration
  @Configuration
  public static class EmptyConfiguration {

  }
}
