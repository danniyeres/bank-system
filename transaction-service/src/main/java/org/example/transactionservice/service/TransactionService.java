package org.example.transactionservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.transactionservice.dto.AccountDto;
import org.example.transactionservice.dto.UserDto;
import org.example.transactionservice.feign.AccountClient;
import org.example.transactionservice.feign.UserClient;
import org.example.transactionservice.kafka.KafkaMessageProducer;
import org.example.transactionservice.model.Transaction;
import org.example.transactionservice.model.TransactionStatus;
import org.example.transactionservice.model.TransactionType;
import org.example.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final UserClient userClient;
    private final KafkaMessageProducer kafkaMessageProducer;

    @Transactional
    public void deposit(Long accountId, Double amount) {

        var account = toAccount(accountId);
        var user = toUser(account.getUserId());

        var transaction = Transaction.builder()
                .toAccountId(account.getId())
                .transactionType(TransactionType.DEPOSIT)
                .transactionStatus(TransactionStatus.PENDING)
                .transactionDate(LocalDateTime.now())
                .amount(amount)
                .build();

        transactionRepository.save(transaction);

        try {
            accountClient.deposit(account.getId(), amount);
            transaction.setTransactionStatus(TransactionStatus.COMPLETED);
            log.info("Deposited {} to account {}", amount, account.getId());
            kafkaMessageProducer.sendMessage(user.getEmail()+",transaction,Deposited " + amount + " to account " + account.getId());
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            log.error("Error while depositing money", e);
            throw new RuntimeException("Error while depositing money", e);
        } finally {
            transactionRepository.save(transaction);
        }
    }

    @Transactional
    public void withdraw(Long accountId, Double amount) {
        var account = fromAccount(accountId);
        var user = fromUser(account.getUserId());
        if (account.getBalance() < amount) {
            throw new IllegalStateException("Not enough money on account " + accountId);
        }

        var transaction = Transaction.builder()
                .fromAccountId(account.getId())
                .transactionType(TransactionType.WITHDRAWAL)
                .transactionStatus(TransactionStatus.PENDING)
                .transactionDate(LocalDateTime.now())
                .amount(amount)
                .build();
        transactionRepository.save(transaction);

        try {
            accountClient.withdraw(account.getId(), amount);
            transaction.setTransactionStatus(TransactionStatus.COMPLETED);
            log.info("Withdrawn {} from account {}", amount, account.getId());
            kafkaMessageProducer.sendMessage(user.getEmail()+",transaction,Withdrawal " + amount + " from " + account.getId());
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            log.error("Error while withdrawing money", e);
            throw new RuntimeException("Error while withdrawing money", e);
        } finally {
            transactionRepository.save(transaction);
        }
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, Double amount) {
        var fromAccount = fromAccount(fromAccountId);
        var toAccount = toAccount(toAccountId);
        var fromUser = fromUser(fromAccount.getUserId());
        var toUser = toUser(toAccount.getUserId());

        if (fromAccount.getBalance() < amount) {
            throw new IllegalStateException("Not enough money on account " + fromAccountId);
        }

        var transaction = Transaction.builder()
                .fromAccountId(fromAccount.getId())
                .toAccountId(toAccount.getId())
                .transactionType(TransactionType.TRANSFER)
                .transactionStatus(TransactionStatus.PENDING)
                .transactionDate(LocalDateTime.now())
                .amount(amount)
                .build();
        transactionRepository.save(transaction);

        try {
            accountClient.withdraw(fromAccount.getId(), amount);
            accountClient.deposit(toAccount.getId(), amount);
            transaction.setTransactionStatus(TransactionStatus.COMPLETED);
            log.info("Transferred {} from account {} to account {}", amount, fromAccount.getId(), toAccount.getId());
            kafkaMessageProducer.sendMessage(toUser.getEmail()+",transaction,Transfer " + amount + " to " + fromAccount.getId());
            kafkaMessageProducer.sendMessage(fromUser.getEmail()+",transaction,Transfer " + amount + " from " + toAccount.getId());
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            log.error("Error while transferring money", e);
            throw new RuntimeException("Error while transferring money", e);
        } finally {
            transactionRepository.save(transaction);
        }
    }

    private AccountDto fromAccount (Long accountId) {
        var account = accountClient.getAccountById(accountId);
        if (account == null)
            throw new IllegalArgumentException("Account not found");
        return account;
    }

    private AccountDto toAccount (Long accountId) {
        var account = accountClient.getAccountById(accountId);
        if (account == null)
            throw new IllegalArgumentException("Account not found");
        return account;
    }

    private UserDto fromUser (Long userId) {
        var user = userClient.findById(userId);
        if (user == null)
            throw new IllegalArgumentException("User not found");
        return user;
    }

    private UserDto toUser (Long userId) {
        var user = userClient.findById(userId);
        if (user == null)
            throw new IllegalArgumentException("User not found");
        return user;
    }
}
