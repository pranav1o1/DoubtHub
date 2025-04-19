package com.DoubtHub.Chat_Room.service;

import com.DoubtHub.Chat_Room.model.ChatRoom;
import com.DoubtHub.Chat_Room.model.Message;
import com.DoubtHub.Chat_Room.model.User;
import com.DoubtHub.Chat_Room.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, ChatRoomService chatRoomService, UserService userService) {
        this.messageRepository = messageRepository;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
    }

    public Message postMessage(UUID roomId, UUID userId, String content) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);
        User sender = userService.getUserById(userId);

        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setContent(content);
        message.setSendAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public Page<Message> getMessagesByChatRoom(UUID chatRoomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        return messageRepository.findByChatRoomOrderBySendAtAsc(chatRoom, pageable);
    }

    public Page<Message> getMessagesByUser(UUID userId, Pageable pageable) {
        User sender = userService.getUserById(userId);
        return messageRepository.findBySenderOrderBySendAtDesc(sender, pageable);
    }

    public Page<Message> getMessagesByUserInChatRoom(UUID chatRoomId, UUID userId, Pageable pageable){
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        User sender = userService.getUserById(userId);
        return messageRepository.findByChatRoomAndSenderOrderBySendAtDesc(chatRoom, sender, pageable);
    }

    public void deleteMessageByUser(UUID messageId){
        if(!messageRepository.existsById(messageId)){
            throw new IllegalArgumentException("Message not found with id: " + messageId);
        }
        messageRepository.deleteById(messageId);
    }

    public boolean isOwnerOfMessage(UUID messageId, UUID userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        return message.getSender().getId().equals(userId);
    }

}

