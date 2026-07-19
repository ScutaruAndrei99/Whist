package com.example.whist.game;

import java.util.ArrayList;
import java.util.List;

public class GameRound {

    private final int index;
    private final int cards;
    private final String phase;
    private final boolean phaseEnd;
    private final List<RoundCell> cells;

    public GameRound(int index, int cards, String phase, boolean phaseEnd, int playerCount) {
        this.index = index;
        this.cards = cards;
        this.phase = phase;
        this.phaseEnd = phaseEnd;
        this.cells = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            cells.add(new RoundCell());
        }
    }

    public int getIndex() {
        return index;
    }

    public int getCards() {
        return cards;
    }

    public String getPhase() {
        return phase;
    }

    public boolean isPhaseEnd() {
        return phaseEnd;
    }

    public List<RoundCell> getCells() {
        return cells;
    }

    public boolean isCompleted() {
        return cells.stream().allMatch(RoundCell::isCompleted);
    }
}
