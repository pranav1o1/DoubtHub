package com.DoubtHub.Chat_Room.restController;

import com.DoubtHub.Chat_Room.model.Role;
import com.DoubtHub.Chat_Room.model.User;
import com.DoubtHub.Chat_Room.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "User Service is up and running!";
    }

    @PostMapping("/register")
    public User registerUser(
                             @RequestParam String name,
                             @RequestParam String emailId,
                             @RequestParam String password,
                             @RequestParam Role role) {
        return userService.registerUser(name,emailId,password,role);
    }

    @PostMapping("/create")
    public User createUser(@RequestParam String name,
                             @RequestParam String emailId,
                             @RequestParam String password,
                             @RequestParam Role role) {
        return userService.createUserByAdmin(name, emailId, password, role);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id){
        return userService.getUserById(id);
    }

    @GetMapping("/search")
    public User getUserByNameOrEmail(@RequestParam String query){
        return userService.getUserByNameOrEmail(query);
    }

    @GetMapping("/all")
    public Page<User> getUsers(Pageable pageable){
        return userService.getUsersPage(pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable UUID id){
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id,
                           @RequestParam String name,
                           @RequestParam String password,
                           @RequestParam Role role){
        return userService.updateUser(id,name,password,role);
    }

    @GetMapping("/exists")
    public boolean checkEmail(@RequestParam String emailId){
        return userService.userExistsByEmail(emailId);
    }
}
