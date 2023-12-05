package com.direcsik.project.facade.impl

import com.direcsik.project.entity.User
import com.direcsik.project.entity.dto.UserDto
import com.direcsik.project.facade.UserFacade
import com.direcsik.project.service.PostService
import com.direcsik.project.service.UserService
import com.direcsik.project.util.GroovyModelMapper
import com.direcsik.project.util.JwtTokenHelper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import spock.lang.Specification

class UserFacadeImplTest extends Specification {

    private static final String USER_ID = 'user_id'
    private static final String SECOND_USER_ID = 'second_user_id'
    private static final String USER_USERNAME = 'user_username'
    private static final String USER_PASSWORD = 'user_password'

    private UserService userService = Mock()
    private PostService postService = Mock()
    private AuthenticationManager authenticationManager = Mock()
    private JwtTokenHelper jwtTokenHelper = Mock()
    private GroovyModelMapper modelMapper = Mock()
    private UserFacade userFacade

    def setup() {
        userFacade = new UserFacadeImpl(userService: userService, postService: postService,
                authenticationManager: authenticationManager, jwtTokenHelper: jwtTokenHelper, modelMapper: modelMapper)
    }

    def 'should create user'() {
        given:
        UserDto userDto = new UserDto()
        User user = new User()

        modelMapper.map(userDto, User.class) >> user

        when:
        userFacade.createUser(userDto)

        then:
        1 * userService.createUser(user)
    }

    def 'should create token'() {
        given:
        Authentication authentication = Mock()
        authenticationManager.authenticate(_) >> authentication

        when:
        userFacade.createTokenForUser(USER_USERNAME, USER_PASSWORD)

        then:
        1 * jwtTokenHelper.generateJwtToken(_ as Authentication)
    }

    def 'should remove user'() {
        given:
        User user = new User()

        user.setId(USER_ID)
        userService.findById(USER_ID) >> user

        when:
        userFacade.removeUser(USER_ID)

        then:
        0 * userService.removePost(_, _)
        1 * userService.removeUser(_)
    }

    def 'should subscribe on target user'() {
        given:
        User source = new User()
        User target = new User()

        userService.findById(USER_ID) >> source
        userService.findById(SECOND_USER_ID) >> target

        when:
        userFacade.subscribe(USER_ID, SECOND_USER_ID)

        then:
        1 * userService.subscribe(source, target)
    }

    def 'should unsubscribe from target user'() {
        given:
        User source = new User()
        User target = new User()

        userService.findById(USER_ID) >> source
        userService.findById(SECOND_USER_ID) >> target

        when:
        userFacade.unsubscribe(USER_ID, SECOND_USER_ID)

        then:
        1 * userService.unsubscribe(source, target)
    }

    def 'should get user feed'() {
        given:
        User user = new User()

        userService.findById(USER_ID) >> user

        when:
        userFacade.getUserFeed(USER_ID)

        then:
        1 * userService.getUserFeed(user)
    }
}
