package fr.glady.wedoogift.managers;

import fr.glady.wedoogift.models.Gift;
import fr.glady.wedoogift.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GiftManager {

    public GiftManager() {
    }

    /**
     * Add gift to user.
     *
     * @param user        the suer
     * @param companyName the name
     * @param amount      the amount
     * @return a user
     */
    public User giftDeposit(User user, String companyName, double amount) {
        log.info("Add gift to user: " + user.getName() + " from company: " + companyName +
                " with amount: " + amount);
        Gift gift = Gift.builder()
                .amount(amount)
                .depositDate(LocalDate.now())
                .companyName(companyName)
                .build();
        List<Gift> gifts = new ArrayList<>(user.getGifts());
        gifts.add(gift);
        return user.toBuilder()
                .gifts(gifts)
                .build();
    }
    
}
