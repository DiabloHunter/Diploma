package com.example.project.JWT.security.services;

import com.example.project.exceptions.UserEmailNotFoundException;
import com.example.project.model.User;
import com.example.project.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;

    @Transactional
    public UserDetails loadUserByUserEmail(String email) throws UserEmailNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User Not Found with email: " + email));

        return UserDetailsImpl.build(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
