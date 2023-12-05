package com.direcsik.project.facade

import com.direcsik.project.entity.dto.CommentDto
import com.direcsik.project.entity.dto.PostDto
import com.direcsik.project.security.UserPrinciple

interface PostFacade {

    PostDto createPost(PostDto postDto, UserPrinciple principle)

    PostDto updatePost(String postId, String newText)

    void removePost(String postId, UserPrinciple principle)

    PostDto createLike(String postId, UserPrinciple principle)

    void removeLike(String postId, UserPrinciple principle)

    List<CommentDto> getPostComments(String postId)
}
