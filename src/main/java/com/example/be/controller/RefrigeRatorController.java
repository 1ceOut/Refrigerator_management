package com.example.be.controller;



import com.example.be.Request.InviteRefrigeratorRequset;
import com.example.be.Request.RefrigeRatorRequest;
import com.example.be.Request.UpdateRequest;
import com.example.be.data.entity.RefrigeRator;
import com.example.be.data.entity.User;
import com.example.be.service.RefrigeRatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/food")
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true") // CORS 설정 (필요에 따라 조정)
public class RefrigeRatorController {

    private final RefrigeRatorService refrigeRatorService;

    //냉장고 등록
    @PostMapping("/refri/insert")
    public RefrigeRator insertRefrigeRator(@RequestBody RefrigeRatorRequest request) {
        return refrigeRatorService.addRefrigeRator(request.getUserId(), request.getRefrigeratorName());
    }

    //냉장고 삭제
    @PostMapping("/refri/delete")
    public ResponseEntity<Void> deleteByrefrigerator_id(@RequestParam String refrigerator_id) {
        refrigeRatorService.deleteByrefrigerator_id(refrigerator_id);
        return ResponseEntity.noContent().build(); // HTTP 204 상태 코드
    }

    // 모든 냉장고 조회_현재 안씀
    @GetMapping("/refri/list")
    public List<RefrigeRator> findAll() {
        return refrigeRatorService.findAll();
    }
    //냉장고 조회
    @PostMapping("/refri/user/refrilist")
    public ResponseEntity<List<RefrigeRator>> getRefrigerators(@RequestBody String userId) {
        List<RefrigeRator> refrigerators = refrigeRatorService.findRefrigeratorsByUserId(userId);
        return ResponseEntity.ok(refrigerators);
    }
    //냉장고 생성자가 생성한 냉장고 조회
    @GetMapping("/refri/master/refrilist")
    public List<RefrigeRator> getMasterList(@RequestParam String id) {
        return refrigeRatorService.getMasterList(id);
    }
    //냉장고 이름 수정_지금안씀
    @GetMapping("/refri/update")
    public RefrigeRator updateByName(@RequestParam String refrigerator_id, @RequestParam String refrigerator_name) {
        return refrigeRatorService.updateByName(refrigerator_id, refrigerator_name);
    }
    //냉장고 생성자가 냉장고 이름 수정
    @PostMapping("/refri/master/update")
    public ResponseEntity<String> updateRefrigeratorNames(@RequestBody UpdateRequest request) {
        System.out.println("Received update request: " + request);

        try {
            refrigeRatorService.updateRefrigeratorNames(request.getUserId(), request.getData());
            return ResponseEntity.ok("Refrigerators updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update refrigerators.");
        }
    }
    //초대코드로 냉장고 추가
    @PostMapping("/refri/invite/user")
    public List<InviteRefrigeratorRequset> inviteRefrigerator(@RequestBody InviteRefrigeratorRequset request) {
        return refrigeRatorService.inviteRefrigerator(request);
    }
    //초대코드로 냉장고 추가한 인원 조회
    @PostMapping("/refri/invite/userlist")
    public List<User> inviteUserList(@RequestBody Map<String, String> requestData) {
        String userId = requestData.get("userId");
        String refrigerator_id = requestData.get("refrigerator_id");
        return refrigeRatorService.inviteUserList(userId,refrigerator_id);
    }

    //초대코드로 냉장고 추가한 인원 삭제
    @DeleteMapping("/refri/invite/delete")
    public String deleteInvite(@RequestParam List<String> userId, @RequestParam("refrigerator_id") String refrigerator_id) {

        System.out.println("삭제할 사용자 ID 리스트: " + userId);
        System.out.println("냉장고 ID: " + refrigerator_id);
        return refrigeRatorService.deleteInvite(userId, refrigerator_id);
    }







}