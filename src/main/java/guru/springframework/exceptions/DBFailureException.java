package guru.springframework.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DBFailureException extends RuntimeException {

    public DBFailureException() {
        super();
    }

    public DBFailureException(String message) {
        super(message);
    }

    public DBFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}