package com.DoubtHub.Chat_Room.repository;

import com.DoubtHub.Chat_Room.model.ChatRoom;
import com.DoubtHub.Chat_Room.model.Membership;
import com.DoubtHub.Chat_Room.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    List<Membership> findByUser(User user);
    List<User> findUsersByChatRoom(ChatRoom chatRoom);
    Optional<Membership> findByChatRoomAndUser(ChatRoom room, User user);
    boolean existsByChatRoomAndUser(ChatRoom room, User user);
    List<Membership> findByChatRoom(ChatRoom chatRoom);

}
