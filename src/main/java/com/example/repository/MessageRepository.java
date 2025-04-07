package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for accessing Message data from the database.
 * Uses Spring Data JPA to automatically generate queries.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByPostedBy(Integer accountId);
}