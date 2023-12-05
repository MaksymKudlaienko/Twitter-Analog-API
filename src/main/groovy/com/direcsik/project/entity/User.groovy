package com.direcsik.project.entity


import groovy.transform.Canonical
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
@Canonical
class User {

    @Id
    private String id

    private String username

    private String password

    @DBRef(lazy = true)
    private List<Post> posts

    @DBRef(lazy = true)
    private List<Role> roles

    @DBRef(lazy = true)
    private List<User> subscriptions

    void setId(String id) {
        this.id = id
    }

    String getId() {
        return id
    }

    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
    }

    String getPassword() {
        return password
    }

    void setPassword(String password) {
        this.password = password
    }

    List<Post> getPosts() {
        return posts
    }

    void setPosts(List<Post> posts) {
        this.posts = posts
    }

    List<Comment> getComments() {
        return comments
    }

    List<Role> getRoles() {
        return roles
    }

    void setRoles(List<Role> roles) {
        this.roles = roles
    }

    List<User> getSubscriptions() {
        return subscriptions
    }

    void setSubscriptions(List<User> subscriptions) {
        this.subscriptions = subscriptions
    }
}
