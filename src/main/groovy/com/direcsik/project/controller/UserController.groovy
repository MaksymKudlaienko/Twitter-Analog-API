package com.direcsik.project.controller

import com.direcsik.project.entity.dto.UserDto
import com.direcsik.project.facade.UserFacade
import com.direcsik.project.security.UserPrinciple
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('api/v1/users')
class UserController {

    @Autowired
    private UserFacade userFacade

    @PostMapping
    ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body('Invalid password or email formats')
        }
        UserDto user = userFacade.createUser(userDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }

    @PostMapping('/login')
    ResponseEntity<?> login(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body('Invalid password or email formats')
        }
        String token = userFacade.createTokenForUser(userDto.getUsername(), userDto.getPassword())
        return ResponseEntity.ok(token)
    }

    @PatchMapping
    ResponseEntity<?> updateUserPassword(@Valid @RequestBody @NotEmpty String password,
                                         @AuthenticationPrincipal UserPrinciple principle,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body('Password cannot be empty')
        }
        UserDto user = userFacade.updateUserPassword(password, principle)
        return ResponseEntity.ok(user)
    }

    @DeleteMapping('/{userId}')
    ResponseEntity<?> removeUser(@PathVariable('userId') String userId,
                                 @AuthenticationPrincipal UserPrinciple principle) {
        if (userId != principle.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body('Access denied');
        }
        userFacade.removeUser(userId)
        return ResponseEntity.ok().build()
    }

    @PostMapping('/{userId}/subscribe')
    ResponseEntity<?> subscribe(@PathVariable('userId') String userId,
                                @AuthenticationPrincipal UserPrinciple principle) {
        if (userId == principle.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body('Cannot subscribe on yourself')
        }
        UserDto user = userFacade.subscribe(principle.getId(), userId)
        return ResponseEntity.ok(user)
    }

    @PostMapping('/{userId}/unsubscribe')
    ResponseEntity<?> unsubscribe(@PathVariable('userId') String userId,
                                  @AuthenticationPrincipal UserPrinciple principle) {
        if (userId == principle.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body('Cannot unsubscribe from yourself')
        }
        userFacade.unsubscribe(principle.getId(), userId)
        return ResponseEntity.ok().build()
    }

    @GetMapping('/{userId}/feed')
    ResponseEntity<?> getUserFeed(@PathVariable('userId') String userId,
                                  @AuthenticationPrincipal UserPrinciple principle) {
        if (userId == principle.getId()) {
            return ResponseEntity.ok(userFacade.getCurrentUserFeed(userId))
        } else {
            return ResponseEntity.ok(userFacade.getUserFeed(userId))
        }
    }
}
