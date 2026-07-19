package com.example.whist.service;

import com.example.whist.game.RoundSchedule;
import com.example.whist.game.WhistGame;
import com.example.whist.model.Match;
import com.example.whist.model.MatchPlayer;
import com.example.whist.repository.MatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ScoreService {

    private final MatchRepository matchRepository;
    private WhistGame currentGame;

    public ScoreService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public WhistGame getCurrentGame() {
        return currentGame;
    }

    public boolean hasActiveGame() {
        return currentGame != null && !currentGame.isFinished();
    }

    public void startGame(List<String> names) {
        List<String> cleaned = new ArrayList<>();
        for (String name : names) {
            if (name != null && !name.isBlank()) {
                cleaned.add(name.trim());
            }
        }
        if (cleaned.size() < RoundSchedule.MIN_PLAYERS || cleaned.size() > RoundSchedule.MAX_PLAYERS) {
            throw new IllegalArgumentException(
                    "Introdu intre " + RoundSchedule.MIN_PLAYERS + " si " + RoundSchedule.MAX_PLAYERS + " jucatori");
        }
        currentGame = new WhistGame(cleaned);
    }

    public void submitRound(List<Integer> bets, List<Integer> hands) {
        if (currentGame == null) {
            throw new IllegalStateException("Nu exista un joc activ");
        }
        currentGame.submitRound(bets, hands);
        if (currentGame.isFinished()) {
            persistFinishedGame();
        }
    }

    public void abandonGame() {
        currentGame = null;
    }

    public List<Match> getMatchHistory() {
        return matchRepository.findAllWithPlayersOrderByPlayedAtDesc();
    }

    private void persistFinishedGame() {
        Match match = new Match();
        match.setPlayedAt(LocalDateTime.now());

        int[] scores = currentGame.getScores();
        List<String> names = currentGame.getPlayerNames();
        for (int i = 0; i < names.size(); i++) {
            MatchPlayer mp = new MatchPlayer();
            mp.setPlayerName(names.get(i));
            mp.setScore(scores[i]);
            match.addPlayer(mp);
        }
        matchRepository.save(match);
    }
}
