package com.examly.springapp.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examly.springapp.model.Connection;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.ConnectionRepository;
import com.examly.springapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public Connection sendRequest(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        Connection connection = new Connection();
        connection.setSender(sender);
        connection.setReceiver(receiver);
        connection.setStatus(Connection.Status.PENDING);
        connection.setCreatedAt(LocalDateTime.now());

        return connectionRepository.save(connection);
    }

    public Connection respondToRequest(Long connectionId, Connection.Status status) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("Connection not found"));
        connection.setStatus(status);

        return connectionRepository.save(connection);
    }

    // UPDATED: show all accepted connections where user is sender or receiver
    public List<Connection> getUserConnections(Long userId) {
        return connectionRepository.findBySenderIdOrReceiverIdAndStatus(
            userId, userId, Connection.Status.ACCEPTED
        );
    }

    // Pending requests are only those where user is receiver of a new request
    public List<Connection> getPendingRequests(Long userId) {
        return connectionRepository.findByReceiverIdAndStatus(userId, Connection.Status.PENDING);
    }

    public void disconnect(Long connectionId) {
        connectionRepository.deleteById(connectionId);
    }
}
