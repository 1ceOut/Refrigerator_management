package com.example.be.data.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
import java.util.UUID;

@Node("user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    // "OWNS"라는 유형의 관계를 사용하여 User와 RefrigeRator 간의 연결을 정의합니다.
    @Relationship(type = "OWNS", direction = Relationship.Direction.OUTGOING)
    private List<RefrigeRator> refrigerators;
}
