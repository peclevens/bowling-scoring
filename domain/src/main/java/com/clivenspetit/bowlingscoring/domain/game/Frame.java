package com.clivenspetit.bowlingscoring.domain.game;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class Frame {

    /**
     * First ball throw score
     */
    private char firstBallScore = '\0';

    /**
     * Second ball throw score
     */
    private char secondBallScore = '\0';

    /**
     * This is to hold score for extra ball throw in the tenth frame
     */
    private char thirdBallScore = '\0';

    /**
     * The frame score
     */
    private short score = 0;

    public Frame() {

    }

    public Frame(char firstBallScore, char secondBallScore) {
        this.firstBallScore = firstBallScore;
        this.secondBallScore = secondBallScore;
    }

    public char getFirstBallScore() {
        return firstBallScore;
    }

    public void setFirstBallScore(char firstBallScore) {
        this.firstBallScore = firstBallScore;
    }

    public char getSecondBallScore() {
        return secondBallScore;
    }

    public void setSecondBallScore(char secondBallScore) {
        this.secondBallScore = secondBallScore;
    }

    public char getThirdBallScore() {
        return thirdBallScore;
    }

    public void setThirdBallScore(char thirdBallScore) {
        this.thirdBallScore = thirdBallScore;
    }

    public short getScore() {
        return score;
    }

    public void setScore(short score) {
        this.score = score;
    }
}
