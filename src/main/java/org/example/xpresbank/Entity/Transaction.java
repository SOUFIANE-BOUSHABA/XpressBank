package org.example.xpresbank.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.xpresbank.Entity.Enums.Frequency;
import org.example.xpresbank.Entity.Enums.TransactionType;

import java.util.Date;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double transactionFee;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date nextScheduledDate;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
}
