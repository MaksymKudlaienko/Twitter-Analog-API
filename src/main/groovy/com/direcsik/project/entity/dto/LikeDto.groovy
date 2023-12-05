package com.direcsik.project.entity.dto

import groovy.transform.Canonical

@Canonical
class LikeDto {

    private SimpleUserDto owner

    SimpleUserDto getOwner() {
        return owner
    }

    void setOwner(SimpleUserDto owner) {
        this.owner = owner
    }
}
