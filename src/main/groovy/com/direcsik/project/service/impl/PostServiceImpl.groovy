package com.direcsik.project.service.impl

import com.direcsik.project.entity.Comment
import com.direcsik.project.entity.Like
import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User
import com.direcsik.project.exception.PostNotFoundException
import com.direcsik.project.repository.PostRepository
import com.direcsik.project.service.PostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository

    @Override
    Post findById(String postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(String.format('Post with %s id was not found', postId)))
    }

    @Override
    Post createPost(String postText, User user) {
        Post post = new Post()
        post.setText(postText)
        post.setUser(user)
        return postRepository.save(post)
    }

    @Override
    Post updatePost(String postId, String newText) {
        Post post = findById(postId)
        post.setText(newText)
        return postRepository.save(post)
    }

    @Override
    void removePost(Post post) {
        postRepository.delete(post)
    }

    @Override
    Post addComment(String commentText, Post post, User user) {
        if (post.getComments() == null) {
            post.setComments(new ArrayList<>())
        }
        Comment comment = new Comment()
        comment.setUser(user)
        comment.setText(commentText)
        post.getComments().add(comment)
        return postRepository.save(post)
    }

    @Override
    Post addLike(Post post, User user) {
        if (post.getLikes() == null) {
            post.setLikes(new ArrayList<>())
        }
        Like like = new Like()
        like.setOwner(user)
        if (isPostContainsLike(post, user.getId())) {
            return post
        }
        post.getLikes().add(like)
        return postRepository.save(post)
    }

    @Override
    void removeLike(Post post, User user) {
        if (post.getLikes() != null) {
            post.getLikes().removeIf { like -> (like.getOwner() == user) }
            postRepository.save(post)
        }
    }

    private boolean isPostContainsLike(Post post, userId) {
        post.getLikes().stream()
                .anyMatch { like -> like.getOwner().getId() == userId }
    }
}
