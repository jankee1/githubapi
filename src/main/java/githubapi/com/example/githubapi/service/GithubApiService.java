package githubapi.com.example.githubapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import githubapi.com.example.githubapi.enums.QueryParam;
import githubapi.com.example.githubapi.exception.CustomException;
import githubapi.com.example.githubapi.model.BranchModel;
import githubapi.com.example.githubapi.model.RepositoryModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Getter
@Setter
public class GithubApiService extends GithubApiUrlCreator {
    private final HttpClientService httpClientService;

    @Autowired
    public GithubApiService(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    public List<RepositoryModel> getRepositories(String login) {
        String url = this.getRepositoriesUrl(login);
        List<RepositoryModel> modelsList = new ArrayList<>();

        HashMap<String, Integer> repositoryQueryParams = this.httpClientService.createQueryParams();
        int currentRepositoryPage = repositoryQueryParams.getOrDefault(QueryParam.PAGE.getValue(), 1);

        while (true) {
            repositoryQueryParams.put(QueryParam.PAGE.getValue(), currentRepositoryPage);
            ResponseEntity<String> repositoryResponse = this.httpClientService.makeGetRequest(url, repositoryQueryParams);
            try {
                JsonNode repositoriesRoot = this.getJsonNodes(repositoryResponse);
                modelsList.addAll(this.processRepositories(repositoriesRoot));
                if (repositoriesRoot.size() < repositoryQueryParams.get(QueryParam.PER_PAGE.getValue())) {
                    break;
                }
            } catch (JsonProcessingException exception) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, CustomException.GENERAL_ERROR_MESSAGE);
            }
            currentRepositoryPage++;
        }
        return modelsList;
    }

    private List<RepositoryModel> processRepositories(JsonNode repositoriesRoot) throws JsonProcessingException {
        List<RepositoryModel> modelsList = new ArrayList<>();
        for (JsonNode repositoryNode : repositoriesRoot) {
            if (repositoryNode.path("fork").asBoolean()) {
                continue;
            }
            RepositoryModel model = RepositoryModel.mapToModelFromJsonNode(repositoryNode);
            model.setBranches(this.getBranches(model));
            modelsList.add(model);
        }
        return modelsList;
    }

    private List<BranchModel> getBranches(RepositoryModel model) throws JsonProcessingException {
        String url = this.getBranchesUrl(model);
        List<BranchModel> branchesList = new ArrayList<>();

        HashMap<String, Integer> branchesQueryParams = this.httpClientService.createQueryParams();
        int currentBranchesPage = branchesQueryParams.getOrDefault(QueryParam.PAGE.getValue(), 1);

        while (true) {
            branchesQueryParams.put(QueryParam.PAGE.getValue(), currentBranchesPage);

            ResponseEntity<String> branchesResponse = this.httpClientService.makeGetRequest(url, branchesQueryParams);
            JsonNode branchesRoot = this.getJsonNodes(branchesResponse);

            for (JsonNode branchNode : branchesRoot) {
                branchesList.add(BranchModel.mapToModelFromJsonNode(branchNode));
            }

            if (branchesRoot.size() < branchesQueryParams.get(QueryParam.PER_PAGE.getValue())) {
                break;
            }
            currentBranchesPage++;
        }

        return branchesList;
    }

    private JsonNode getJsonNodes(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.getBody());
    }
}