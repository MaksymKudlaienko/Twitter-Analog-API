package com.direcsik.project.entity.dto

import groovy.transform.Canonical
import jakarta.validation.constraints.NotBlank

@Canonical
class PostDto {

    private String id

    @NotBlank
    private String text

    private List<LikeDto> likes

    private List<CommentDto> comments

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    String getText() {
        return text
    }

    void setText(String text) {
        this.text = text
    }

    List<LikeDto> getLikes() {
        return likes
    }

    void setLikes(List<LikeDto> likes) {
        this.likes = likes
    }

    List<CommentDto> getComments() {
        return comments
    }

    void setComments(List<CommentDto> comments) {
        this.comments = comments
    }
}
