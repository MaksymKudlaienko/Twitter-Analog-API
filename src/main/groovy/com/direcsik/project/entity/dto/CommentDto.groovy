package com.direcsik.project.entity.dto

import groovy.transform.Canonical
import jakarta.validation.constraints.NotBlank

@Canonical
class CommentDto {

    @NotBlank
    private String text

    private SimpleUserDto user

    String getText() {
        return text
    }

    void setText(String text) {
        this.text = text
    }

    SimpleUserDto getUser() {
        return user
    }

    void setUser(SimpleUserDto user) {
        this.user = user
    }
}
