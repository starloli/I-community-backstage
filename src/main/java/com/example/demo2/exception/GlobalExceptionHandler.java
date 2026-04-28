package com.example.demo2.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo2.dto.response.ErrorResponse;
import com.example.demo2.dto.response.FieldErrorDetail;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
                List<FieldErrorDetail> errors = new ArrayList<>();
                FieldErrorDetail error = new FieldErrorDetail("field", ex.getMessage());
                errors.add(error);
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse(
                                                404,
                                                "Not_Found",
                                                errors));
        }

        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
                List<FieldErrorDetail> errors = new ArrayList<>();
                FieldErrorDetail error = new FieldErrorDetail("field", ex.getMessage());
                errors.add(error);
                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(new ErrorResponse(
                                                401,
                                                "UNAUTHORIZED",
                                                errors));
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
                List<FieldErrorDetail> errors = new ArrayList<>();
                FieldErrorDetail error = new FieldErrorDetail("field", ex.getMessage());
                errors.add(error);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ErrorResponse(
                                                400,
                                                "BAD_REQUEST",
                                                errors));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

                List<FieldErrorDetail> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(err -> new FieldErrorDetail(
                                                err.getField(),
                                                err.getDefaultMessage()))
                                .toList();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ErrorResponse(
                                                400,
                                                "BAD_REQUEST",
                                                errors));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleException(Exception ex) {
                List<FieldErrorDetail> errors = new ArrayList<>();
                FieldErrorDetail error = new FieldErrorDetail("field", "system error");
                errors.add(error);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(
                                                500,
                                                "Internal_Error",
                                                errors));
        }

        @ExceptionHandler(AccountStatusException.class)
        public ResponseEntity<Map<String, Object>> handleAccountException(AccountStatusException ex) {
                Map<String, Object> body = new HashMap<>();
                body.put("errorCode", ex.getErrorCode()); // 關鍵：回傳 ACCOUNT_PENDING 或 ACCOUNT_INACTIVE
                body.put("message", ex.getMessage());

                return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
        }
}
