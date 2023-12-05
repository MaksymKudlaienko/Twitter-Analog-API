package com.direcsik.project.service.impl

import com.direcsik.project.entity.Like
import com.direcsik.project.entity.Post
import com.direcsik.project.entity.User
import com.direcsik.project.exception.PostNotFoundException
import com.direcsik.project.repository.PostRepository
import com.direcsik.project.service.PostService
import spock.lang.Specification

class PostServiceImplTest extends Specification{

    private static final String POST_ID = 'post_id'
    private static final String POST_TEXT = 'post_text'
    private static final String UPDATED_POST_TEXT = 'updated_post_text'
    private static final String COMMENT_TEXT = 'comment_text'

    private PostRepository postRepository = Mock()
    private PostService postService

    def setup() {
        postService = new PostServiceImpl(postRepository: postRepository)
    }

    def 'should create a post'() {
        given:
        User user = new User()

        postRepository.findById(POST_ID) >> Optional.empty()

        when:
        postService.createPost(POST_TEXT, user)

        then:
        1 * postRepository.save(_ as Post)
    }

    def 'should find post by id'() {
        given:
        Post post = createFilledPost(POST_ID, POST_TEXT)

        postRepository.findById(POST_ID) >> Optional.of(post)

        when:
        Post result = postService.findById(POST_ID)

        then:
        result == post
    }

    def 'should throw exception if post not found by id'() {
        given:
        postRepository.findById(POST_ID) >> Optional.empty()

        when:
        postService.findById(POST_ID)

        then:
        thrown(PostNotFoundException.class)
    }

    def 'should update post text'() {
        given:
        Post post = createFilledPost(POST_ID, POST_TEXT)

        postRepository.findById(POST_ID) >> Optional.of(post)

        when:
        postService.updatePost(POST_ID, UPDATED_POST_TEXT)

        then:
        1 * postRepository.save(post)
    }

    def 'should remove post'() {
        given:
        Post post = createFilledPost(POST_ID, POST_TEXT)

        when:
        postService.removePost(post)

        then:
        1 * postRepository.delete(post)
    }

    def 'should add comment to post'() {
        given:
        Post post = createFilledPost(POST_ID, POST_TEXT)
        User user = new User()

        when:
        postService.addComment(COMMENT_TEXT, post, user)

        then:
        1 * postRepository.save(post)
    }

    def 'should add like to post if not exists'() {
        given:
        Post post = createFilledPost(POST_ID, POST_TEXT)
        User user = new User()

        when:
        postService.addLike(post, user)

        then:
        1 * postRepository.save(post)
    }

    def 'should not remove like if not exists'() {
        given:
        Post post = createFilledPost(POST_ID, POST_TEXT)
        User user = new User()

        when:
        postService.removeLike(post, user)

        then:
        0 * postRepository.save(post)
    }

    def 'should remove like if exists'() {
        given:
        Post post = createFilledPost(POST_ID, POST_TEXT)
        User user = new User()
        List<Like> likes = new ArrayList<>()
        Like like = new Like()

        like.setOwner(user)
        post.setLikes(likes)

        when:
        postService.removeLike(post, user)

        then:
        1 * postRepository.save(post)
    }

    private Post createFilledPost(String id, String text) {
        Post post = new Post()
        post.setId(id)
        post.setText(text)
        return post
    }
}
