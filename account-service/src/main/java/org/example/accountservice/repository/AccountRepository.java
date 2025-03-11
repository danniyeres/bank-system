package org.example.accountservice.repository;

import org.example.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    Account findByUserId(Long userId);
    boolean existsByUsername(String username);
    boolean existsByUserId(Long userId);
}
