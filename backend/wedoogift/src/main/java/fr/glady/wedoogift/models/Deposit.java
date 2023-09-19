package fr.glady.wedoogift.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Deposit {

    private double amount;

    private LocalDate depositDate;

    private String companyName;

    private DepositType type;

}
