package com.example.project.dto.user;

import com.example.project.model.ERole;

import java.util.Set;

public class SignInResponseDTO {

    private String status;
    private Set<ERole> role;

    public SignInResponseDTO(String status, Set<ERole> role) {
        this.status = status;
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<ERole> getRole() {
        return role;
    }

    public void setRole(Set<ERole> role) {
        this.role = role;
    }
}
