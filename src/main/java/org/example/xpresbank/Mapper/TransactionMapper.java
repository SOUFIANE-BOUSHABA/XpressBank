package org.example.xpresbank.Mapper;

import org.example.xpresbank.DTO.CreateTransactionDTO;
import org.example.xpresbank.DTO.TransactionDTO;
import org.example.xpresbank.Entity.Account;
import org.example.xpresbank.Entity.Enums.Frequency;
import org.example.xpresbank.Entity.Enums.TransactionType;
import org.example.xpresbank.Entity.Transaction;
import org.example.xpresbank.VM.TransactionVM;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(CreateTransactionDTO createTransactionDTO, Account sourceAccount, Account destinationAccount) {
        String type = createTransactionDTO.getType();
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }

        Transaction.TransactionBuilder transactionBuilder = Transaction.builder()
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .type(TransactionType.valueOf(type.toUpperCase()))
                .amount(createTransactionDTO.getAmount())
                .transactionFee(calculateTransactionFee(type, createTransactionDTO.getAmount()))
                .status("PENDING")
                .timestamp(new java.util.Date());

        if (createTransactionDTO.getFrequency() != null) {
            transactionBuilder.frequency(Frequency.valueOf(createTransactionDTO.getFrequency().toUpperCase()));
        }

        return transactionBuilder.build();
    }


    public TransactionVM toTransactionVM(Transaction transaction, String message) {
        return TransactionVM.builder()
                .id(transaction.getId())
                .sourceAccountNumber(transaction.getSourceAccount().getAccountNumber())
                .destinationAccountNumber(transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getAccountNumber() : null)
                .amount(transaction.getAmount())
                .transactionFee(transaction.getTransactionFee())
                .type(transaction.getType().name())
                .status(transaction.getStatus())
                .message(message)
                .build();
    }



    private double calculateTransactionFee(String type, double amount) {

        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }

        switch (type.toUpperCase()) {
            case "CLASSIC":
                return amount * 0.02;
            case "INSTANT":
                return amount * 0.05;
            default:
                return 0.0;
        }
    }
}