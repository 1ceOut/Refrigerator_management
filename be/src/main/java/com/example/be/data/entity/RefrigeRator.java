package com.example.be.data.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDate;
import java.util.UUID;

@Node("RefrigeRator")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefrigeRator {

    @Id
    private String refrigerator_id = UUID.randomUUID().toString();

    private String refrigerator_name;




}
