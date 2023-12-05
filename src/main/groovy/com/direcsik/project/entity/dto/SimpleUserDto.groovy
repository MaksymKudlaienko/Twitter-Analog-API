package com.direcsik.project.entity.dto

import groovy.transform.Canonical

@Canonical
class SimpleUserDto {

    private String username

    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
    }
}
