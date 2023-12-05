package com.direcsik.project.facade.impl

import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User
import com.direcsik.project.entity.dto.CommentDto
import com.direcsik.project.entity.dto.PostDto
import com.direcsik.project.facade.CommentFacade
import com.direcsik.project.security.UserPrinciple
import com.direcsik.project.service.PostService
import com.direcsik.project.service.UserService
import com.direcsik.project.util.GroovyModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CommentFacadeImpl implements CommentFacade {

    @Autowired
    private GroovyModelMapper modelMapper
    @Autowired
    private PostService postService
    @Autowired
    private UserService userService

    @Override
    PostDto createComment(CommentDto commentDto, String postId, UserPrinciple principle) {
        User user = userService.findById(principle.getId())
        Post post = postService.findById(postId)
        return modelMapper.map(postService.addComment(commentDto.getText(), post, user), PostDto.class)
    }
}
