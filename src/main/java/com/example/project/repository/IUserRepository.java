package com.example.project.repository;


import com.example.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    Boolean existsByEmail(String userEmail);

}
