package com.example.whist.game;

public class RoundCell {

    private Integer bet;
    private Integer handsTaken;
    private Integer cumulativeScore;
    private Boolean correct;

    public Integer getBet() {
        return bet;
    }

    public void setBet(Integer bet) {
        this.bet = bet;
    }

    public Integer getHandsTaken() {
        return handsTaken;
    }

    public void setHandsTaken(Integer handsTaken) {
        this.handsTaken = handsTaken;
    }

    public Integer getCumulativeScore() {
        return cumulativeScore;
    }

    public void setCumulativeScore(Integer cumulativeScore) {
        this.cumulativeScore = cumulativeScore;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public boolean isCompleted() {
        return correct != null;
    }

    public String cssClass() {
        if (correct == null) {
            return "";
        }
        return correct ? "cell-ok" : "cell-miss";
    }
}
