package com.springboot.owl.jgit.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

public interface OwlGit {


  void load(String localPath, OwlGitConsumer consumer) throws IOException;

  void load(String localPath) throws IOException;


  void gitClone(String uri, File directory) throws GitAPIException;

  void gitClone(String uri, File directory, OwlGitConsumer consumer) throws GitAPIException;

  List<Ref> remoteBranches(Git git) throws GitAPIException;

  List<Ref> branches(Git git) throws GitAPIException;


  void gitCheckout(Git git, String branchName) throws GitAPIException;


  void pull(Git git) throws GitAPIException;

  boolean branchNameExists(Git git, String branchName) throws GitAPIException;

  interface OwlGitConsumer {

    void accept(OwlGit jGit, Git git);
  }
}
