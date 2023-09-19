package fr.glady.wedoogift.models.requests;

import fr.glady.wedoogift.models.DepositType;
import lombok.Data;

@Data
public class DepositRequest {

    private String username;

    private String companyName;

    private double amount;

    private DepositType type;

}
