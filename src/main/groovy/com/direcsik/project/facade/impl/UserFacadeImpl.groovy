package com.direcsik.project.facade.impl

import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User
import com.direcsik.project.entity.dto.PostDto
import com.direcsik.project.entity.dto.UserDto
import com.direcsik.project.facade.UserFacade
import com.direcsik.project.security.UserPrinciple
import com.direcsik.project.service.PostService
import com.direcsik.project.service.UserService
import com.direcsik.project.util.GroovyModelMapper
import com.direcsik.project.util.JwtTokenHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserFacadeImpl implements UserFacade {

    @Autowired
    private UserService userService
    @Autowired
    private PostService postService
    @Autowired
    private AuthenticationManager authenticationManager
    @Autowired
    private JwtTokenHelper jwtTokenHelper
    @Autowired
    private GroovyModelMapper modelMapper

    @Override
    UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class)
        User updatedUser = userService.createUser(user)
        return modelMapper.map(updatedUser, UserDto.class)
    }

    @Override
    String createTokenForUser(String username, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password)
        Authentication authentication = authenticationManager.authenticate(token)
        SecurityContextHolder.getContext().setAuthentication(authentication)
        return jwtTokenHelper.generateJwtToken(authentication)
    }

    @Override
    UserDto updateUserPassword(String newPassword, UserPrinciple principle) {
        User updatedUser = userService.updateUserPassword(newPassword, principle.getId())
        UserPrinciple updatedPrinciple = new UserPrinciple(updatedUser)
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(updatedPrinciple, null, updatedPrinciple.getAuthorities())
        SecurityContextHolder.getContext().setAuthentication(newAuthentication)
        return modelMapper.map(updatedUser, UserDto.class)
    }

    @Override
    void removeUser(String userId) {
        User user = userService.findById(userId)
        removeRelatedPosts(user)
        userService.removeUser(user)
    }

    @Override
    UserDto subscribe(String sourceId, String targetId) {
        User sourceUser = userService.findById(sourceId)
        User targetUser = userService.findById(targetId)
        return modelMapper.map(userService.subscribe(sourceUser, targetUser), UserDto.class)
    }

    @Override
    void unsubscribe(String sourceId, String targetId) {
        User sourceUser = userService.findById(sourceId)
        User targetUser = userService.findById(targetId)
        userService.unsubscribe(sourceUser, targetUser)
    }

    @Override
    List<PostDto> getCurrentUserFeed(String userId) {
        User user = userService.findById(userId)
        List<Post> posts = userService.getCurrentUserFeed(user)
        return convertPostsToDto(posts)
    }

    @Override
    List<PostDto> getUserFeed(String userId) {
        User user = userService.findById(userId)
        List<Post> posts = userService.getUserFeed(user)
        return convertPostsToDto(posts)
    }

    private void removeRelatedPosts(User user) {
        List<Post> posts = user.getPosts()
        for (Post post : posts) {
            postService.removePost(post)
        }
    }

    private List<PostDto> convertPostsToDto(List<Post> posts) {
        return posts.stream()
                .map { post -> modelMapper.map(post, PostDto) }
                .toList()
    }
}
