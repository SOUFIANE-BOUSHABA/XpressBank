package org.example.xpresbank.Exception;

public class UserNotEligibleException extends RuntimeException {
    public UserNotEligibleException(String message) {
        super(message);
    }
}
