package fr.glady.wedoogift.services;

import fr.glady.wedoogift.managers.GiftManager;
import fr.glady.wedoogift.models.Gift;
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
    private GiftManager giftManager;

    @BeforeEach
    public void setUp() {
        users = new ArrayList<>();
        userService = new UserService(giftManager);
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
        List<Gift> gifts = List.of(Gift.builder()
                .depositDate(LocalDate.now().minusDays(200))
                .amount(100.0)
                .companyName("Tesla")
                .build(), Gift.builder()
                .depositDate(LocalDate.now().minusDays(400))
                .amount(50.0)
                .companyName("Tesla")
                .build());
        User existingUser = User.builder()
                .name(name)
                .gifts(gifts)
                .build();
        users.add(existingUser);

        double balance = userService.getGiftBalanceFromUser(name);

        assertEquals(100.0, balance, 0.01);
    }

    @Test
    public void testGetGiftBalanceFromUserWhenUserDoesNotExist() {
        String name = "John";

        double balance = userService.getGiftBalanceFromUser(name);

        assertEquals(-1, balance, 0.01);
    }

}
