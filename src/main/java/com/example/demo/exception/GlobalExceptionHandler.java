// package com.example.demo.exception;

// import com.example.demo.dto.ApiResponse;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice
// public class GlobalExceptionHandler {

//     @ExceptionHandler(ResourceNotFoundException.class)
//     public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
//         ApiResponse response = new ApiResponse(false, ex.getMessage());
//         return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//     }

//     @ExceptionHandler(IllegalArgumentException.class)
//     public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
//         ApiResponse response = new ApiResponse(false, ex.getMessage());
//         return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//     }

//     @ExceptionHandler(Exception.class)
//     public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
//         ApiResponse response = new ApiResponse(false, "An error occurred: " + ex.getMessage());
//         return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//     }
// }
