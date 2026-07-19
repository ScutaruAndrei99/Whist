package com.example.whist.game;

import java.util.ArrayList;
import java.util.List;

public final class RoundSchedule {

    public static final int MAX_HANDS = 8;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 6;

    private RoundSchedule() {
    }

    /**
     * N x 1, then 2..7, then N x 8, then 7..2, then N x 1.
     */
    public static List<Integer> build(int playerCount) {
        if (playerCount < MIN_PLAYERS || playerCount > MAX_PLAYERS) {
            throw new IllegalArgumentException("Players must be between " + MIN_PLAYERS + " and " + MAX_PLAYERS);
        }

        List<Integer> rounds = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            rounds.add(1);
        }
        for (int cards = 2; cards < MAX_HANDS; cards++) {
            rounds.add(cards);
        }
        for (int i = 0; i < playerCount; i++) {
            rounds.add(MAX_HANDS);
        }
        for (int cards = MAX_HANDS - 1; cards >= 2; cards--) {
            rounds.add(cards);
        }
        for (int i = 0; i < playerCount; i++) {
            rounds.add(1);
        }
        return rounds;
    }

    public static String phaseOf(int roundIndex, int playerCount) {
        int n = playerCount;
        int ascending = MAX_HANDS - 2; // 2..7 => 6
        int startOnesEnd = n;
        int ascendingEnd = startOnesEnd + ascending;
        int eightsEnd = ascendingEnd + n;
        int descendingEnd = eightsEnd + ascending;

        if (roundIndex < startOnesEnd) {
            return "ones-start";
        }
        if (roundIndex < ascendingEnd) {
            return "ascending";
        }
        if (roundIndex < eightsEnd) {
            return "eights";
        }
        if (roundIndex < descendingEnd) {
            return "descending";
        }
        return "ones-end";
    }

    public static boolean isPhaseEnd(int roundIndex, int playerCount, int totalRounds) {
        if (roundIndex >= totalRounds - 1) {
            return true;
        }
        return !phaseOf(roundIndex, playerCount).equals(phaseOf(roundIndex + 1, playerCount));
    }
}
