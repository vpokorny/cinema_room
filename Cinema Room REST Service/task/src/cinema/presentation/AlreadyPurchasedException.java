package cinema.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyPurchasedException extends RuntimeException {
    private final String error;
    public AlreadyPurchasedException(String error) {
        this.error = error;
    }
}
