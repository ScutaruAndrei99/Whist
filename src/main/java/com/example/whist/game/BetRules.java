package com.example.whist.game;

import java.util.ArrayList;
import java.util.List;

public final class BetRules {

    private BetRules() {
    }

    /** Pariul unui jucator: 0 .. cards (cartile din runda). */
    public static void validateSingleBet(int bet, int cards, String playerName) {
        if (bet < 0 || bet > cards) {
            throw new IllegalArgumentException(
                    playerName + ": pariu invalid. Maximul in aceasta runda este " + cards);
        }
    }

    /**
     * Suma tuturor pariurilor nu poate fi egala cu numarul de carti din runda.
     * Restrictia se aplica practic ultimului jucator.
     */
    public static void validateBetSum(List<Integer> bets, int cards) {
        int sum = bets.stream().mapToInt(Integer::intValue).sum();
        if (sum == cards) {
            throw new IllegalArgumentException(
                    "Suma pariurilor nu poate fi egala cu " + cards
                            + " (restrictie pe ultimul jucator)");
        }
    }

    /**
     * Valorile de pariu permise pentru ultimul jucator, date fiind pariurile celorlalti.
     */
    public static List<Integer> allowedBetsForLastPlayer(List<Integer> previousBets, int cards) {
        int sumPrev = previousBets.stream().mapToInt(Integer::intValue).sum();
        List<Integer> allowed = new ArrayList<>();
        for (int bet = 0; bet <= cards; bet++) {
            if (sumPrev + bet != cards) {
                allowed.add(bet);
            }
        }
        return allowed;
    }
}
