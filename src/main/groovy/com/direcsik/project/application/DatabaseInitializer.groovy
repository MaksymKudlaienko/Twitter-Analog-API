package com.direcsik.project.application

import com.direcsik.project.entity.ERole
import com.direcsik.project.entity.Role
import com.direcsik.project.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer {

    @Autowired
    private RoleRepository roleRepository

    @EventListener(ApplicationReadyEvent.class)
    private void runAfterStartup() {
        ERole[] roleNames = ERole.class.getEnumConstants()
        for (ERole roleName in roleNames) {
            if (roleRepository.findByName(roleName.name()).isEmpty()) {
                roleRepository.save(new Role(name: roleName))
            }
        }
    }
}
