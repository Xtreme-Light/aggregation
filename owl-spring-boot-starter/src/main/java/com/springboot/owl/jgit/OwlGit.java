package com.springboot.owl.jgit;

import java.io.File;
import java.util.List;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

public interface OwlGit {


  void load(String localPath, OwlGitConsumer consumer) throws Exception;

  void load(String localPath) throws Exception;


  void gitClone(String uri, File directory) throws Exception;

  void gitClone(String uri, File directory, OwlGitConsumer consumer) throws Exception;


  interface OwlGitConsumer {

    void accept(Holder git) throws Exception;

  }

  class Holder {

    private final Git git;

    public Holder(Git git) {
      this.git = git;
    }

    public Git getGit() {
      return git;
    }


    /**
     * 列出远端分支
     */
    public List<Ref> remoteBranches() throws GitAPIException {
      return this.git.branchList().setListMode(ListMode.REMOTE).call();
    }

    /**
     * 列出所有分支
     */
    public List<Ref> branches() throws GitAPIException {
      return this.git.branchList().setListMode(ListMode.ALL).call();
    }


    /**
     * checkout
     */
    public void gitCheckout(String branchName) throws GitAPIException {
      if (this.git != null) {
        if (branchNameExists(branchName)) {
          this.git.checkout().setCreateBranch(false).setName(branchName)
              .setUpstreamMode(SetupUpstreamMode.TRACK)
              .call();
        } else {
          this.git.checkout().setCreateBranch(true).setName(branchName)
              .setUpstreamMode(SetupUpstreamMode.TRACK)
              .setStartPoint("origin/" + branchName).call();
        }
      }
    }

    public void pull() throws GitAPIException {
      this.git.pull().call();
    }

    public boolean branchNameExists(String branchName) throws GitAPIException {
      return branches().stream().anyMatch(v -> v.getName().contains(branchName));
    }

  }
}
