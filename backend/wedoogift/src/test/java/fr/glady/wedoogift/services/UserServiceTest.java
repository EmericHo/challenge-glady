package fr.glady.wedoogift.services;

import fr.glady.wedoogift.managers.DepositManager;
import fr.glady.wedoogift.models.Deposit;
import fr.glady.wedoogift.models.DepositType;
import fr.glady.wedoogift.models.User;
import fr.glady.wedoogift.models.requests.DepositRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private DepositManager depositManager;

    @BeforeEach
    public void setUp() {
        userService = new UserService(depositManager);
    }

    @Test
    public void testCreateUserWhenUserDoesNotExist() {
        String name = "John";

        User newUser = userService.createUser(name);

        assertNotNull(newUser);
        assertEquals(name, newUser.getName());
    }

    @Test
    public void testCreateUserWhenUserExists() {
        String name = "John";
        User existingUser = User.builder().name(name).build();

        User newUser = userService.createUser(name);

        assertNotNull(newUser);
        assertEquals(name, newUser.getName());
    }

    @Test
    public void testGetDepositBalanceForExpiredGiftDepositFromUserWhenUserExists() {
        String name = "John";
        // Create user
        User user = userService.createUser(name);
        DepositRequest depositRequest = DepositRequest.builder()
                .username(name)
                .amount(100.0)
                .companyName("Tesla")
                .type(DepositType.GIFT)
                .build();
        // Expected
        User expectedUser = user.toBuilder()
                .deposits(List.of(Deposit.builder()
                        .type(DepositType.GIFT)
                        .depositDate(LocalDate.of(2024, 9, 20))
                        .amount(100.0)
                        .companyName("Tesla")
                        .build()))
                .build();

        // Mock
        when(depositManager.addDeposit(eq(user), eq("Tesla"), eq(100.0), eq(DepositType.GIFT))).thenReturn(expectedUser);
        userService.addDepositToUser(depositRequest);

        double balance = userService.getDepositBalanceFromUser(name, DepositType.GIFT);

        assertEquals(0.0, balance);
    }

    @Test
    public void testGetDepositBalanceForExpiredMealDepositFromUserWhenUserExists() {
        String name = "John";
        // Create user
        User user = userService.createUser(name);
        DepositRequest depositRequest = DepositRequest.builder()
                .username(name)
                .amount(50.0)
                .companyName("Apple")
                .type(DepositType.MEAL)
                .build();
        // Expected
        User expectedUser = user.toBuilder()
                .deposits(List.of(Deposit.builder()
                        .type(DepositType.MEAL)
                        .depositDate(LocalDate.of(2024, 3, 28))
                        .amount(50.0)
                        .companyName("Apple")
                        .build()))
                .build();

        // Mock
        when(depositManager.addDeposit(eq(user), eq("Apple"), eq(50.0), eq(DepositType.MEAL))).thenReturn(expectedUser);
        userService.addDepositToUser(depositRequest);

        double balance = userService.getDepositBalanceFromUser(name, DepositType.MEAL);

        assertEquals(0.0, balance);
    }

    @Test
    public void testGetDepositBalanceForGiftFromUserWhenUserExists() {
        String name = "John";
        // Create user
        User user = userService.createUser(name);
        DepositRequest depositRequest = DepositRequest.builder()
                .username(name)
                .amount(100.0)
                .companyName("Tesla")
                .type(DepositType.GIFT)
                .build();
        // Expected
        User expectedUser = user.toBuilder()
                .deposits(List.of(Deposit.builder()
                        .type(DepositType.GIFT)
                        .depositDate(LocalDate.now())
                        .amount(100.0)
                        .companyName("Tesla")
                        .build()))
                .build();

        // Mock
        when(depositManager.addDeposit(eq(user), eq("Tesla"), eq(100.0), eq(DepositType.GIFT))).thenReturn(expectedUser);
        userService.addDepositToUser(depositRequest);

        double balance = userService.getDepositBalanceFromUser(name, DepositType.GIFT);

        assertEquals(100.0, balance);
    }

    @Test
    public void testGetDepositBalanceForMealFromUserWhenUserExists() {
        String name = "John";
        // Create user
        User user = userService.createUser(name);
        DepositRequest depositRequest = DepositRequest.builder()
                .username(name)
                .amount(50.0)
                .companyName("Apple")
                .type(DepositType.MEAL)
                .build();
        // Expected
        User expectedUser = user.toBuilder()
                .deposits(List.of(Deposit.builder()
                        .type(DepositType.MEAL)
                        .depositDate(LocalDate.now())
                        .amount(50.0)
                        .companyName("Apple")
                        .build()))
                .build();

        // Mock
        when(depositManager.addDeposit(eq(user), eq("Apple"), eq(50.0), eq(DepositType.MEAL))).thenReturn(expectedUser);
        userService.addDepositToUser(depositRequest);

        double balance = userService.getDepositBalanceFromUser(name, DepositType.MEAL);

        assertEquals(50.0, balance);
    }

    @Test
    public void testGetDepositBalanceFromUserWhenUserDoesNotExist() {
        String name = "Bob";

        assertThrows(NullPointerException.class, () -> {
            userService.getDepositBalanceFromUser(name, DepositType.GIFT);
        });
    }

    @Test
    public void testAddDepositForUserWhenUserDoesNotExist() {
        String name = "Alice";
        DepositRequest depositRequest = DepositRequest.builder()
                .username(name)
                .amount(50.0)
                .companyName("Apple")
                .type(DepositType.MEAL)
                .build();

        assertThrows(NullPointerException.class, () -> {
            userService.addDepositToUser(depositRequest);
        });
    }

}
