package githubapi.com.example.githubapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getStatus(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, ex.getStatus());
    }

    @Getter
    private static class ErrorMessage {
        private final int status;
        private final String message;

        public ErrorMessage(HttpStatus status, String message) {
            this.status = status.value();
            this.message = message;
        }
    }
}