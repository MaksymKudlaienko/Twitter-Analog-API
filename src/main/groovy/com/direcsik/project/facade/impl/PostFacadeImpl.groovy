package com.direcsik.project.facade.impl

import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User
import com.direcsik.project.entity.dto.CommentDto
import com.direcsik.project.entity.dto.PostDto
import com.direcsik.project.facade.PostFacade
import com.direcsik.project.security.UserPrinciple
import com.direcsik.project.service.PostService
import com.direcsik.project.service.UserService
import com.direcsik.project.util.GroovyModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PostFacadeImpl implements PostFacade {

    @Autowired
    private PostService postService
    @Autowired
    private UserService userService
    @Autowired
    private GroovyModelMapper modelMapper

    @Override
    PostDto createPost(PostDto postDto, UserPrinciple principle) {
        User user = userService.findById(principle.getId())
        Post post = postService.createPost(postDto.getText(), user)
        userService.addPost(post, user)
        return modelMapper.map(post, PostDto.class)
    }

    @Override
    PostDto updatePost(String postId, String newText) {
        Post post = postService.updatePost(postId, newText)
        return modelMapper.map(post, PostDto.class)
    }

    @Override
    void removePost(String postId, UserPrinciple principle) {
        User user = userService.findById(principle.getId())
        Post post = postService.findById(postId)
        userService.removePost(post, user)
        postService.removePost(post)
    }

    @Override
    PostDto createLike(String postId, UserPrinciple principle) {
        User user = userService.findById(principle.getId())
        Post post = postService.findById(postId)
        return modelMapper.map(postService.addLike(post, user), PostDto.class)
    }

    @Override
    void removeLike(String postId, UserPrinciple principle) {
        User user = userService.findById(principle.getId())
        Post post = postService.findById(postId)
        postService.removeLike(post, user)
    }

    @Override
    List<CommentDto> getPostComments(String postId) {
        Post post = postService.findById(postId)
        return post.getComments().stream()
                .map {comment -> modelMapper.map(comment, CommentDto)}
                .toList()
    }
}
