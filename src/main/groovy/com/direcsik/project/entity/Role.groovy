package com.direcsik.project.entity

import groovy.transform.Canonical
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@Canonical
class Role {

    @Id
    private String id

    private ERole name

    ERole getName() {
        return name
    }

    void setName(ERole name) {
        this.name = name
    }
}
