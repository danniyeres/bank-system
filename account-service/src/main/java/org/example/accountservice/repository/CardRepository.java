package org.example.accountservice.repository;

import org.example.accountservice.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByCardNumber(String cardNumber);
    Card findByAccount_UserId(Long userId);
}
