package com.example.be.service;

import com.example.be.data.entity.RefrigeRator;
import com.example.be.repository.RefrigeRatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RefrigeRatorService {

    @Autowired
    private RefrigeRatorRepository refrigeRatorRepository;

    //냉장고 이름 저장
    public void insertRefrigeRator(RefrigeRator refrigeRator) {
        refrigeRatorRepository.save(refrigeRator);
    }
    //냉장고 삭제
    public void deleteByrefrigerator_name(String refrigerator_name) {
        refrigeRatorRepository.deleteByName(refrigerator_name);
    }
    //모든 냉장고 조회
    public List<RefrigeRator> findAll() {
        return refrigeRatorRepository.findAll();
    }

    //냉장고 이름 수정
    public RefrigeRator updateByName(String refrigerator_id, String refrigerator_name) {
        return refrigeRatorRepository.updateByName(refrigerator_id, refrigerator_name);
    }


}
