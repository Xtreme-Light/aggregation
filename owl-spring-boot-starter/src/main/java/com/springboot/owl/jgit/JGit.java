package com.springboot.owl.jgit;

import com.springboot.owl.jgit.property.GitProperties;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.util.StringUtils;

public class JGit implements OwlGit {

  private CredentialsProvider defaultCredentialsProvider;

  public JGit(GitProperties gitProperties) {
    if (StringUtils.hasText(gitProperties.getUserName()) && StringUtils.hasText(
        gitProperties.getPassword())) {
      defaultCredentialsProvider = new UsernamePasswordCredentialsProvider(
          gitProperties.getUserName(),
          gitProperties.getPassword());
    }
  }

  @Override
  public void load(String localPath, OwlGitConsumer consumer) throws Exception {
    if (!StringUtils.hasText(localPath)) {
      return;
    }
    try (
        final Repository repository = FileRepositoryBuilder.create(
            Paths.get(localPath, ".git").toFile());
        final Git git = new Git(repository);
    ) {
      if (consumer != null) {
        consumer.accept(new Holder(git));
      }
    }
  }

  @Override
  public void load(String localPath) throws Exception {
    load(localPath, null);
  }

  @Override
  public void gitClone(String uri, File directory) throws Exception {
    gitClone(uri, directory, null);
  }

  @Override
  public void gitClone(String uri, File directory, OwlGitConsumer consumer) throws Exception {
    if (uri == null || uri.isEmpty()) {
      return;
    }
    if (directory == null) {
      return;
    }
    try (
        final Git git = Git.cloneRepository()
            .setURI(uri)
            .setDirectory(directory)
            .setCredentialsProvider(defaultCredentialsProvider)
            .call()
    ) {
      if (consumer != null) {
        consumer.accept(new Holder(git));
      }
    }
  }

  public static void main(String[] args) throws Exception {

    final JGit jGit = new JGit(null);
    jGit.gitClone("", null, _git -> {
      final List<Ref> branches = _git.branches();

    });
  }

}
