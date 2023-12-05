package com.direcsik.project.service

import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService extends UserDetailsService {

    User createUser(User user)

    User updateUserPassword(String newPassword, String userId)

    User findById(String id)

    void addPost(Post post, User user)

    void removePost(Post post, User user)

    void removeUser(User user)

    User subscribe(User sourceUser, User targetUser)

    void unsubscribe(User sourceUser, User targetUser)

    List<Post> getCurrentUserFeed(User user)

    List<Post> getUserFeed(User user)
}
