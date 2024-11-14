package org.example.xpresbank.Repository;

import org.example.xpresbank.Entity.CreditRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {

    List<CreditRequest> findByUserId(Long userId);
}
