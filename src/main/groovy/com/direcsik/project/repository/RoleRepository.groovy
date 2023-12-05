package com.direcsik.project.repository

import com.direcsik.project.entity.Role
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface RoleRepository extends MongoRepository<Role, String> {

    @Query("{name:'?0'}")
    Optional<Role> findByName(String name)
}