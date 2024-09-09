package com.example.be.Component;

import com.example.be.Request.FoodRemainingDays;
import com.example.be.service.UserService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScheduledTasks {

    private final UserService userService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ScheduledTasks(UserService userService, KafkaTemplate<String, String> kafkaTemplate) {
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendItems(FoodRemainingDays item) {
        Map<String, String> map = new HashMap<>();
        map.put("refrigerator_id", item.getRefrigerator_id());
        map.put("remainingDays", Long.toString(item.getRemainingDays()));
        map.put("id",item.getId());
        kafkaTemplate.send("report-food", map.toString());
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
//    @Scheduled(cron = "0/1 * * * * *", zone = "Asia/Seoul")  // 매초 마다 실행
    public void reportCurrentTime() {
        List<FoodRemainingDays> products = userService.getRefrigeratorAndFoodIdWithThreeDaysRemaining();
        for (FoodRemainingDays product : products) {
            sendItems(product);
            System.out.println("냉장고 UUID: " + product.getRefrigerator_id() +
                    ", 음식 UUID: " + product.getId() +
                    ", 남은 일자 (3일 이하 (-)로 나올경우 유통기한이 지난 친구): " + product.getRemainingDays());
        }
    }
}
