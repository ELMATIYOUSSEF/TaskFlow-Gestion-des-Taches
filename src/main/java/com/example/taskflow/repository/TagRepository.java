package com.aftasapi.repository;

import com.aftasapi.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FishRepository extends JpaRepository<Fish,Long> {
    Optional<Fish> findByName(String name);
    @Query("SELECT f.level.point FROM Fish f WHERE f.name = :fishName")
    Integer findPointByName(@Param("fishName") String fishName);
}
