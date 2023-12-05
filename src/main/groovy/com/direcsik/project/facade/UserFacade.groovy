package com.direcsik.project.facade

import com.direcsik.project.entity.dto.PostDto
import com.direcsik.project.entity.dto.UserDto
import com.direcsik.project.security.UserPrinciple

interface UserFacade {

    UserDto createUser(UserDto userDto)

    String createTokenForUser(String username, String password)

    UserDto updateUserPassword(String newPassword, UserPrinciple principle)

    void removeUser(String userId)

    UserDto subscribe(String sourceId, String targetId)

    void unsubscribe(String sourceId, String targetId)

    List<PostDto> getCurrentUserFeed(String userId)

    List<PostDto> getUserFeed(String userId)
}
