package com.trpo.dominion.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldCash {

    private Integer bronze;
    private Integer silver;
    private Integer gold;

    private final Integer bronzeCost = 0;
    private final Integer silverCost = 3;
    private final Integer goldCost = 6;

    public void resetCash(){
        bronze = 60;
        silver = 40;
        gold = 30;
    }
}
