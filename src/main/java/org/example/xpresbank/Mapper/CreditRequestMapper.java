package org.example.xpresbank.Mapper;

import org.example.xpresbank.DTO.CreditRequestDTO;
import org.example.xpresbank.DTO.CreateCreditRequestDTO;
import org.example.xpresbank.Entity.CreditRequest;
import org.example.xpresbank.Entity.Enums.CreditRequestStatus;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.VM.CreditRequestVM;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CreditRequestMapper {

    public CreditRequestDTO toCreditRequestDTO(CreditRequest creditRequest) {
        return CreditRequestDTO.builder()
                .id(creditRequest.getId())
                .amount(creditRequest.getAmount())
                .interestRate(creditRequest.getInterestRate())
                .startDate(creditRequest.getStartDate())
                .endDate(creditRequest.getEndDate())
                .status(creditRequest.getStatus().name())
                .userId(creditRequest.getUser().getId())
                .eligibilityStatus(creditRequest.getEligibilityStatus())
                .build();
    }

    public CreditRequest fromCreateCreditRequestDTO(CreateCreditRequestDTO createCreditRequestDTO, User user) {
        return CreditRequest.builder()
                .amount(createCreditRequestDTO.getAmount())
                .interestRate(createCreditRequestDTO.getInterestRate())
                .user(user)
                .startDate(new Date())
                .status(CreditRequestStatus.PENDING)
                .eligibilityStatus("PENDING")
                .build();
    }

    public CreditRequestVM toCreditRequestVM(CreditRequest creditRequest, String message) {
        return CreditRequestVM.builder()
                .id(creditRequest.getId())
                .amount(creditRequest.getAmount())
                .interestRate(creditRequest.getInterestRate())
                .startDate(creditRequest.getStartDate())
                .endDate(creditRequest.getEndDate())
                .status(creditRequest.getStatus().name())
                .userId(creditRequest.getUser().getId())
                .eligibilityStatus(creditRequest.getEligibilityStatus())
                .message(message)
                .build();
    }
}
