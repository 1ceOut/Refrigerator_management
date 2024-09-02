package com.example.be.repository;

import com.example.be.data.entity.Barcode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcodeRepository extends Neo4jRepository<Barcode, String> {

//    @Query("MATCH (b:food {refrigeratorName: $refrigeratorName, productName: $productName}) RETURN b LIMIT 1")
//    Barcode findByRefrigeratorNameAndProductName(
//            @Param("refrigeratorName") String refrigeratorName,
//            @Param("productName") String productName);
    //이 쿼리문은 기존 냉장고에 같은 이름이 있는 것을 찾는 코드이다, 지금은 유통기한 이슈로 사용안하도록함,,,,,,

    @Query("MATCH (f:food {id: $id}) " +
            "MATCH (r:RefrigeRator {refrigeratorName: $refrigeratorName}) " +
            "MERGE (r)-[:STORED_IN]->(f)")
    void createRelationship(@Param("id") String id, @Param("refrigeratorName") String refrigeratorName);

    //검색 조회
    @Query("MATCH (r:RefrigeRator {refrigeratorName: $refrigeratorName})-[:STORED_IN]->(f:food) " +
            "WHERE f.productName CONTAINS $productName " +
            "RETURN f")
    List<Barcode> KeywordSearchFood(@Param("refrigeratorName") String refrigeratorName, @Param("productName") String productName);


//    @Query("match (f:food{productName:$productName}) return f;")
@Query("MATCH (u:user {id: $userid})-[:OWNS]->(r:RefrigeRator) " +
        "MATCH (r)-[:STORED_IN]->(f:food) " +
        "WHERE f.productName CONTAINS $productName " +
        "RETURN f")
List<Barcode> SearchAllFood(@Param("userid") String userid, @Param("productName") String productName);

    /*
    Neo4jRepository에서 엔티티를 삭제할 때는 기본적으로 엔티티의 ID를 사용합니다.
    하지만, 원자재성 식품 같은 경우 바코드가 없을경우를 대비해서
    쿼리문을 작성해서 이름으로 삭제하려고 수정함.


        void deleteByBarcode(String productName);
     */

    //삭제
    @Query("MATCH (b:food {productName: $productName, id: $id})" +
            "DETACH DELETE b")
    void deleteByProductName(String productName,String id);


    //수량 수정
    @Query("MATCH (f:food {productName: $productName, id: $id}) SET f.count = $count RETURN f")
    void updateByProductNave(String productName,String id,String count);

    //조회문
    @Query("MATCH (r:RefrigeRator {refrigeratorName: $refrigeratorName})-[:STORED_IN]->(f:food) RETURN f")
    List<Barcode> findFoodsByRefrigeratorName(@Param("refrigeratorName") String refrigeratorName);
    //lcategory 조회문

    @Query("MATCH (r:RefrigeRator {refrigeratorName: $refrigeratorName})-[:STORED_IN]->(f:food {lcategory: $lcategory}) RETURN f")
    List<Barcode> findFoodsByRefrigeratorNameAndCategory(@Param("refrigeratorName") String refrigeratorName, @Param("lcategory") String lcategory);

}
