package com.direcsik.project.facade.impl

import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User
import com.direcsik.project.facade.PostFacade
import com.direcsik.project.security.UserPrinciple
import com.direcsik.project.service.PostService
import com.direcsik.project.service.UserService
import com.direcsik.project.util.GroovyModelMapper
import spock.lang.Specification

class PostFacadeImplTest extends Specification {

    private static final String USER_ID = 'user_id'
    private static final String POST_ID = 'post_id'
    private static final String POST_TEXT = 'post_text'

    private PostService postService = Mock()
    private UserService userService = Mock()
    private GroovyModelMapper modelMapper = Mock()
    private PostFacade postFacade

    def setup() {
        postFacade = new PostFacadeImpl(postService: postService, userService: userService, modelMapper: modelMapper)
    }

    def 'should update post with text'() {
        when:
        postFacade.updatePost(POST_ID, POST_TEXT)

        then:
        1 * postService.updatePost(POST_ID, POST_TEXT)
    }

    def 'should remove post'() {
        given:
        User user = new User()
        Post post = new Post()
        UserPrinciple userPrinciple = new UserPrinciple(user)

        user.setId(USER_ID)
        userService.findById(USER_ID) >> user
        postService.findById(POST_ID) >> post

        when:
        postFacade.removePost(POST_ID, userPrinciple)

        then:
        1 * postService.removePost(post)
    }

    def 'should add like'() {
        given:
        User user = new User()
        Post post = new Post()
        UserPrinciple userPrinciple = new UserPrinciple(user)

        user.setId(USER_ID)
        userService.findById(USER_ID) >> user
        postService.findById(POST_ID) >> post

        when:
        postFacade.createLike(POST_ID, userPrinciple)

        then:
        1 * postService.addLike(post, _)
    }

    def 'should remove like'() {
        given:
        User user = new User()
        Post post = new Post()
        UserPrinciple userPrinciple = new UserPrinciple(user)

        user.setId(USER_ID)
        userService.findById(USER_ID) >> user
        postService.findById(POST_ID) >> post

        when:
        postFacade.removeLike(POST_ID, userPrinciple)

        then:
        1 * postService.removeLike(post, _)
    }
}
