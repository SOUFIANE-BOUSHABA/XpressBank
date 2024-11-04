package org.example.xpresbank.Repository;

import org.example.xpresbank.Entity.CreditRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {
}
