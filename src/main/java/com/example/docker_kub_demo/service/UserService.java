package com.example.docker_kub_demo.service;

import com.example.docker_kub_demo.model.User;
import com.example.docker_kub_demo.repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // CREATE
    public User createUser(User user) {
        return userRepo.save(user);
    }

    // READ ALL
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // READ BY ID
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // UPDATE
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setActive(updatedUser.getActive());

        return userRepo.save(existingUser);
    }

    // DELETE
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepo.delete(user);
    }
}
