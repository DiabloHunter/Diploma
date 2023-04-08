package com.example.project.service.impl;

import com.example.project.model.ERole;
import com.example.project.model.Role;
import com.example.project.repository.IRoleRepository;
import com.example.project.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    IRoleRepository roleRepository;

    @Override
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }

    @Override
    public Optional<Role> findByName(ERole roleName) {
        return roleRepository.findByName(roleName);
    }
}
