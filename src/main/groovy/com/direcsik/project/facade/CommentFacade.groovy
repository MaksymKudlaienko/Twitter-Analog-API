package com.direcsik.project.facade

import com.direcsik.project.entity.dto.CommentDto
import com.direcsik.project.entity.dto.PostDto
import com.direcsik.project.security.UserPrinciple

interface CommentFacade {

    PostDto createComment(CommentDto commentDto, String postId, UserPrinciple principle)

}