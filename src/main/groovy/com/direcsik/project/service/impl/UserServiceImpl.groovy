package com.direcsik.project.service.impl


import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User
import com.direcsik.project.exception.UserAlreadyExistsException

import com.direcsik.project.repository.UserRepository
import com.direcsik.project.security.UserPrinciple
import com.direcsik.project.service.RoleService
import com.direcsik.project.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository
    @Autowired
    private RoleService roleService
    @Autowired
    private PasswordEncoder passwordEncoder

    @Override
    User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(String.format('User with %s username already exists', user.getUsername()))
        }
        String encodedPassword = encodeUserPassword(user.getPassword())
        user.setPassword(encodedPassword)
        user.setRoles(List.of(roleService.getUserRole()))
        return userRepository.save(user)
    }

    @Override
    User updateUserPassword(String newPassword, String userId) {
        User user = findById(userId)
        String encodedPassword = encodeUserPassword(newPassword)
        user.setPassword(encodedPassword)
        return userRepository.save(user)
    }

    @Override
    User findById(String userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UsernameNotFoundException(String.format('User with %s id was not found', userId)))
    }

    @Override
    void addPost(Post post, User user) {
        if (user.getPosts() == null) {
            user.setPosts(new ArrayList<>())
        }
        user.getPosts().add(post)
        userRepository.save(user)
    }

    @Override
    void removePost(Post post, User user) {
        if (user.getPosts() != null) {
            user.getPosts().remove(post)
            userRepository.save(user)
        }
    }

    @Override
    void removeUser(User user) {
        userRepository.delete(user)
    }

    @Override
    User subscribe(User sourceUser, User targetUser) {
        if (sourceUser.getSubscriptions() == null) {
            sourceUser.setSubscriptions(new ArrayList<>())
        }
        if (isUserContainsSubscription(sourceUser, targetUser.getId())) {
            return sourceUser
        }
        sourceUser.getSubscriptions().add(targetUser)
        return userRepository.save(sourceUser)
    }

    @Override
    void unsubscribe(User sourceUser, User targetUser) {
        if (sourceUser.getSubscriptions() != null) {
            sourceUser.getSubscriptions().remove(targetUser)
            userRepository.save(sourceUser)
        }
    }

    @Override
    List<Post> getCurrentUserFeed(User user) {
        if (user.getSubscriptions() == null) {
            return Collections.emptyList()
        }
        return user.getSubscriptions().stream()
                .flatMap { subscription -> subscription.getPosts().stream() }
                .toList()
    }

    @Override
    List<Post> getUserFeed(User user) {
        if (user.getPosts() == null) {
            return Collections.emptyList()
        }
        return user.getPosts()
    }

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format('User with %s username was not found', username)))
        return new UserPrinciple(user)
    }

    private String encodeUserPassword(String password) {
        return passwordEncoder.encode(password)
    }

    private boolean isUserContainsSubscription(User user, userId) {
        user.getSubscriptions().stream()
                .anyMatch { subscription -> subscription.getId() == userId }
    }
}
