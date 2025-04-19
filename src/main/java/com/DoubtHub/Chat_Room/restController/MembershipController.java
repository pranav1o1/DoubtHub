package com.DoubtHub.Chat_Room.restController;

import com.DoubtHub.Chat_Room.model.*;
import com.DoubtHub.Chat_Room.service.ChatRoomService;
import com.DoubtHub.Chat_Room.service.MembershipService;
import com.DoubtHub.Chat_Room.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/membership")
public class MembershipController {
    private final MembershipService membershipService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    public MembershipController(MembershipService membershipService, UserService userService, ChatRoomService chatRoomService) {
        this.membershipService = membershipService;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Membership> addMembership(@RequestParam UUID userId, @RequestParam UUID chatRoomId, @RequestParam MemberRole role){
//        User user = userService.getUserById(userId);
//        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        Membership membership = membershipService.assignUserToRoom(userId,chatRoomId,role);
        return ResponseEntity.status(HttpStatus.CREATED).body(membership);
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMembership(@RequestParam UUID chatRoomId, @RequestParam UUID userId){
        membershipService.revokeUserFromRoom(chatRoomId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chatRoom/{id}/members")
    public ResponseEntity<List<User>> getUsersInChatRoom(@PathVariable UUID chatRoomId){
        List<User> members = membershipService.getAllMembers(chatRoomId);
        return ResponseEntity.ok(members);
    }

}
