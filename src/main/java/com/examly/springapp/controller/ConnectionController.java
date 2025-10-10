package com.examly.springapp.controller;
import com.examly.springapp.model.Connection;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.service.ConnectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @PostMapping("/send")
    public ResponseEntity<Connection> sendRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        Connection connection = connectionService.sendRequest(senderId, receiverId);
        return ResponseEntity.ok(connection);
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<Connection> respond(@PathVariable Long id, @RequestParam Connection.Status status) {
        Connection updated = connectionService.respondToRequest(id, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Connection>> listConnections(@RequestParam Long userId) {
        List<Connection> connections = connectionService.getUserConnections(userId);
        return ResponseEntity.ok(connections);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Connection>> pendingRequests(@RequestParam Long userId) {
        List<Connection> requests = connectionService.getPendingRequests(userId);
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/{id}/disconnect")
    public ResponseEntity<Void> disconnect(@PathVariable Long id) {
        connectionService.disconnect(id);
        return ResponseEntity.noContent().build();
    }
}
