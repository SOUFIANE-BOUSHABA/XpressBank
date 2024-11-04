package org.example.xpresbank.Exception;

public class CreditRequestNotFoundException extends RuntimeException {
    public CreditRequestNotFoundException(String message) {
        super(message);
    }
}
