package com.example.be.Request;

import com.example.be.data.entity.RefrigeRator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data

public class UpdateRequest {
    private String userId;
    private List<RefrigeRator> data;

}
