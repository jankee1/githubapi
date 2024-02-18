package githubapi.com.example.githubapi.controller;

import githubapi.com.example.githubapi.model.RepositoryModel;
import githubapi.com.example.githubapi.service.GithubApiService;
import githubapi.com.example.githubapi.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class GithubApiController {

    private final GithubApiService githubApiService;

    @Autowired
    public GithubApiController(GithubApiService githubApiService) {
        this.githubApiService = githubApiService;
    }

    @GetMapping("/{login}")
    public List<RepositoryModel> getInfo(@PathVariable String login){
        Validator.username(login); // it should have been done by implementing annotation in the method, however it is the only way that I have managed to make it work
        return this.githubApiService.getRepositories(login);
    }
}
