package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import com.example.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for business logic related to messages.
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new message after validating its content and user existence.
     */
    public Optional<Message> createMessage(Message message) {
        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return Optional.empty();
        }

        // Validate that the posting user exists
        if (!accountRepository.existsById(message.getPostedBy())) {
            return Optional.empty();
        }

        // Check if the user trying to post the message exists
        // If not, throw an exception that gets handled globally
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new ResourceNotFoundException("Account with ID " + message.getPostedBy() + " does not exist.");
        }

        // Save message to database
        Message saved = messageRepository.save(message);
        return Optional.of(saved);
    }

    /**
     * Retrieves all messages from the database.
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieves a message by its unique ID.
     */
    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }
    
    /**
     * Deletes a message by its ID.
     * Returns 1 if a message was deleted, or 0 if it didn't exist.
     */
    public int deleteMessageById(Integer id) {
        boolean exists = messageRepository.existsById(id);
        if (exists) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    /**
     * Updates the message text for a given message ID.
     * Returns 1 if successful, 0 if the message doesn't exist or input is invalid.
     */
    public int updateMessageText(Integer id, String newText) {
        Optional<Message> maybeMessage = messageRepository.findById(id);

        // Validate existence and text length
        if (maybeMessage.isEmpty() || newText == null || newText.isBlank() || newText.length() > 255) {
            return 0;
        }

        // Update and save
        Message message = maybeMessage.get();
        message.setMessageText(newText);
        messageRepository.save(message);
        return 1;
    }

    /**
     * Retrieves all messages posted by a specific user.
     */
    public List<Message> getMessagesByUserId(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }   
}
//test to commit