package com.example.be.controller;

import com.example.be.data.entity.User;
import com.example.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.neo4j.cypherdsl.core.Use;
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

    @PostMapping("/delete/subUser")
    public ResponseEntity<Void> deleteUserSUBscribe(@RequestParam String userid1, @RequestParam String userid2){
        userService.deleteUserSUBscribe(userid1,userid2);
        return ResponseEntity.ok().build();
    }



    //구독 관계
    @GetMapping("/find/subUser")
    public ResponseEntity<List<User>> findUserBySubscribe(@RequestParam String userId){
        List<User> users = userService.findUserBySubscribe(userId);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/find/Usersub")
    public ResponseEntity<List<User>> findUserBySubscribeUser(@RequestParam String userId){
        List<User> users = userService.findUserBySubscribeUser(userId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/find/refriUser")
    public ResponseEntity<List<User>> findUserByRefrigerator(@RequestParam String refrigerator_id){
        List<User> users = userService.findUserByRefrigerator(refrigerator_id);
        return ResponseEntity.ok(users);
    }


    @GetMapping("/find/refriName")
    public ResponseEntity<String> findRefrigeratorByRefrigeratorName(@RequestParam String refrigerator_id){
        String Names = userService.findRefrigeratorByRefrigeratorName(refrigerator_id);
        return ResponseEntity.ok(Names);
    }
}
