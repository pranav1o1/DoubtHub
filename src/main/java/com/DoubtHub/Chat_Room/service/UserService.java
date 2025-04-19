package com.DoubtHub.Chat_Room.service;

import com.DoubtHub.Chat_Room.model.Role;
import com.DoubtHub.Chat_Room.model.User;
import com.DoubtHub.Chat_Room.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isAdmin(UUID requesterId) {
        return userRepository.findById(requesterId)
                .map(user -> user.getRole() == Role.ADMIN)
                .orElse(false);
    }

    //for registration
    public User registerUser(String name, String emailId, String password, Role role){
        if (role == Role.ADMIN) {
            throw new IllegalArgumentException("Registration as ADMIN is not allowed.");
        }
        return internalCreateUser(name, emailId, password, role);
    }

    // Admin-only
    public User createUserByAdmin(String name, String emailId, String password, Role role){
        return internalCreateUser(name, emailId, password, role);
    }

    // Shared internal logic
    private User internalCreateUser(String name, String emailId, String password, Role role) {
        User user = new User();
        user.setName(name);
        user.setEmailId(emailId);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }
//    public User createUser(String name, String emailId, String password, Role role){
//        User user = new User();
//        user.setName(name);
//        user.setEmailId(emailId);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole(role);
//        return userRepository.save(user);
//    }

    public User getUserById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    public User getUserByNameOrEmail(String query){
        return userRepository.findByNameIgnoreCaseOrEmailIdIgnoreCase(query, query)
                .orElseThrow(()-> new IllegalArgumentException("User not found with name or email: " + query));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(UUID id){
        if(!userRepository.existsById(id)){
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User updateUser(UUID id, String name, String password, Role role){
        User user = new User();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmailIdIgnoreCase(email);
    }

    public Page<User> getUsersPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
