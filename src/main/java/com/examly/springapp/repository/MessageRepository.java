package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.examly.springapp.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderId(Long senderId);
    List<Message> findByReceiverId(Long receiverId);
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userAId AND m.receiver.id = :userBId) OR (m.sender.id = :userBId AND m.receiver.id = :userAId) ORDER BY m.createdAt")
List<Message> findConversationBetweenUsers(@Param("userAId") Long userAId, @Param("userBId") Long userBId);

}
