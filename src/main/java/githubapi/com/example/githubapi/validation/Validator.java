package githubapi.com.example.githubapi.validation;

import githubapi.com.example.githubapi.exception.CustomException;
import org.springframework.http.HttpStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static void username(String login) {
        // https://github.com/shinnn/github-username-regex
        String regex = "^[a-z\\d](?:[a-z\\d]|-(?=[a-z\\d])){0,38}$";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(login);
        if(!matcher.matches()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "GIVEN USERNAME IS NOT VALID GITHUB USERNAME");
        }
    }
}
