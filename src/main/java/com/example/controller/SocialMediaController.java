/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class that handles all incoming HTTP requests related to user accounts and messages.
 * Defines REST API endpoints for registration, login, message creation, message retrieval, update, and deletion.
 * ..
 */
@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * Endpoint to register a new user account.
     * @param account JSON body representing a new account (without accountId).
     * @return 200 OK with the new account, 409 if username already exists, or 400 if input is invalid. 
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Optional<Account> result = accountService.register(account);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else if (accountService.login(account).isPresent()) {
            return ResponseEntity.status(409).build(); // Username conflict
        } else {
            return ResponseEntity.badRequest().build(); // Invalid input
        }
    }

    /**
     * Endpoint to authenticate a user.
     * @param account JSON body containing username and password.
     * @return 200 OK with the account if login is successful, 401 Unauthorized if not.
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Optional<Account> result = accountService.login(account);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * Endpoint to create a new message.
     * @param message JSON body containing postedBy, messageText, and timePostedEpoch.
     * @return 200 OK with the new message if successful, 400 if input is invalid.
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Optional<Message> result = messageService.createMessage(message);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint to retrieve all messages.
     * @return A list of all messages in the database (200 OK always, even if empty).
     */
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    /**
     * Endpoint to retrieve a single message by its ID.
     * @param messageId The ID of the message.
     * @return 200 OK with the message if found, otherwise an empty 200 OK response.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> result = messageService.getMessageById(messageId);

        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    /**
     * Endpoint to delete a message by ID.
     * @param messageId The ID of the message.
     * @return 200 OK with 1 if deleted, or empty 200 OK if message did not exist.
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        int deleted = messageService.deleteMessageById(messageId);

        if (deleted == 1) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().build(); // still 200, no content
        }
    }

    /**
     * Endpoint to update the text of a message.
     * @param messageId The ID of the message to update.
     * @param updated A JSON object containing only the new messageText.
     * @return 200 OK with 1 if updated, 400 if update failed.
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageText(@PathVariable Integer messageId, @RequestBody Message updated) {
        int updatedCount = messageService.updateMessageText(messageId, updated.getMessageText());

        if (updatedCount == 1) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint to get all messages posted by a specific user.
     * @param accountId The ID of the account.
     * @return A list of messages posted by the user (empty list if none exist).
     */
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable Integer accountId) {
        return messageService.getMessagesByUserId(accountId);
    }
}