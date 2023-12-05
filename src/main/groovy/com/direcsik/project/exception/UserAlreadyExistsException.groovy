package com.direcsik.project.exception

class UserAlreadyExistsException extends RuntimeException {

    UserAlreadyExistsException(String message) {
        super(message)
    }
}
