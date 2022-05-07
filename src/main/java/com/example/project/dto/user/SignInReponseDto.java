package com.example.project.dto.user;

public class SignInReponseDto {

    private String status;
    private String token;
    private String email;

    public SignInReponseDto(String status, String token, String email) {
        this.status = status;
        this.token = token;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
