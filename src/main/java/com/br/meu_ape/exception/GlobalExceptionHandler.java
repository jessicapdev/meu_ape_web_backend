package com.br.meu_ape.exception;

import com.br.meu_ape.model.response.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  public static class CustomException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    public CustomException(String message, HttpStatus httpStatus) {
      super(message);
      this.httpStatus = httpStatus;
      this.errorCode = "ERR-" + httpStatus.value();
    }

    public CustomException(String message, HttpStatus httpStatus, String errorCode) {
      super(message);
      this.httpStatus = httpStatus;
      this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
      return httpStatus;
    }

    public String getErrorCode() {
      return errorCode;
    }
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  public static class ObjectNotFoundException extends CustomException {
    public ObjectNotFoundException(String id, String entityName) {
      super(String.format("%s não encontrado com id: %s", entityName, id),
              HttpStatus.NOT_FOUND,
              "MONGO-404");
    }
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public static class ValidationException extends CustomException {
    public ValidationException(String message) {
      super(message, HttpStatus.BAD_REQUEST, "VALIDATION-400");
    }
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  public static class DuplicateKeyException extends CustomException {
    public DuplicateKeyException(String message) {
      super(message, HttpStatus.CONFLICT, "MONGO-409");
    }
  }

  // Configuração do manipulador de erros
  @Bean
  public ErrorAttributes errorAttributes() {
    return new DefaultErrorAttributes() {
      @Override
      public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        return super.getErrorAttributes(webRequest,
                ErrorAttributeOptions.defaults()
                        .excluding(ErrorAttributeOptions.Include.EXCEPTION));
      }
    };
  }

  @ExceptionHandler(CustomException.class)
  public void handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
    res.sendError(ex.getHttpStatus().value(), ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
  }

  @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
  public void handleDuplicateKeyException(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.CONFLICT.value(),
            "Duplicate key violation - the document already exists");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
    );
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(NoResourceFoundException ex) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            new Date(),
            "Recurso não encontrado",
            ex.getMessage()
    );
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

}