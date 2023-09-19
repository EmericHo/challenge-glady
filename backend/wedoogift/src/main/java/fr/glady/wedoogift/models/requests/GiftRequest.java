package fr.glady.wedoogift.models.requests;

import lombok.Data;

@Data
public class GiftRequest {

    private String username;

    private String companyName;

    private double amount;

}
