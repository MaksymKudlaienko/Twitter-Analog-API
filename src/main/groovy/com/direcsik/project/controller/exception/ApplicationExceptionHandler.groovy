package com.direcsik.project.controller.exception

import com.direcsik.project.exception.PostNotFoundException
import com.direcsik.project.exception.RoleNotFoundException
import com.direcsik.project.exception.UserAlreadyExistsException
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    ResponseEntity<Object> handlePostNotFoundException(PostNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage())
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<Object> handleUserNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage())
    }

    @ExceptionHandler(RoleNotFoundException.class)
    ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage())
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage())
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage())
    }

  /*  @ExceptionHandler(JwtTokenException.class)
    ResponseEntity<Object> handleJwtTokenException(JwtTokenException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage())
    }*/
}
