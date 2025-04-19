package com.DoubtHub.Chat_Room.restController;

import com.DoubtHub.Chat_Room.model.ChatRoom;
import com.DoubtHub.Chat_Room.model.User;
import com.DoubtHub.Chat_Room.service.ChatRoomService;
import com.DoubtHub.Chat_Room.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/chatRoom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    public ChatRoomController(ChatRoomService chatRoomService, UserService userService) {
        this.chatRoomService = chatRoomService;
        this.userService = userService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam String name, @RequestParam UUID createdById){
        User createdBy = userService.getUserById(createdById);
        ChatRoom chatRoom = chatRoomService.createChatRoom(name,createdBy);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable UUID id){
        ChatRoom chatRoom = chatRoomService.getChatRoom(id);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ChatRoom>> getAllRoomsForAdmin() {
        List<ChatRoom> rooms = chatRoomService.getAllChatRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/myRooms/{userId}")
    @PreAuthorize("hasAnyRole('MENTOR','STUDENT')")
    public ResponseEntity<List<ChatRoom>> getRoomsForMentor(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        List<ChatRoom> rooms = chatRoomService.getRoomsForUser(user);
        return ResponseEntity.ok(rooms);
    }

//    @GetMapping("/student/{studentId}")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<List<ChatRoom>> getRoomsForStudent(@PathVariable UUID studentId) {
//        User student = userService.getUserById(studentId);
//        List<ChatRoom> rooms = chatRoomService.getRoomsForUser(student);
//        return ResponseEntity.ok(rooms);
//    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable UUID id) {
        chatRoomService.deleteChatRoom(id);
        return ResponseEntity.noContent().build();
    }

}
