package com.example.be.service;

import com.example.be.data.entity.RefrigeRator;
import com.example.be.data.entity.User;
import com.example.be.repository.RefrigeRatorRepository;
import com.example.be.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefrigeRatorService {


    private final RefrigeRatorRepository refrigeRatorRepository;
    private final UserRepository userRepository;

    public RefrigeRatorService(RefrigeRatorRepository refrigeRatorRepository, UserRepository userRepository) {
        this.refrigeRatorRepository = refrigeRatorRepository;
        this.userRepository = userRepository;
    }

//    //냉장고 이름 저장
//    public void insertRefrigeRator(RefrigeRator refrigeRator) {
//        refrigeRatorRepository.save(refrigeRator);
//    }

    @Transactional
    public RefrigeRator addRefrigeRator(String userId, String refrigeratorName) {
        // 사용자 조회
        User user = userRepository.findByUserID(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 냉장고 이름으로 중복 체크
        Optional<RefrigeRator> existingRefrigeratorOpt = refrigeRatorRepository.findByRefrigeratorName(refrigeratorName);
        if (existingRefrigeratorOpt.isPresent()) {
            throw new RuntimeException("Refrigerator with this name already exists.");
        }

        // 새로운 냉장고 생성 및 저장
        RefrigeRator refrigerator = RefrigeRator.builder()
                .refrigeratorName(refrigeratorName)
                .refrigerator_id(UUID.randomUUID().toString())
                .build();

        refrigeRatorRepository.save(refrigerator);

        // 사용자와 냉장고의 관계를 명시적으로 추가
        user.getRefrigerators().add(refrigerator);
        userRepository.save(user);

        return refrigerator;
    }


    //냉장고 삭제
    public void deleteByrefrigerator_name(String refrigerator_name) {
        refrigeRatorRepository.deleteByName(refrigerator_name);
    }
    //모든 냉장고 조회
    public List<RefrigeRator> findAll() {
        return refrigeRatorRepository.findAll();
    }


    // 사용자 ID로 냉장고 조회
    public List<RefrigeRator> findRefrigeratorsByUserId(String userId) {
        try {
            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(userId);
            String extractedUserId = jsonNode.get("userId").asText();
            System.out.println("Extracted User ID: " + extractedUserId);
            return userRepository.findRefrigeratorsByUserId(extractedUserId);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing user ID");
        }

    }
//    public List<RefrigeRator> findUserByRefrigerators(String userId){
//        // userid로 user 찾음
//
//        System.out.println("User ID: " + userId);
//
//        try {
//            // JSON 파싱
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(userId);
//            String extractedUserId = jsonNode.get("userId").asText();
//            System.out.println("Extracted User ID: " + extractedUserId);
//            Optional<User> optionalUser = userRepository.findByUserID(extractedUserId);
//            if(optionalUser.isPresent()){
//                // user가 null이 아니면
//                // user가 관계를 형성하고 있는 냉장고 찾음
//                return optionalUser.get().getRefrigerators();
//            } else {
//                throw new RuntimeException("user not found");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error parsing user ID");
//        }
//    }

    //냉장고 이름 수정
    public RefrigeRator updateByName(String refrigerator_id, String refrigerator_name) {
        return refrigeRatorRepository.updateByName(refrigerator_id, refrigerator_name);
    }


}
