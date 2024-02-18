package githubapi.com.example.githubapi.model;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepositoryModel {
    public String repositoryName;
    public String repositoryOwner;
    public List<BranchModel> branches;

    public static RepositoryModel mapToModelFromJsonNode(JsonNode node) {
        if(node == null) {
            return null;
        }

        RepositoryModel model = new RepositoryModel();
        model.setRepositoryName(node.path("name").asText());
        JsonNode owner = node.path("owner");

        if(!owner.isMissingNode()) {
            model.setRepositoryOwner(owner.path("login").asText());
        } else {
            model.setRepositoryOwner("");
        }

        return model;
    }
}
