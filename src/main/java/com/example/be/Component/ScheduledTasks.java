package com.example.be.Component;

import com.example.be.Request.FoodRemainingDays;
import com.example.be.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    private final UserService userService;

    public ScheduledTasks(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 * * * ?")  // 매시간 정각에 실행, cron 표현식으로 주기 설정
    public void reportCurrentTime() {
        List<FoodRemainingDays> products = userService.getRefrigeratorIdWithOneDayRemaining();
        // 결과 처리 로직
        products.forEach(product -> {
            System.out.println("Refrigerator ID: " + product.getRefrigeratorId() +
                    ", Remaining Days: " + product.getRemainingDays());
        });
    }
}
