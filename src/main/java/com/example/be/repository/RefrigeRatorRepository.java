package com.example.be.repository;

import com.example.be.Request.InviteRefrigeratorRequset;
import com.example.be.data.entity.RefrigeRator;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RefrigeRatorRepository extends Neo4jRepository<RefrigeRator,String> {



    //삭제
//    @Query("MATCH (r:RefrigeRator {refrigerator_id: $refrigerator_id})-[rel]-() WITH rel, r MATCH (r)-[:STORED_IN*]-(n) DELETE rel, r, n")
    @Query("MATCH (r:RefrigeRator {refrigerator_id: $refrigerator_id}) OPTIONAL MATCH (r)-[rel]-() OPTIONAL MATCH (r)-[:STORED_IN*]-(n) DELETE rel, r, n")
    void deleteById(String refrigerator_id);


    //수정_이거 안씀
    @Query("MATCH (r:RefrigeRator {refrigerator_id: $refrigerator_id}) SET r.refrigerator_name = $refrigerator_name RETURN r")
    RefrigeRator updateByName(String refrigerator_id, String refrigerator_name);

    //조회
    @Query("MATCH (r:RefrigeRator) WHERE r.id = $id RETURN r")
//    @Query("MATCH (r:RefrigeRator {id: $id})<-[:OWNS]-(u:user) RETURN  r")
    List<RefrigeRator> findByIdAndRefrigeratorId(String id);

    //수정
    @Query("MATCH (r:RefrigeRator {id:$id, refrigerator_id:$refrigerator_id}) SET r.refrigeratorName=$refrigeratorName RETURN r")
    RefrigeRator updateRefrigeratorName(String id, String refrigerator_id, String refrigeratorName);

////    @Query("MATCH (u:user {id: $userId}), (r:RefrigeRator {refrigerator_id: $refrigerator_id}) OPTIONAL MATCH (u)-[rel:OWNS]->(r) WITH u, r, rel WHERE rel IS NULL MERGE (u)-[:OWNS]->(r) RETURN u, r")
//    @Query("MATCH (u:user {id: $userId}), (r:RefrigeRator {refrigerator_id: $refrigerator_id}) OPTIONAL MATCH (u)-[rel:OWNS]->(r) WITH u, r, rel WHERE rel IS NULL MERGE (u)-[:OWNS]->(r) RETURN u, r")
//    void inviteRefrigerator(String userId, String refrigerator_id);

    @Query("MATCH (u:user {id: $userId}), (r:RefrigeRator {refrigerator_id: $refrigerator_id}) " +
            "OPTIONAL MATCH (u)-[rel:OWNS]->(r) " +
            "WITH u, r, rel " +
            "WHERE rel IS NULL " +
            "MERGE (u)-[:OWNS]->(r) " +
            "RETURN u, r")
    List<InviteRefrigeratorRequset> inviteRefrigerator(@Param("userId") String userId, @Param("refrigerator_id") String refrigeratorId);




    //조회
    Optional<RefrigeRator> findByRefrigeratorName(String refrigeratorName);

}
