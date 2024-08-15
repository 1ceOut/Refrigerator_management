package com.example.be.controller;


import com.example.be.data.entity.RefrigeRator;
import com.example.be.service.RefrigeRatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 (필요에 따라 조정)
public class RefrigeRatorController {

    private final RefrigeRatorService refrigeRatorService;

    //냉장고 등록
    @PostMapping("/refri/insert")
    public String insertRefrigeRator(@RequestBody RefrigeRator refrigeRator) {

        //db 저장
        refrigeRatorService.insertRefrigeRator(refrigeRator);
        return "success";
    }

    //냉장고 삭제
    @GetMapping("/refri/delete")
    public void deleteByrefrigerator_name(@RequestParam("refrigerator_name")String refrigerator_name) {
        refrigeRatorService.deleteByrefrigerator_name(refrigerator_name);
    }

    // 모든 냉장고 조회
    @GetMapping("/refri/list")
    public List<RefrigeRator> findAll() {
        return refrigeRatorService.findAll();
    }

    @GetMapping("/refri/update")
    public RefrigeRator updateByName(@RequestParam String refrigerator_id, @RequestParam String refrigerator_name) {
        return refrigeRatorService.updateByName(refrigerator_id, refrigerator_name);
    }
}