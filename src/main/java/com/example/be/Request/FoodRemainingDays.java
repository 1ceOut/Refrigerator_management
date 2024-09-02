package com.example.be.Request;

import lombok.Data;

@Data
public class FoodRemainingDays {
    private String refrigeratorId;
    private long remainingDays;
}
