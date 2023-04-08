package com.example.project.dto.user;

import com.example.project.model.ERole;

public class SignInResponseDTO {

    private String status;
    private ERole role;

    public SignInResponseDTO(String status, ERole role) {
        this.status = status;
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }
}
