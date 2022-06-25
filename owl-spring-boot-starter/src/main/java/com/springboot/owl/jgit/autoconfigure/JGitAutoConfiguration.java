package com.springboot.owl.jgit.autoconfigure;

import com.springboot.owl.jgit.JGit;
import com.springboot.owl.jgit.OwlGit;
import com.springboot.owl.jgit.property.GitProperties;
import org.eclipse.jgit.api.Git;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({Git.class})
@EnableConfigurationProperties(GitProperties.class)
@ConditionalOnProperty(name = "owl.git.enable", havingValue = "true")
public class JGitAutoConfiguration {


  @Bean
  @ConditionalOnMissingBean(OwlGit.class)
  public OwlGit ijGitService(
      GitProperties gitProperties
  ) {
    return new JGit(gitProperties);
  }
}
