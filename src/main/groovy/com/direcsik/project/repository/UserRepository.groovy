package com.direcsik.project.repository

import com.direcsik.project.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends MongoRepository<User, String> {

    @Query("{username:'?0'}")
    Optional<User> findByUsername(String username)
}
