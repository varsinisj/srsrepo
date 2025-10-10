package com.examly.springapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.Connection;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findBySenderId(Long senderId);
    List<Connection> findByReceiverId(Long receiverId);
    List<Connection> findByStatus(Connection.Status status);
List<Connection> findBySenderIdOrReceiverIdAndStatus(Long senderId, Long receiverId, Connection.Status status);

    List<Connection> findByReceiverIdAndStatus(Long receiverId, Connection.Status status);

}
