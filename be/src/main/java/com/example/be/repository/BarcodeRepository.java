package com.example.be.repository;

import com.example.be.data.entity.Barcode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarcodeRepository extends Neo4jRepository<Barcode, String> {

    void deleteByBarcode(String barcode);
}
