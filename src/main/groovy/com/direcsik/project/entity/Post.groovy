package com.direcsik.project.entity

import groovy.transform.Canonical
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
@Canonical
class Post {

    @Id
    private String id

    @DBRef(lazy = true)
    private User user

    private List<Like> likes

    private List<Comment> comments

    private String text

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.user = user
    }

    List<Like> getLikes() {
        return likes
    }

    void setLikes(List<Like> likes) {
        this.likes = likes
    }

    List<Comment> getComments() {
        return comments
    }

    void setComments(List<Comment> comments) {
        this.comments = comments
    }

    String getText() {
        return text
    }

    void setText(String text) {
        this.text = text
    }
}
