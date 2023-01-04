package com.example.project.service.impl;

import com.example.project.JWT.payload.request.LoginRequest;
import com.example.project.JWT.payload.request.SignupRequest;
import com.example.project.JWT.payload.response.JwtResponse;
import com.example.project.JWT.payload.response.MessageResponse;
import com.example.project.JWT.security.jwt.JwtUtils;
import com.example.project.JWT.security.services.UserDetailsImpl;
import com.example.project.exceptions.CustomException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService implements IAuthService {

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
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles));
    }


//    @Override
//    public SignInResponseDTO signInMob(SignInDTO signInDto) {
//        String userEmail = signInDto.getEmail();
//        User user = userRepository.findByEmail(signInDto.getEmail())
//                .orElseThrow(() -> new CustomException("User Not Found with email: " + userEmail));
//        if (Objects.isNull(user)) {
//            return new SignInResponseDTO("fail", null);
//        }
//        try {
//            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
//                return new SignInResponseDTO("fail", null);
//            }
//        } catch (NoSuchAlgorithmException e) {
//            return new SignInResponseDTO("fail", null);
//        }
//
//        return new SignInResponseDTO("success", /*user.getRoles()*/ null);
//    }

    @Transactional
    public ResponseEntity<MessageResponse> signUp(SignupRequest signUpRequest) {

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleService.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleService.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "moderator":
                        Role moderatorRole = roleService.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(moderatorRole);
                        break;
                    case "manager":
                        Role managerRole = roleService.findByName(ERole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(managerRole);
                        break;
                    case "cashier":
                        Role cashierRole = roleService.findByName(ERole.ROLE_CASHIER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(cashierRole);
                        break;
                    default:
                        Role userRole = roleService.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                0.0,
                roles);

        if (userService.existsByEmail(user.getEmail())) {
            throw new CustomException("user already present");
        }
        userService.create(user);

        return ResponseEntity.ok(new MessageResponse("User successfully created!"));
    }
}
