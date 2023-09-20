package fr.glady.wedoogift.services;

import fr.glady.wedoogift.managers.DepositManager;
import fr.glady.wedoogift.models.Deposit;
import fr.glady.wedoogift.models.DepositType;
import fr.glady.wedoogift.models.User;
import fr.glady.wedoogift.models.requests.DepositRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final DepositManager depositManager;
    private static final List<User> users = new ArrayList<>();

    private static final int THRESHOLD_DAYS = 365;

    public UserService(DepositManager depositManager) {
        this.depositManager = depositManager;
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
                .deposits(Collections.emptyList())
                .build();
        users.add(newUser);
        log.info("User with name: {} added.", newUser.getName());
        return newUser;
    }

    /**
     * Get deposit balance from user.
     *
     * @param name the name
     * @return the balance
     */
    public double getDepositBalanceFromUser(String name, DepositType type) {
        log.info("Get balance from user: {}", name);
        if (userExists(name)) {
            log.info("User with name : {} exists", name);
            return getBalance(name, type);
        }
        log.info("User with name : {} not exists", name);
        throw new NullPointerException();
    }

    /**
     * Add deposit to a user.
     *
     * @param depositRequest the request
     * @return the user
     */
    public User addDepositToUser(DepositRequest depositRequest) {
        log.info("Add deposit to user with name : {}", depositRequest.getUsername());
        if (userExists(depositRequest.getUsername())) {

            User user = findUserWithName(depositRequest.getUsername());
            User updatedUser = depositManager.addDeposit(user, depositRequest.getCompanyName(),
                    depositRequest.getAmount(), depositRequest.getType());
            users.remove(user);
            users.add(updatedUser);
            return updatedUser;
        }
        log.info("User with name : {} not exists", depositRequest.getUsername());
        throw new NullPointerException();
    }

    private User findUserWithName(String name) {
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }

    private boolean userExists(String name) {
        return !users.isEmpty() && users.stream()
                .anyMatch(user -> user.getName().equalsIgnoreCase(name));
    }

    private boolean checkDateIsYoungerThan365Days(LocalDate toCompareDate) {
        log.info("Check difference between date now : {} and saved : {}", LocalDate.now().toEpochDay(),
                toCompareDate.toEpochDay());
        long dayDifference = toCompareDate.toEpochDay() - LocalDate.now().toEpochDay();
        return dayDifference < THRESHOLD_DAYS;
    }

    private boolean checkDateIsYoungerThanGivenDate(LocalDate toCompareDate) {
        log.info("Check difference between given date : {} and saved : {}", LocalDate.now().toEpochDay(),
                toCompareDate.toEpochDay());
        long dayDifference = LocalDate.of(2024, 2, 28).toEpochDay() - toCompareDate.toEpochDay();
        return dayDifference > 0;
    }

    private double getBalance(String name, DepositType type) {
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .map(User::getDeposits)
                .flatMap(List::stream)
                .filter(deposit -> deposit.getType().equals(type))
                .filter(deposit -> {
                    if (type.equals(DepositType.GIFT)) {
                        return checkDateIsYoungerThan365Days(deposit.getDepositDate());
                    }
                    return checkDateIsYoungerThanGivenDate(deposit.getDepositDate());
                })
                .map(Deposit::getAmount)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

}
