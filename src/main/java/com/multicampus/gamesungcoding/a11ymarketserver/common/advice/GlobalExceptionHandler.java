package com.multicampus.gamesungcoding.a11ymarketserver.common.advice;

import com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model.ErrorRespStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model.RestErrorResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new RestErrorResponse(
                        ErrorRespStatus.USER_NOT_FOUND.getStatusName(),
                        ex.getMessage())
                );
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<RestErrorResponse> handleDataNotFoundException(DataNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new RestErrorResponse(
                        ErrorRespStatus.DATA_NOT_FOUND.getStatusName(),
                        ex.getMessage())
                );
    }
}
