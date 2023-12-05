package com.direcsik.project.entity

import groovy.transform.Canonical
import org.springframework.data.mongodb.core.mapping.DBRef

@Canonical
class Comment {

    private String text

    @DBRef(lazy = true)
    private User user

    String getText() {
        return text
    }

    void setText(String text) {
        this.text = text
    }

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.user = user
    }
}
