package com.DoubtHub.Chat_Room.repository;

import com.DoubtHub.Chat_Room.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
}
