package org.example.accountservice.service;

import org.example.accountservice.model.Account;
import org.example.accountservice.model.Card;
import org.example.accountservice.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card getCard(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    public Card createCard(Account account) {
        var card = Card.builder()
                .cardNumber(createCardNumber(account.getUserId()))
                .expirationDate(createExpirationDate())
                .cvv(createCvv())
                .account(account)
                .build();

        cardRepository.save(card);
        return card;
    }

    private String createCardNumber(Long userId) {
        StringBuilder cardNumber = new StringBuilder("44001234");
        String userIdStr = String.valueOf(userId);

        cardNumber.append("0".repeat(Math.max(0, 8 - userIdStr.length())));
        cardNumber.append(userIdStr);

        return cardNumber.toString();
    }

    private String createExpirationDate() {
        LocalDate today = LocalDate.now().plusYears(5);
        String month;
        String year = String.valueOf(today.getYear()).substring(2);
        if (today.getMonthValue() >= 10) {
            month = String.valueOf(today.getMonthValue());
        } else {
            month = "0" + today.getMonthValue();
        }
        return month + "/" + year;
    }

    private int createCvv() {
        return (int) (Math.random() * 900 + 100);
    }

}
