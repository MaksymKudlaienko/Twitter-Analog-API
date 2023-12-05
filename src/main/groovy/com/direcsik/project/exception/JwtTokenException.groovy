package com.direcsik.project.exception

class JwtTokenException extends RuntimeException {

    JwtTokenException(String message, Throwable cause) {
        super(message, cause)
    }
}
