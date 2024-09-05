package com.example.be.service;

import com.example.be.Request.FoodRemainingDays;
import com.example.be.data.entity.User;
import com.example.be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void createUserSubscribe(String userid1,String userid2){
        userRepository.createUserSubscribe(userid1,userid2);
    }
    public void deleteUserSUBscribe(String userid1, String userid2){
        userRepository.deleteUserSUBscribe(userid1,userid2);
    }

    public List<User> findUserBySubscribe(String userId){
        return userRepository.findUserBySubscribe(userId);
    }
    public List<User> findUserBySubscribeUser(String userId){
        return userRepository.findUserBySubscribeUser(userId);
    }

    public List<User> findUserByRefrigerator(String refrigerator_id){
        return userRepository.findUserByRefrigerator(refrigerator_id);
    }
    public List<User> findUser(String userId){
        return userRepository.findUser(userId);
    }




    public List<FoodRemainingDays> getRefrigeratorAndFoodIdWithThreeDaysRemaining() {
        return userRepository.findFoodIdAndRefrigeratorIdAndRemainingDays();
    }

}
