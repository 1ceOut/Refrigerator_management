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





//1 2 3 4 5 6  // 순서
//* * * * * *  // 실행주기 문자열
//
//// 순서별 정리
//1. 초(0-59)
//2. 분(0-59)
//3. 시간(0-23)
//4. 일(1-31)
//5. 월(1-12)
//6. 요일(0-7)

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")  // 매일 정각에 실행
    public void reportCurrentTime() {
        List<FoodRemainingDays> products = userService.getRefrigeratorIdWithOneDayRemaining();
        for (FoodRemainingDays product : products) {
            System.out.println("냉장고 UUID: " + product.getRefrigerator_id() + ", 남은 일자 (1일): " + product.getRemainingDays());
        }
    }

//

}
