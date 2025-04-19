package com.DoubtHub.Chat_Room.service;

import com.DoubtHub.Chat_Room.model.ChatRoom;
import com.DoubtHub.Chat_Room.model.MemberRole;
import com.DoubtHub.Chat_Room.model.Membership;
import com.DoubtHub.Chat_Room.model.User;
import com.DoubtHub.Chat_Room.repository.MembershipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;


    public MembershipService(MembershipRepository membershipRepository, ChatRoomService chatRoomService, UserService userService) {
        this.membershipRepository = membershipRepository;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
    }

    public Membership assignUserToRoom(UUID chatRoomId, UUID userId, MemberRole role){
        ChatRoom room = chatRoomService.getChatRoom(chatRoomId);
        User user = userService.getUserById(userId);

        Membership membership = new Membership();
        membership.setChatRoom(room);
        membership.setUser(user);
        membership.setRole(role);
        return membershipRepository.save(membership);
    }

    public void revokeUserFromRoom(UUID chatRoomId, UUID userId){
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        User user = userService.getUserById(userId);

        Membership membership = membershipRepository.findByChatRoomAndUser(chatRoom,user)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found."));
        membershipRepository.delete(membership);

    }

    public boolean isUserInRoom(UUID chatRoomId, UUID userId) {
        ChatRoom room = chatRoomService.getChatRoom(chatRoomId);
        User user = userService.getUserById(userId);
        return membershipRepository.existsByChatRoomAndUser(room, user);
    }

    public List<User> getAllMembers(UUID chatRoomId) {
        ChatRoom room = chatRoomService.getChatRoom(chatRoomId);
        return membershipRepository.findUsersByChatRoom(room);
    }

    public Set<ChatRoom> getChatRoomsForUser(User user) {
        return membershipRepository.findByUser(user)
                .stream()
                .map(Membership::getChatRoom)
                .collect(Collectors.toSet());
    }

    public Page<Membership> getAllMemberships(Pageable pageable) {
        return membershipRepository.findAll(pageable);
    }

}
