package githubapi.com.example.githubapi.service;

import githubapi.com.example.githubapi.model.RepositoryModel;

public class GithubApiUrlCreator {
    private final String githubBaseUrl = "https://api.github.com/";
    private final String githubRepos = "repos";
    private final String githubUsers = "users";
    private final String githubBranches = "branches";

    protected String getRepositoriesUrl(String login) {
        return this.githubBaseUrl + this.githubUsers + "/" + login + "/" + this.githubRepos;
    }

    protected String getBranchesUrl(RepositoryModel model){
        return this.githubBaseUrl + this.githubRepos + "/" + model.getRepositoryOwner() + "/" + model.getRepositoryName() + "/" + this.githubBranches;
    }
}
