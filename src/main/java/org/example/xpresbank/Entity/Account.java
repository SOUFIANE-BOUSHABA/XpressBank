package org.example.xpresbank.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.xpresbank.Entity.Enums.AccountStatus;

import java.util.Set;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private double balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @OneToMany(mappedBy = "sourceAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "destinationAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> incomingTransactions;
}


