package com.direcsik.project.entity

import groovy.transform.Canonical
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
@Canonical
class Subscription {

    @Id
    private String id

    @DBRef(lazy = true)
    private User subscriber

    @DBRef(lazy = true)
    private User target
}
