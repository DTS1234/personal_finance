package personal.finance.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import personal.finance.summary.application.exceptions.NoSummaryInDraftException;

@RestControllerAdvice
@CrossOrigin
public class ApplicationErrorHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handle(IllegalStateException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(NoSummaryInDraftException.class)
    public ResponseEntity<ErrorResponse> handle(NoSummaryInDraftException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    record ErrorResponse(String message) {

    }
}
