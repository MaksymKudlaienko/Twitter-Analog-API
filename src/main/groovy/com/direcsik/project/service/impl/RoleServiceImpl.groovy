package com.direcsik.project.service.impl

import com.direcsik.project.entity.ERole
import com.direcsik.project.entity.Role
import com.direcsik.project.exception.RoleNotFoundException
import com.direcsik.project.repository.RoleRepository
import com.direcsik.project.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository

    @Override
    Role getUserRole() {
        return roleRepository.findByName(ERole.ROLE_TRUSTED_USER.toString()).orElseThrow(() ->
                new RoleNotFoundException(String.format('Role with name %s not found', ERole.ROLE_TRUSTED_USER.toString())))
    }
}
