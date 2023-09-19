package fr.glady.wedoogift.services;

import fr.glady.wedoogift.managers.GiftManager;
import fr.glady.wedoogift.models.Gift;
import fr.glady.wedoogift.models.User;
import fr.glady.wedoogift.models.requests.GiftRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final GiftManager giftManager;
    private static final List<User> users = new ArrayList<>();

    private static final int THRESHOLD_DAYS = 365;

    public UserService(GiftManager giftManager) {
        this.giftManager = giftManager;
    }

    /**
     * Create a user.
     *
     * @param name the name
     */
    public User createUser(String name) {
        if (userExists(name)) {
            log.info("User with name: {} exists.", name);
            return findUserWithName(name);
        }

        log.info("Create user with name: {}", name);
        User newUser = User.builder()
                .name(name)
                .gifts(Collections.emptyList())
                .build();
        users.add(newUser);
        log.info("User with name: {} added.", newUser.getName());
        return newUser;
    }

    /**
     * Get gift balance from user.
     *
     * @param name the name
     * @return the balance
     */
    public double getGiftBalanceFromUser(String name) {
        log.info("Get balance from user: {}", name);
        if (userExists(name)) {
            log.info("User with name : {} exists", name);

            return users.stream()
                    .filter(user -> user.getName().equalsIgnoreCase(name))
                    .map(User::getGifts)
                    .flatMap(List::stream)
                    .filter(gift -> checkDateIsYoungerThan365Days(gift.getDepositDate()))
                    .map(Gift::getAmount)
                    .mapToDouble(Double::doubleValue)
                    .sum();
        }
        log.info("User with name : {} not exists", name);
        throw new NullPointerException();
    }

    /**
     * Add gift to a user.
     *
     * @param giftRequest the request
     * @return the user
     */
    public User addGiftToUser(GiftRequest giftRequest) {
        log.info("Add gift to user with name : {}", giftRequest.getUsername());
        if (userExists(giftRequest.getUsername())) {

            User user = findUserWithName(giftRequest.getUsername());
            User updatedUser = giftManager.giftDeposit(user, giftRequest.getCompanyName(), giftRequest.getAmount());
            users.remove(user);
            users.add(updatedUser);
            return updatedUser;
        }
        log.info("User with name : {} not exists", giftRequest.getUsername());
        throw new NullPointerException();
    }

    private User findUserWithName(String name) {
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }

    private boolean userExists(String name) {
        return users.stream()
                .anyMatch(user -> user.getName().equalsIgnoreCase(name));
    }

    private boolean checkDateIsYoungerThan365Days(LocalDate toCompareDate) {
        log.info("Check difference between date now : {} and saved : {}", LocalDate.now().toEpochDay(),
                toCompareDate.toEpochDay());
        long dayDifference = LocalDate.now().toEpochDay() - toCompareDate.toEpochDay();
        return dayDifference < THRESHOLD_DAYS;
    }

}
