package com.example.project.service;

import com.example.project.model.ERole;
import com.example.project.model.Role;

import java.util.Optional;

public interface IRoleService {
    Role findByName(String roleName);

    Optional<Role> findByName(ERole roleName);
}
