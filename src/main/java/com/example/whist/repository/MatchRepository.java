package com.example.whist.repository;

import com.example.whist.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("SELECT DISTINCT m FROM Match m LEFT JOIN FETCH m.players ORDER BY m.playedAt DESC")
    List<Match> findAllWithPlayersOrderByPlayedAtDesc();
}
