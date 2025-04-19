package com.DoubtHub.Chat_Room.repository;

import com.DoubtHub.Chat_Room.model.ChatRoom;
import com.DoubtHub.Chat_Room.model.Message;
import com.DoubtHub.Chat_Room.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChatRoom_Id(UUID chatRoomId);
    Page<Message> findByChatRoomOrderBySendAtAsc(ChatRoom chatRoom, Pageable pageable);
    Page<Message> findBySenderOrderBySendAtDesc(User sender, Pageable pageable);
    Page<Message> findByChatRoomAndSenderOrderBySendAtDesc(ChatRoom chatRoom, User sender, Pageable pageable);
}
