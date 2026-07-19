package com.example.whist.game;

import java.util.ArrayList;
import java.util.List;

public class WhistGame {

    private final List<String> playerNames;
    private final List<GameRound> rounds;
    private final int[] scores;
    private int currentRoundIndex;
    private boolean finished;

    public WhistGame(List<String> playerNames) {
        this.playerNames = List.copyOf(playerNames);
        List<Integer> schedule = RoundSchedule.build(playerNames.size());
        this.rounds = new ArrayList<>();
        for (int i = 0; i < schedule.size(); i++) {
            rounds.add(new GameRound(
                    i,
                    schedule.get(i),
                    RoundSchedule.phaseOf(i, playerNames.size()),
                    RoundSchedule.isPhaseEnd(i, playerNames.size(), schedule.size()),
                    playerNames.size()
            ));
        }
        this.scores = new int[playerNames.size()];
        this.currentRoundIndex = 0;
        this.finished = false;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public List<GameRound> getRounds() {
        return rounds;
    }

    public int getPlayerCount() {
        return playerNames.size();
    }

    public int getCurrentRoundIndex() {
        return currentRoundIndex;
    }

    public GameRound getCurrentRound() {
        if (finished) {
            return null;
        }
        return rounds.get(currentRoundIndex);
    }

    public boolean isFinished() {
        return finished;
    }

    public int[] getScores() {
        return scores;
    }

    public void submitRound(List<Integer> bets, List<Integer> hands) {
        if (finished) {
            throw new IllegalStateException("Game already finished");
        }
        if (bets.size() != playerNames.size() || hands.size() != playerNames.size()) {
            throw new IllegalArgumentException("Must provide bet and hands for every player");
        }

        GameRound round = rounds.get(currentRoundIndex);
        int cards = round.getCards();

        for (int i = 0; i < playerNames.size(); i++) {
            BetRules.validateSingleBet(bets.get(i), cards, playerNames.get(i));
            int taken = hands.get(i);
            if (taken < 0 || taken > cards) {
                throw new IllegalArgumentException("Maini invalide pentru " + playerNames.get(i)
                        + ". Maximul in aceasta runda este " + cards);
            }
        }
        BetRules.validateBetSum(bets, cards);

        int totalHands = hands.stream().mapToInt(Integer::intValue).sum();
        if (totalHands != cards) {
            throw new IllegalArgumentException("Suma mainilor luate trebuie sa fie exact " + cards);
        }

        for (int i = 0; i < playerNames.size(); i++) {
            int bet = bets.get(i);
            int taken = hands.get(i);

            boolean correct = bet == taken;
            int delta = correct ? (5 + bet) : -Math.abs(taken - bet);
            scores[i] += delta;

            RoundCell cell = round.getCells().get(i);
            cell.setBet(bet);
            cell.setHandsTaken(taken);
            cell.setCorrect(correct);
            cell.setCumulativeScore(scores[i]);
        }

        if (currentRoundIndex >= rounds.size() - 1) {
            finished = true;
        } else {
            currentRoundIndex++;
        }
    }
}
