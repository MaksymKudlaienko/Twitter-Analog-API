package com.direcsik.project.service


import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User

interface PostService {

    Post findById(String postId)

    Post createPost(String postText, User user)

    Post updatePost(String postId, String newText)

    void removePost(Post post)

    Post addComment(String commentText, Post post, User user)

    Post addLike(Post post, User user)

    void removeLike(Post post, User user)
}
