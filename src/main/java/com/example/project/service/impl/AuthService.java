package com.example.project.service.impl;

import com.example.project.JWT.payload.request.LoginRequest;
import com.example.project.JWT.payload.request.SignupRequest;
import com.example.project.JWT.payload.response.JwtResponse;
import com.example.project.JWT.payload.response.MessageResponse;
import com.example.project.JWT.security.jwt.JwtUtils;
import com.example.project.JWT.security.services.UserDetailsImpl;
import com.example.project.model.ERole;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.service.IAuthService;
import com.example.project.service.IRoleService;
import com.example.project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthService implements IAuthService {

    private static Random random = new Random();

    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<JwtResponse> signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()).get(0);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                role, userDetails.getUsername()));
    }

    @Transactional
    public void signUp(SignupRequest signUpRequest) {
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw  new IllegalArgumentException("Email is already in use!");
        }

        String strRole = signUpRequest.getRole();
        Role role;

        if (strRole == null) {
            role = roleService.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new NotFoundException("Error: Role was not found."));
        } else {
            switch (strRole) {
                case "admin":
                    role = roleService.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new NotFoundException("Error: Role was not found."));
                    break;
                case "manager":
                    role = roleService.findByName(ERole.ROLE_MANAGER)
                            .orElseThrow(() -> new NotFoundException("Error: Role was not found."));
                    break;
                case "cashier":
                    role = roleService.findByName(ERole.ROLE_CASHIER)
                            .orElseThrow(() -> new NotFoundException("Error: Role was not found."));
                    break;
                default:
                    role = roleService.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new NotFoundException("Error: Role was not found."));
            }
        }

        User user = new User(signUpRequest.getName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                0.0,
                role);

        userService.create(user);
    }
}
