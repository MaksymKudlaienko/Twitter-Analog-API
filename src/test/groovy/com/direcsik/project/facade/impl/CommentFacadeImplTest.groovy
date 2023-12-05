package com.direcsik.project.facade.impl

import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User
import com.direcsik.project.entity.dto.CommentDto
import com.direcsik.project.facade.CommentFacade
import com.direcsik.project.security.UserPrinciple
import com.direcsik.project.service.PostService
import com.direcsik.project.service.UserService
import com.direcsik.project.util.GroovyModelMapper
import spock.lang.Specification

class CommentFacadeImplTest extends Specification {

    private static final String USER_ID = 'user_id'
    private static final String USER_USERNAME = 'user_username'
    private static final String USER_PASSWORD = 'user_password'
    private static final String COMMENT_TEXT = 'comment_text'
    private static final String POST_ID = 'post_id'
    private GroovyModelMapper modelMapper = Mock()
    private PostService postService = Mock()
    private UserService userService = Mock()
    private CommentFacade commentFacade

    def setup() {
        commentFacade = new CommentFacadeImpl(modelMapper: modelMapper, postService: postService, userService: userService)
    }

    def "should create a comment for post"() {
        given:
        User user = createUser(USER_ID, USER_USERNAME, USER_PASSWORD)
        Post post = createPost(POST_ID)
        CommentDto commentDto = new CommentDto(text: COMMENT_TEXT)
        UserPrinciple userPrinciple = new UserPrinciple(user)

        userService.findById(userPrinciple.id) >> user
        postService.findById(POST_ID) >> post

        when:
        commentFacade.createComment(commentDto, POST_ID, userPrinciple)

        then:
        1 * postService.addComment(commentDto.text, _, _)
    }

    private User createUser(String id, String username, String password) {
        User user = new User()
        user.setId(id)
        user.setUsername(username)
        user.setPassword(password)
        return user
    }

    private Post createPost(String id) {
        Post post = new Post()
        post.setId(id)
        return post
    }
}
