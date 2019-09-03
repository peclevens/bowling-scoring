package com.clivenspetit.bowlingscoring.domain.utility;

import com.clivenspetit.bowlingscoring.domain.game.Frame;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class StubFrame extends Frame {

    public StubFrame(char firstBallScore, char secondBallScore, char thirdBallScore, int score) {
        setFirstBallScore(firstBallScore);
        setSecondBallScore(secondBallScore);
        setThirdBallScore(thirdBallScore);
        setScore(score);
    }
}
