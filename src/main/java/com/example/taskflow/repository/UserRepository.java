package com.aftasapi.repository;

import com.aftasapi.entity.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, String> {
    Optional<Competition> findByDate(Date date);

    Page<Competition> findAll(Pageable pageable);

    Optional<Competition> findByCode(String code);

    Optional<Competition> deleteByCode(String code);
    Page<Competition> findByCodeContaining(String name, Pageable pageable);
}