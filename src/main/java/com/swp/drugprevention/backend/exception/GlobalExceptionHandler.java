package com.swp.drugprevention.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //đây là nơi để bắt và handle những exception ta tự làm
    //ví dụ ngoài những lỗi có sẵn khi throw new thì mình có thể tự tạo ra lỗi mình mong muốn

    //lỗi bên dưới là lỗi khi đăng kí với 2 email trùng lặp
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // hoặc BAD_REQUEST nếu bạn muốn
                .body(Map.of(
                        "error", "Email is exist: ",
                        "message", ex.getMessage()
                ));
    }

    //có thể thêm các handler khác nếu muốn

    @ExceptionHandler(SameRoleException.class)
    public ResponseEntity<?> handleSameRoleException(SameRoleException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Same role error",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        String details = "Only users with the required role are authorized to perform this action.";
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "error", "ACCESS_DENIED",
                        "message", "Insufficient permissions",
                        "details", details
                ));
    }
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRunTimeException (RuntimeException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

