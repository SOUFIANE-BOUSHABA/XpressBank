package org.example.xpresbank.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Account> accounts;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> transactions;




    @Column(nullable = false, columnDefinition = "integer default 18")
    private int age;

    @Column(nullable = false, columnDefinition = "double precision default 1000.0")
    private double monthlyIncome;

    @Column(nullable = false, columnDefinition = "integer default 600")
    private int creditScore;

    @Column(nullable = false, columnDefinition = "double precision default 0.0")
    private double debtToIncomeRatio;

    @Column(nullable = false, columnDefinition = "integer default 6")
    private int bankingDuration;
}
