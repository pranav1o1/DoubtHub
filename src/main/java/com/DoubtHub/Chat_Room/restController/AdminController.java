package com.DoubtHub.Chat_Room.restController;

import com.DoubtHub.Chat_Room.model.Membership;
import com.DoubtHub.Chat_Room.model.User;
import com.DoubtHub.Chat_Room.service.MembershipService;
import com.DoubtHub.Chat_Room.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final MembershipService membershipService;

    public AdminController(UserService userService, MembershipService membershipService) {
        this.userService = userService;
        this.membershipService = membershipService;
    }

    //View all registered users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //View all memberships
    @GetMapping("/memberships")
    public ResponseEntity<Page<Membership>> getAllMemberships(Pageable pageable) {
        Page<Membership> memberships = membershipService.getAllMemberships(pageable);
        return ResponseEntity.ok(memberships);
    }

    //View user details
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
