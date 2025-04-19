package com.DoubtHub.Chat_Room.service;

import com.DoubtHub.Chat_Room.model.*;
import com.DoubtHub.Chat_Room.repository.ChatRoomRepository;
import com.DoubtHub.Chat_Room.repository.MembershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    private final MembershipRepository membershipRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository,
                           MembershipRepository membershipRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.membershipRepository = membershipRepository;
    }

    public ChatRoom createChatRoom(String name, User createdBy){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(name);
        chatRoom.setCreatedBy(createdBy);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoom(UUID id){
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with id: " + id));
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public List<ChatRoom> getRoomsForUser(User user){
        List<Membership> memberships = membershipRepository.findByUser(user);
                return memberships.stream()
                        .map(Membership::getChatRoom)
                        .distinct()
                        .collect(Collectors.toList());
    }

    public void deleteChatRoom(UUID id){
        if(!chatRoomRepository.existsById(id)){
            throw new IllegalArgumentException("ChatRoom not found with id: " + id);
        }
        chatRoomRepository.deleteById(id);
    }

    public void addUserToChatRoom(UUID chatRoomId, User user, MemberRole role) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        Membership membership = new Membership();
        membership.setChatRoom(chatRoom);
        membership.setUser(user);
        membership.setRole(role);  // Could be STUDENT or MENTOR

        membershipRepository.save(membership);
    }


    public Set<User> getPermittedUsers(ChatRoom chatRoom) {
        return membershipRepository.findByChatRoom(chatRoom)
                .stream()
                .map(Membership::getUser)
                .collect(Collectors.toSet());
    }

    public Set<User> getMentors(ChatRoom chatRoom){
        return membershipRepository.findByChatRoom(chatRoom)
                .stream()
                .filter(m -> m.getRole() == MemberRole.MENTOR)  // Use MemberRole enum here
                .map(Membership::getUser)
                .collect(Collectors.toSet());
    }

    public Set<User> getStudents(ChatRoom chatRoom){
        return membershipRepository.findByChatRoom(chatRoom)
                .stream()
                .filter(m -> m.getRole() == MemberRole.STUDENT)
                .map(Membership::getUser)
                .collect(Collectors.toSet());
    }


//    public Set<User> getMentors(ChatRoom chatRoom){
//        return membershipRepository.findByChatRoom(chatRoom)
//                .stream()
//                .filter(m->m.getRole() == Role.MENTOR)
//                .map(Membership::getUser)
//                .collect(Collectors.toSet());
//    }
//
//    public Set<User> getStudents(ChatRoom chatRoom){
//        return membershipRepository.findByChatRoom(chatRoom)
//                .stream()
//                .filter(m->m.getRole() == Role.STUDENT)
//                .map(Membership::getUser)
//                .collect(Collectors.toSet());
//    }

}


//    public void addUserToChatRoom(UUID id, User user){
//        if(!chatRoomRepository.existsById(id)){
//            throw new IllegalArgumentException("ChatRoom not found with id: " + id);
//        }
//        ChatRoom chatRoom = getChatRoom(id);
//        chatRoom.getPermittedUsers().add(user);
//        chatRoomRepository.save(chatRoom);
//    }