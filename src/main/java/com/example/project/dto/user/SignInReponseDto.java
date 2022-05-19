package com.example.project.dto.user;

import com.example.project.model.UserRoles;

public class SignInReponseDto {

    private String status;
    private String token;
    private UserRoles role;

    public SignInReponseDto(String status, String token, UserRoles role) {
        this.status = status;
        this.token = token;
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}
