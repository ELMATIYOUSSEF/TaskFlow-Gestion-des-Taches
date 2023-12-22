package com.aftasapi.repository;

import com.aftasapi.entity.Hunting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuntingRepository extends JpaRepository<Hunting,Long> {
    @Query("SELECT h FROM Hunting h WHERE h.competition.code = :competitionCode")
    List<Hunting> findHuntingByCompetitionCode(@Param("competitionCode") String competitionCode);
}
