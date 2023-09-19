package fr.glady.wedoogift.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class User {

    private String name;

    private List<Gift> gifts;
    

}
