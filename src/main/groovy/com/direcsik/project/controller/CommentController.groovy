package com.direcsik.project.controller

import com.direcsik.project.entity.dto.CommentDto
import com.direcsik.project.entity.dto.PostDto
import com.direcsik.project.facade.CommentFacade
import com.direcsik.project.security.UserPrinciple
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('api/v1/comments')
class CommentController {

    @Autowired
    private CommentFacade commentFacade

    @PostMapping
    ResponseEntity<?> createComment(@Valid @RequestBody CommentDto commentDto, @RequestParam('postId') String postId,
                                    @AuthenticationPrincipal UserPrinciple principle, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body('Comment text cannot be empty')
        }
        PostDto post = commentFacade.createComment(commentDto, postId, principle)
        return ResponseEntity.ok(post)
    }
}
