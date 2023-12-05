package com.direcsik.project.entity.dto

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import groovy.transform.Canonical
import jakarta.validation.constraints.NotBlank

@Canonical
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
class UserDto {

    private String id

    @NotBlank
    private String username
    @NotBlank
    private String password

    private List<SimpleUserDto> subscriptions

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
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

    List<SimpleUserDto> getSubscriptions() {
        return subscriptions
    }

    void setSubscriptions(List<SimpleUserDto> subscriptions) {
        this.subscriptions = subscriptions
    }
}
