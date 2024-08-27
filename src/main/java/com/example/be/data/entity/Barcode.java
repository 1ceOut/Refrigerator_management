package com.example.be.data.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/*
@Node는 JPA의 @Entity와 같다고 생각하면 편하다.
@Property는 해당 노드의 속성 값임을 명시하는 어노테이션이다.
@Id는 pk를 정의해준다.
 */
@Node("food")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barcode {

    @Id
    private String id = UUID.randomUUID().toString();;

    private String productName;     //제품명

    private String barcode;         //바코드
    private String expiryDate;      //유통기한
    private String count;           //수량
    private String productType;     //식품 유형
    private LocalDate createdDate;  //등록 일자

    private String lcategory;       //식품 대분류    ex) 육류, 채소류
    private String scategory;       //식품 소분류    ex) 생것, 삶은것, 삶은것을말린것을삶은것을데친것을튀긴것,


    private String refrigeratorName; // 냉장고이름

}
