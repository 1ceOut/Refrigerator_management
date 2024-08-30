package com.example.be.repository;

import com.example.be.data.entity.RefrigeRator;
import com.example.be.data.entity.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User,String> {

    @Query("MATCH (u:user {id: $userId}) RETURN u")
    Optional<User> findByUserID(String userId);

    @Query("MATCH (u:user) WHERE u.id IN $userIds RETURN u")
    List<User> findByUserIds(List<String> userIds);


    @Query("MATCH (u:user {id: $userId})-[:OWNS]->(r:RefrigeRator) RETURN r.refrigerator_id AS refrigerator_id, r.refrigeratorName AS refrigeratorName")
    List<RefrigeRator> findRefrigeratorsByUserId(String userId);

}
