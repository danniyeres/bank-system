package org.example.accountservice.controller;

import org.example.accountservice.model.Account;
import org.example.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String hello() {
        return """
                
                http://localhost:8080/account/create?userId=1 (POST)
                // Create account
                {
                    "id": 1,
                    "userId": 1,
                    "balance": 0
                }
                
                http://localhost:8080/account/get/userId/1 (GET)
                // Get account by user id
                {
                    "id": 1,
                    "userId": 1,
                    "balance": 0
                }
                
                http://localhost:8080/account/get/username/username (GET)
                // Get account by username
                {
                    "id": 1,
                    "userId": 1,
                    "balance": 0
                }
                
                http://localhost:8080/account/get/id/1 (GET)
                // Get account by id
                {
                    "id": 1,
                    "userId": 1,
                    "balance": 0
                }
                
                
                http://localhost:8080/account/deposit/1?amount=100 (POST)
                // Deposit money
                {
                    "id": 1,
                    "userId": 1,
                    "balance": 100
                }
                
                http://localhost:8080/account/withdraw/1?amount=50 (POST)
                // Withdraw money
                {
                    "id": 1,
                    "userId": 1,
                    "balance": 50
                }
                
                """;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestParam Long userId) {
        try {
            return ResponseEntity.ok(accountService.createAccount(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }

    @GetMapping("/get/userId/{userId}")
    public ResponseEntity<?> getAccountByUserId(@PathVariable Long userId) {
        try {
            Account account = accountService.getAccountByUserId(userId);
            return account != null ? ResponseEntity.ok(account) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }

    @GetMapping("/get/username/{username}")
    public ResponseEntity<?> getAccountByUsername(@PathVariable String username) {
        try {
            Account account = accountService.getAccount(username);
            return account != null ? ResponseEntity.ok(account) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        try {
            Account account = accountService.getAccountById(id);
            return account != null ? ResponseEntity.ok(account) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<?> deposit(@PathVariable Long accountId, @RequestParam double amount) {
        try {
            accountService.deposit(accountId, amount);
            return ResponseEntity.ok("Deposit successful amount: " + amount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<?> withdraw(@PathVariable Long accountId, @RequestParam double amount) {
        try {
            accountService.withdraw(accountId, amount);
            return ResponseEntity.ok("Withdraw successful amount: " + amount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error server: " + e.getMessage());
        }
    }
}
