package org.example.xpresbank.Repository;

import org.example.xpresbank.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUserId(Long userId);

    @Query("SELECT SUM(a.balance) FROM Account a")
    double sumAllBalances();
}
