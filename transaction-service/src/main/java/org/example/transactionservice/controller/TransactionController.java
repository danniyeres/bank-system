package org.example.transactionservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.transactionservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public String hello(){
        return """
                
                http://localhost:8080/transaction/transfer?fromAccountId=1&toAccountId=2&amount=100 (POST)
                // Transfer money
                {
                    "fromAccountId": 1,
                    "toAccountId": 2,
                    "amount": 100
                }
                
                http://localhost:8080/transaction/deposit?accountId=1&amount=100 (POST)
                // Deposit money
                {
                    "accountId": 1,
                    "amount": 100
                }
                
                http://localhost:8080/transaction/withdraw?accountId=1&amount=100 (POST)
                // Withdraw money
                {
                    "accountId": 1,
                    "amount": 100
                }
                
                """;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam double amount) {
        try {
            transactionService.transfer(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error server: " + e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long accountId, @RequestParam double amount) {
        try {
            transactionService.deposit(accountId, amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error server: " + e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam Long accountId, @RequestParam double amount) {
        try {
            transactionService.withdraw(accountId, amount);
            return ResponseEntity.ok("Withdraw successful");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error server: " + e.getMessage());
        }
    }
}

