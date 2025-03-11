package org.example.accountservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private Long userId;
    private double balance;

    @OneToOne (cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    private Card card;
}
