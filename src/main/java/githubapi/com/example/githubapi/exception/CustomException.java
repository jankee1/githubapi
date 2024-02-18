package githubapi.com.example.githubapi.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

@RequiredArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public static final String GENERAL_ERROR_MESSAGE = "SOMETHING WENT WRONG";

    public static HttpStatus extractIncomingHttpStatus(Exception exception) {
        return  (HttpStatus) ((HttpStatusCodeException) exception).getStatusCode();
    }

    public static String extractIncomingMessage(Exception exception) {
        // incoming message when i.e username is not found
        // "404 Not Found: \"{\\\"message\\\":\\\"Not Found\\\",\\\"documentation_url\\\":\\\"https://docs.github.com/rest/repos/repos#list-repositories-for-a-user\\\"}\""
        String exceptionAsString = exception.getMessage();
        String jsonContent = exceptionAsString.substring(exceptionAsString.indexOf(':') + 2).trim();
        // Remove double quotes at the beginning and the end
        jsonContent = jsonContent.substring(1, jsonContent.length() - 1);
        String messageFinal = CustomException.GENERAL_ERROR_MESSAGE;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            messageFinal = jsonNode.get("message").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageFinal;
    }
}
