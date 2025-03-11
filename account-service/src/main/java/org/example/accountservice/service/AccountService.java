package org.example.accountservice.service;

import lombok.extern.slf4j.Slf4j;
import org.example.accountservice.feign.UserClient;
import org.example.accountservice.model.Account;
import org.example.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CardService cardService;
    private final UserClient userClient;

    public AccountService(AccountRepository accountRepository, CardService cardService, UserClient userClient) {
        this.accountRepository = accountRepository;
        this.cardService = cardService;
        this.userClient = userClient;
    }

    public Account createAccount(Long userId) {
        if (accountRepository.existsByUserId(userId))
            throw new IllegalArgumentException("Account already exists for user with id: " + userId);

        var user = userClient.findById(userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        var account = Account.builder()
                .userId(userId)
                .username(user.getUsername())
                .balance(0)
                .build();
        accountRepository.save(account);
        var card = cardService.createCard(account);
        account.setCard(card);
        accountRepository.save(account);

        log.info("Account created: {}", account.getUsername());
        return account;
    }

    public Account getAccountByUserId(Long userId) {
        var account = accountRepository.findByUserId(userId);
        if (account == null) throw new IllegalArgumentException("Account not found");
        return account;
    }

    public Account getAccount(String username) {
        var account = accountRepository.findByUsername(username);
        if (account == null) throw new IllegalArgumentException("Account not found");
        return account;
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public void deposit(Long accountId, double amount) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        log.info("Deposited {} to account with id: {}", amount, accountId);
    }

    public void withdraw(Long accountId, double amount) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));

        if (account.getBalance() < amount)
            throw new IllegalArgumentException("Not enough funds for withdrawal.");

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        log.info("Withdrawn {} from account with id: {}", amount, accountId);
    }
}
