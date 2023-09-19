package fr.glady.wedoogift.managers;

import fr.glady.wedoogift.models.Deposit;
import fr.glady.wedoogift.models.DepositType;
import fr.glady.wedoogift.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DepositManager {

    public DepositManager() {
    }

    /**
     * Add deposit to user.
     *
     * @param user        the suer
     * @param companyName the name
     * @param amount      the amount
     * @return a user
     */
    public User addDeposit(User user, String companyName, double amount, DepositType type) {
        log.info("Add deposit to user: {} from company: {} with amount: {}", user.getName(),
                companyName, amount);
        Deposit deposit = Deposit.builder()
                .amount(amount)
                .depositDate(LocalDate.now())
                .companyName(companyName)
                .type(type)
                .build();
        List<Deposit> deposits = new ArrayList<>(user.getDeposits());
        deposits.add(deposit);
        return user.toBuilder()
                .deposits(deposits)
                .build();
    }

}
