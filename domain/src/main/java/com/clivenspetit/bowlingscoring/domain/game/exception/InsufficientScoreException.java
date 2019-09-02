package com.clivenspetit.bowlingscoring.domain.game.exception;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class InsufficientScoreException extends RuntimeException {

    public InsufficientScoreException() {
        super("Invalid frame position.");
    }
}
