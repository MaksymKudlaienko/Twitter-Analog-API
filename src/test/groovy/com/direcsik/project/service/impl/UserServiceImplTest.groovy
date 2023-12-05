package com.direcsik.project.service.impl

import com.direcsik.project.entity.ERole
import com.direcsik.project.entity.Post
import com.direcsik.project.entity.Role
import com.direcsik.project.entity.User
import com.direcsik.project.exception.UserAlreadyExistsException
import com.direcsik.project.repository.UserRepository
import com.direcsik.project.security.UserPrinciple
import com.direcsik.project.service.RoleService
import com.direcsik.project.service.UserService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceImplTest extends Specification {

    private static final String USER_ID = 'user_id'
    private static final String SECOND_USER_ID = 'second_user_id'
    private static final String USER_USERNAME = 'user_username'
    private static final String SECOND_USER_USERNAME = 'second_user_username'
    private static final String USER_PASSWORD = 'user_password'
    private static final String UPDATED_USER_PASSWORD = 'updated_user_password'
    private static final String ENCODED_USER_PASSWORD = 'encoded_user_password'
    private static final String POST_ID = 'post_id'

    private UserRepository userRepository = Mock()
    private RoleService roleService = Mock(RoleServiceImpl.class)
    private PasswordEncoder passwordEncoder = Mock()
    private UserService userService

    def setup() {
        userService = new UserServiceImpl(userRepository: userRepository, roleService: roleService, passwordEncoder: passwordEncoder)
    }

    def 'should create a user'() {
        given:
        User user = createFilledUser(null, USER_USERNAME, USER_PASSWORD)
        Role role = createFilledRole()

        userRepository.findByUsername(USER_USERNAME) >> Optional.empty()
        passwordEncoder.encode(USER_PASSWORD) >> ENCODED_USER_PASSWORD
        roleService.getUserRole() >> role

        when:
        userService.createUser(user)

        then:
        1 * userRepository.save(user)
    }

    def 'should throw exception and do not create user if user already exists'() {
        given:
        User user = createFilledUser(null, USER_USERNAME, USER_PASSWORD)

        userRepository.findByUsername(USER_USERNAME) >> Optional.of(user)

        when:
        userService.createUser(user)

        then:
        thrown(UserAlreadyExistsException.class)
    }

    def 'should update user password'() {
        given:
        User user = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)

        userRepository.findById(USER_ID) >> Optional.of(user)
        passwordEncoder.encode(USER_PASSWORD) >> ENCODED_USER_PASSWORD

        when:
        userService.updateUserPassword(UPDATED_USER_PASSWORD, USER_ID)

        then:
        1 * userRepository.save(user)
    }

    def 'should find user by id'() {
        given:
        User user = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)

        userRepository.findById(USER_ID) >> Optional.of(user)

        when:
        User result = userService.findById(USER_ID)

        then:
        result == user
    }

    def 'should throw exception if user not found by id'() {
        given:
        User user = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)

        userRepository.findById(USER_ID) >> Optional.empty()

        when:
        userService.findById(USER_ID)

        then:
        thrown(UsernameNotFoundException.class)
    }

    def 'should add post to user'() {
        given:
        User user = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)
        Post post = createFilledPost(POST_ID)

        when:
        userService.addPost(post, user)

        then:
        1 * userRepository.save(user)
    }

    def 'should remove post if exists'() {
        given:
        User user = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)
        Post post = createFilledPost(POST_ID)
        List<Post> userPosts = new ArrayList<>()

        userPosts.add(post)
        user.setPosts(userPosts)

        when:
        userService.removePost(post, user)

        then:
        1 * userRepository.save(user)
    }

    def 'should do not remove post if post do not exists'() {
        given:
        User user = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)
        Post post = createFilledPost(POST_ID)

        when:
        userService.removePost(post, user)

        then:
        0 * userRepository.save(user)
    }

    def 'should remove user'() {
        given:
        User user = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)

        when:
        userService.removeUser(user)

        then:
        1 * userRepository.delete(user)
    }

    def 'should create user subscription'() {
        given:
        User source = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)
        User target = createFilledUser(SECOND_USER_ID, SECOND_USER_USERNAME, USER_PASSWORD)

        when:
        userService.subscribe(source, target)

        then:
        1 * userRepository.save(source)
    }

    def 'should not create user subscription if already exists'() {
        given:
        User source = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)
        User target = createFilledUser(SECOND_USER_ID, SECOND_USER_USERNAME, USER_PASSWORD)

        List<User> subscriptions = new ArrayList<>()
        subscriptions.add(target)
        source.setSubscriptions(subscriptions)

        when:
        userService.subscribe(source, target)

        then:
        0 * userRepository.save(source)
    }

    def 'should remove user subscription if exists'() {
        given:
        User source = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)
        User target = createFilledUser(SECOND_USER_ID, SECOND_USER_USERNAME, USER_PASSWORD)

        List<User> subscriptions = new ArrayList<>()
        subscriptions.add(target)
        source.setSubscriptions(subscriptions)

        when:
        userService.unsubscribe(source, target)

        then:
        1 * userRepository.save(source)
    }

    def 'should not remove user subscription if not exists'() {
        given:
        User source = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)
        User target = createFilledUser(SECOND_USER_ID, SECOND_USER_USERNAME, USER_PASSWORD)

        when:
        userService.unsubscribe(source, target)

        then:
        0 * userRepository.save(source)
    }

    def 'should find user by username'() {
        given:
        User user = createFilledUser(USER_ID, USER_USERNAME, USER_PASSWORD)

        userRepository.findByUsername(USER_USERNAME) >> Optional.of(user)

        when:
        UserPrinciple result = userService.loadUserByUsername(USER_USERNAME)

        then:
        result.class == UserPrinciple.class
    }

    def 'should throw exception if user not found by username'() {
        given:
        userRepository.findByUsername(USER_USERNAME) >> Optional.empty()

        when:
        userService.loadUserByUsername(USER_USERNAME)

        then:
        thrown(UsernameNotFoundException.class)
    }

    private User createFilledUser(String id, String username, String password) {
        User user = new User()
        user.setId(id)
        user.setUsername(username)
        user.setPassword(password)
        return user
    }

    private Role createFilledRole() {
        Role role = new Role()
        role.setName(ERole.ROLE_TRUSTED_USER)
        return role
    }

    private Post createFilledPost(String id) {
        Post post = new Post()
        post.setId(id)
        return post
    }
}
