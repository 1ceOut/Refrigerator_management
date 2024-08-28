package com.example.be.repository;

import com.example.be.data.entity.RefrigeRator;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;


public interface RefrigeRatorRepository extends Neo4jRepository<RefrigeRator,String> {



    //삭제
    @Query("MATCH (r:RefrigeRator {refrigerator_id: $refrigerator_id})-[rel]-() WITH rel, r MATCH (r)-[:STORED_IN*]-(n) DELETE rel, r, n")
    void deleteById(String refrigerator_id);


    //수정
    @Query("MATCH (r:RefrigeRator {refrigerator_id: $refrigerator_id}) SET r.refrigerator_name = $refrigerator_name RETURN r")
    RefrigeRator updateByName(String refrigerator_id, String refrigerator_name);

    @Query("MATCH (r:RefrigeRator) WHERE r.id = $id RETURN r")
    List<RefrigeRator> findByIdAndRefrigeratorId(String id);


    @Query("MATCH (r:RefrigeRator {id:$id, refrigerator_id:$refrigerator_id}) SET r.refrigeratorName=$refrigeratorName RETURN r")
    RefrigeRator updateRefrigeratorName(String id, String refrigerator_id, String refrigeratorName);


    //조회
    Optional<RefrigeRator> findByRefrigeratorName(String refrigeratorName);

}
