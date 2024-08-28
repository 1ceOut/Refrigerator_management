package com.example.be.data.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Node("RefrigeRator")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefrigeRator {

    @Id
    private String refrigerator_id ;

    private String refrigeratorName;

    private String id; // userid
}
