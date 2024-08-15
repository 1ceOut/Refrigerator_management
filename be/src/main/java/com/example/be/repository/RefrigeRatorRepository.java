package com.example.be.repository;

import com.example.be.data.entity.RefrigeRator;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;


public interface RefrigeRatorRepository extends Neo4jRepository<RefrigeRator,String> {

    //삭제
    @Query("MATCH (r:RefrigeRator {refrigerator_name: $refrigerator_name}) DELETE r RETURN r")
    void deleteByName(String refrigerator_name);

    //수정
    @Query("MATCH (r:RefrigeRator {refrigerator_id: $refrigerator_id}) SET r.refrigerator_name = $refrigerator_name RETURN r")
    RefrigeRator updateByName(String refrigerator_id, String refrigerator_name);


    //조회
    List<RefrigeRator> findAll();
}
