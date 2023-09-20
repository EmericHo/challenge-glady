package fr.glady.wedoogift.managers;

import fr.glady.wedoogift.models.Deposit;
import fr.glady.wedoogift.models.DepositType;
import fr.glady.wedoogift.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class DepositManagerTest {

    private DepositManager depositManager;

    @BeforeEach
    public void setUp() {
        depositManager = new DepositManager();
    }

    @Test
    public void testAddDepositForGiftDeposit() {
        User user = User.builder()
                .name("John")
                .deposits(List.of(Deposit.builder()
                        .type(DepositType.GIFT)
                        .depositDate(LocalDate.now())
                        .amount(100.0)
                        .companyName("Tesla")
                        .build()))
                .build();

        depositManager.addDeposit(user, "Tesla", 100.0, DepositType.GIFT);
    }

    @Test
    public void testAddDepositForMealDeposit() {
        User user = User.builder()
                .name("John")
                .deposits(List.of(Deposit.builder()
                        .type(DepositType.MEAL)
                        .depositDate(LocalDate.now())
                        .amount(50.0)
                        .companyName("Apple")
                        .build()))
                .build();

        depositManager.addDeposit(user, "Apple", 50.0, DepositType.MEAL);
    }

}
