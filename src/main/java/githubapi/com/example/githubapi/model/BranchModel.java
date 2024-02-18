package githubapi.com.example.githubapi.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BranchModel {
    public String name;
    public String sha;

    public static BranchModel mapToModelFromJsonNode(JsonNode node) {
        if(node == null) {
            return null;
        }

        BranchModel model = new BranchModel();
        model.setName(node.path("name").asText());
        JsonNode commit = node.path("commit");

        if(!commit.isMissingNode()) {
            model.setSha(commit.path("sha").asText());
        } else {
            model.setSha("");
        }

        return model;
    }
}
