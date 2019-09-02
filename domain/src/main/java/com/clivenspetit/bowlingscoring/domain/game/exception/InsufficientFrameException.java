package com.clivenspetit.bowlingscoring.domain.game.exception;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class InsufficientFrameException extends RuntimeException {

    public InsufficientFrameException() {
        super("Invalid frame position.");
    }
}
