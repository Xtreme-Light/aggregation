package com.springboot.owl.jgit.impl;

import com.springboot.owl.jgit.property.GitProperties;
import com.springboot.owl.jgit.service.OwlGit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
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
  public void load(String localPath, OwlGitConsumer consumer) throws IOException {
    if (!StringUtils.hasText(localPath)) {
      return;
    }
    try (
        final Repository repository = FileRepositoryBuilder.create(
            Paths.get(localPath, ".git").toFile());
        final Git git = new Git(repository);
    ) {
      if (consumer != null) {
        consumer.accept(this, git);
      }
    }
  }

  @Override
  public void load(String localPath) throws IOException {
    load(localPath, null);
  }

  @Override
  public void gitClone(String uri, File directory) throws GitAPIException {
    gitClone(uri, directory, null);
  }

  @Override
  public void gitClone(String uri, File directory, OwlGitConsumer consumer) throws GitAPIException {
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
        consumer.accept(this, git);
      }
    }
  }

  /**
   * 列出远端分支
   */
  @Override
  public List<Ref> remoteBranches(Git git) throws GitAPIException {
    return git.branchList().setListMode(ListMode.REMOTE).call();
  }

  /**
   * 列出所有分支
   */
  @Override
  public List<Ref> branches(Git git) throws GitAPIException {
    return git.branchList().setListMode(ListMode.ALL).call();
  }


  /**
   * checkout
   */
  @Override
  public void gitCheckout(Git git, String branchName) throws GitAPIException {
    if (git != null) {
      if (branchNameExists(git, branchName)) {
        git.checkout().setCreateBranch(false).setName(branchName)
            .setUpstreamMode(SetupUpstreamMode.TRACK)
            .call();
      } else {
        git.checkout().setCreateBranch(true).setName(branchName)
            .setUpstreamMode(SetupUpstreamMode.TRACK)
            .setStartPoint("origin/" + branchName).call();
      }
    }
  }

  @Override
  public void pull(Git git) throws GitAPIException {
    git.pull().call();
  }

  @Override
  public boolean branchNameExists(Git git, String branchName) throws GitAPIException {
    return this.branches(git).stream().anyMatch(v -> v.getName().contains(branchName));
  }


}
