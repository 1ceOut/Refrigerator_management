package com.example.be.repository;

import com.example.be.data.entity.Barcode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarcodeRepository extends Neo4jRepository<Barcode, String> {

    /*
    Neo4jRepository에서 엔티티를 삭제할 때는 기본적으로 엔티티의 ID를 사용합니다.
    하지만, 원자재성 식품 같은 경우 바코드가 없을경우를 대비해서
    쿼리문을 작성해서 이름으로 삭제하려고 수정함.


        void deleteByBarcode(String productName);
     */

    @Query("MATCH (b:food {productName: $productName}) DELETE b")
    void deleteByProductName(String productName);
}
