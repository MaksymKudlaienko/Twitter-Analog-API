package com.direcsik.project.controller

import com.direcsik.project.entity.Post
import com.direcsik.project.entity.dto.CommentDto
import com.direcsik.project.entity.dto.PostDto
import com.direcsik.project.facade.PostFacade
import com.direcsik.project.security.UserPrinciple
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping('api/v1/posts')
class PostController {

    @Autowired
    private PostFacade postFacade

    @PostMapping
    ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, @AuthenticationPrincipal UserPrinciple principle,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body('Invalid post text')
        }
        PostDto post = postFacade.createPost(postDto, principle)
        return ResponseEntity.status(HttpStatus.CREATED).body(post)
    }

    @PatchMapping(value = '/{postId}')
    ResponseEntity<?> updatePost(@PathVariable('postId') String postId, @Valid @RequestBody @NotEmpty String postText,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body('Invalid post text')
        }
        if (postText == null || postText.isBlank()) {
            return ResponseEntity.badRequest().body('Invalid post text')
        }
        PostDto post = postFacade.updatePost(postId, postText)
        return ResponseEntity.ok(post)
    }

    @DeleteMapping('/{postId}')
    ResponseEntity<?> removePost(@PathVariable('postId') String postId, @AuthenticationPrincipal UserPrinciple principle) {
        postFacade.removePost(postId, principle)
        return ResponseEntity.ok().build()
    }

    @PostMapping('/{postId}/like')
    ResponseEntity<?> likePost(@PathVariable('postId') String postId, @AuthenticationPrincipal UserPrinciple principle) {
        PostDto post = postFacade.createLike(postId, principle)
        return ResponseEntity.ok(post)
    }

    @PostMapping('/{postId}/unlike')
    ResponseEntity<?> unlikePost(@PathVariable('postId') String postId, @AuthenticationPrincipal UserPrinciple principle) {
        Post post = postFacade.removeLike(postId, principle)
        return ResponseEntity.ok(post)
    }

    @GetMapping('/{postId}/comments')
    ResponseEntity<?> getPostComments(@PathVariable('postId') String postId) {
        List<CommentDto> comments = postFacade.getPostComments(postId)
        return ResponseEntity.ok(comments)
    }
}
