package com.example.be.controller;

import com.example.be.data.entity.User;
import com.example.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true") // CORS 설정 (필요에 따라 조정)
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<List<User>> findUser(@RequestParam String userId){
        List<User> users =  userService.findUser(userId);
        System.out.println(users);
        return ResponseEntity.ok(users);
    }
    @PostMapping("/create/subUser")
    public ResponseEntity<Void> createUserSubscribe(@RequestParam String userId1, @RequestParam String userId2){
        userService.createUserSubscribe(userId1,userId2);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/find/subUser")
    public ResponseEntity<List<User>> findUserBySubscribe(@RequestParam String userId){
        List<User> users = userService.findUserBySubscribe(userId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/find/refriUser")
    public ResponseEntity<List<User>> findUserByRefrigerator(@RequestParam String refrigerator_id){
        List<User> users = userService.findUserByRefrigerator(refrigerator_id);
        return ResponseEntity.ok(users);
    }
}
