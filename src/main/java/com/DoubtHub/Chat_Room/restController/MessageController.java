package com.DoubtHub.Chat_Room.restController;

import com.DoubtHub.Chat_Room.model.Message;
import com.DoubtHub.Chat_Room.service.MembershipService;
import com.DoubtHub.Chat_Room.service.MessageService;
import com.DoubtHub.Chat_Room.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;
    private final MembershipService membershipService;
    private final UserService userService;

    public MessageController(MessageService messageService, MembershipService membershipService, UserService userService) {
        this.messageService = messageService;
        this.membershipService = membershipService;
        this.userService = userService;
    }

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('STUDENT', 'MENTOR')")
    public ResponseEntity<Message> sendMessage(
            @RequestParam UUID chatRoomId,
            @RequestParam UUID sendById,
            @RequestParam String content) {

        boolean isAllowed = membershipService.isUserInRoom(chatRoomId, sendById);
        if (!isAllowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Message message = messageService.postMessage(chatRoomId, sendById, content);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/chatRoom/{chatRoomId}/messages")
    @PreAuthorize("hasAnyRole('STUDENT','MENTOR','ADMIN')")
    public ResponseEntity<Page<Message>> getMessagesForChatRoom(
            @PathVariable UUID chatRoomId,
            Pageable pageable) {

        Page<Message> messages = messageService.getMessagesByChatRoom(chatRoomId, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/user/{userId}/messages")
    @PreAuthorize("hasAnyRole('STUDENT','MENTOR','ADMIN')")
    public ResponseEntity<Page<Message>> getAllMessagesByUser(
            @PathVariable UUID userId,
            Pageable pageable) {

        Page<Message> messages = messageService.getMessagesByUser(userId, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/chatRoom/{chatRoomId}/user-messages")
    @PreAuthorize("hasAnyRole('STUDENT','MENTOR','ADMIN')")
    public ResponseEntity<Page<Message>> getMessagesByUserForChatRoom(
            @PathVariable UUID chatRoomId,
            @RequestParam UUID userId,
            Pageable pageable) {

        Page<Message> messages = messageService.getMessagesByUserInChatRoom(chatRoomId, userId, pageable);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','MENTOR','ADMIN')")
    public ResponseEntity<Void> deleteMessageByUser(@PathVariable UUID id, @RequestParam UUID requesterId) {
        boolean isOwner = messageService.isOwnerOfMessage(id, requesterId);
        boolean isAdmin = userService.isAdmin(requesterId);

        if (isOwner || isAdmin) {
            messageService.deleteMessageByUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
