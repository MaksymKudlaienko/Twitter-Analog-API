package com.direcsik.project.service.impl

import com.direcsik.project.entity.ERole
import com.direcsik.project.entity.Role
import com.direcsik.project.exception.RoleNotFoundException
import com.direcsik.project.repository.RoleRepository
import com.direcsik.project.service.RoleService
import spock.lang.Specification

class RoleServiceImplTest extends Specification {

    private RoleRepository roleRepository = Mock()
    private RoleService roleService

    def setup() {
        roleService = new RoleServiceImpl(roleRepository: roleRepository)
    }

    def 'should find role by name'() {
        given:
        Role role = new Role()

        roleRepository.findByName(ERole.ROLE_TRUSTED_USER.toString()) >> Optional.of(role)

        when:
        Role result = roleService.getUserRole()

        then:
        result == role
    }

    def 'should throw exception if role not found by name'() {
        given:
        roleRepository.findByName(ERole.ROLE_TRUSTED_USER.toString()) >> Optional.empty()

        when:
        roleService.getUserRole()

        then:
        thrown(RoleNotFoundException)
    }
}
