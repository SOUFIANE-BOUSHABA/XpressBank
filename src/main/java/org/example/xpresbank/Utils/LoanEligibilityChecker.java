package org.example.xpresbank.Utils;

import org.example.xpresbank.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class LoanEligibilityChecker {

    public boolean isEligible(User user, double creditAmount) {
        if (user.getAge() < 18) {
            System.out.println("The user does not meet the minimum age requirement for applying for a loan.");
            return false;
        }

        if (user.getMonthlyIncome() < 1000) {
            System.out.println("The user's monthly income is insufficient to obtain a loan.");
            return false;
        }

        if (user.getCreditScore() < 600) {
            System.out.println("The user's credit score is too low to be eligible.");
            return false;
        }

        if (user.getDebtToIncomeRatio() > 0.4) {
            System.out.println("The user's debt-to-income ratio exceeds the acceptable threshold.");
            return false;
        }

        if (user.getBankingDuration() < 6) {
            System.out.println("The user's banking relationship duration is below the required minimum.");
            return false;
        }
        return true;
    }
}