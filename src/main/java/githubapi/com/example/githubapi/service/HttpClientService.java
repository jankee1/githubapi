package githubapi.com.example.githubapi.service;

import githubapi.com.example.githubapi.enums.QueryParam;
import githubapi.com.example.githubapi.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class HttpClientService {
    private final RestTemplate restTemplate;

    public HttpClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> makeGetRequest(String url, Map<String, Integer> queryParams) {
        ResponseEntity<String> responseEntity;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            queryParams.forEach(builder::queryParam);
            String urlWithParams = builder.toUriString();
            responseEntity = restTemplate.getForEntity(urlWithParams, String.class);
        } catch (Exception exception) {
            String message = CustomException.extractIncomingMessage(exception);
            if (exception instanceof HttpStatusCodeException) {
                HttpStatus httpStatus = CustomException.extractIncomingHttpStatus(exception);
                throw new CustomException(httpStatus, message);
            } else {
                throw new CustomException(HttpStatus.NOT_FOUND, message);
            }
        }
        return  responseEntity;
    }

    public HashMap<String, Integer> createQueryParams() {
        HashMap<String, Integer> queryParams = new HashMap<>();
        queryParams.put(QueryParam.PAGE.getValue(), 1);
        queryParams.put(QueryParam.PER_PAGE.getValue(), 100);
        return queryParams;
    }
}
