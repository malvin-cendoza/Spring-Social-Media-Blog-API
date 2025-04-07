package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer responsible for business logic related to user accounts.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Handles user registration by validating input and saving new account.
     * Returns an Optional containing the new account if successful.
     */
    public Optional<Account> register(Account account) {
        // Username must not be blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return Optional.empty();
        }

        // Password must be at least 4 characters long
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return Optional.empty();
        }

        // Username must be unique
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            return Optional.ofNullable(null); // Will be interpreted as conflict in controller
        }

        // Save new account to database
        Account saved = accountRepository.save(account);
        return Optional.of(saved);
    }

    /**
     * Handles user login by verifying username and password.
     * Returns an Optional containing the account if credentials match.
     */
    public Optional<Account> login(Account account) {
        return accountRepository.findByUsernameAndPassword(
                account.getUsername(),
                account.getPassword()
        );
    }
}