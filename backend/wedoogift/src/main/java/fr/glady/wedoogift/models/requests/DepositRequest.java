package fr.glady.wedoogift.models.requests;

import fr.glady.wedoogift.models.DepositType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class DepositRequest {

    private String username;

    private String companyName;

    private double amount;

    private DepositType type;

}
