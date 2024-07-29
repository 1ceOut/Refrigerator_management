package com.example.be.data.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barcode {

    @Id
    private String barcode;
    private String productName;
    private String expiryDate;
    private String companyName;
    private String address;
    private String productType;
    private String permissionDate;

}
