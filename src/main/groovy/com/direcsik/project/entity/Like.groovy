package com.direcsik.project.entity

import groovy.transform.Canonical
import org.springframework.data.mongodb.core.mapping.DBRef

@Canonical
class Like {

    @DBRef
    private User owner

    User getOwner() {
        return owner
    }

    void setOwner(User owner) {
        this.owner = owner
    }
}
