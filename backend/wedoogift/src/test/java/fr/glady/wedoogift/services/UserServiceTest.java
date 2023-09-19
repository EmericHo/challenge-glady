package fr.glady.wedoogift.services;

import fr.glady.wedoogift.managers.DepositManager;
import fr.glady.wedoogift.models.Deposit;
import fr.glady.wedoogift.models.DepositType;
import fr.glady.wedoogift.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;
    private List<User> users;

    @Mock
    private DepositManager depositManager;

    @BeforeEach
    public void setUp() {
        users = new ArrayList<>();
        userService = new UserService(depositManager);
    }

    @Test
    public void testCreateUserWhenUserDoesNotExist() {
        String name = "John";

        User newUser = userService.createUser(name);

        assertNotNull(newUser);
        assertEquals(name, newUser.getName());
        assertTrue(users.contains(newUser));
    }

    @Test
    public void testCreateUserWhenUserExists() {
        String name = "John";
        User existingUser = User.builder().name(name).build();
        users.add(existingUser);

        User newUser = userService.createUser(name);

        assertNotNull(newUser);
        assertEquals(name, newUser.getName());
        assertTrue(users.contains(newUser));
        assertEquals(1, users.size());
    }

    @Test
    public void testGetGiftBalanceFromUserWhenUserExists() {
        String name = "John";
        List<Deposit> gifts = List.of(Deposit.builder()
                .depositDate(LocalDate.now().minusDays(200))
                .amount(100.0)
                .companyName("Tesla")
                .build(), Deposit.builder()
                .depositDate(LocalDate.now().minusDays(400))
                .amount(50.0)
                .companyName("Tesla")
                .build());
        User existingUser = User.builder()
                .name(name)
                .deposits(gifts)
                .build();
        users.add(existingUser);

        double balance = userService.getDepositBalanceFromUser(name, DepositType.GIFT);

        assertEquals(100.0, balance, 0.01);
    }

    @Test
    public void testGetGiftBalanceFromUserWhenUserDoesNotExist() {
        String name = "John";

        double balance = userService.getDepositBalanceFromUser(name, DepositType.GIFT);

        assertEquals(-1, balance, 0.01);
    }

}
