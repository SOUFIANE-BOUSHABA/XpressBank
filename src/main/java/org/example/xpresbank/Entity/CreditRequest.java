package org.example.xpresbank.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.xpresbank.Entity.Enums.CreditRequestStatus;

import java.util.Date;

@Entity
@Table(name = "credit_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double interestRate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditRequestStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String eligibilityStatus;
}


