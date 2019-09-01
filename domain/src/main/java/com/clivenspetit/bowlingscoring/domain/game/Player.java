package com.clivenspetit.bowlingscoring.domain.game;

import java.util.Objects;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class Player {

    private static final int MAX_GAME_FRAME = 10;

    private String name;
    private Frame[] frames = new Frame[MAX_GAME_FRAME];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name, "Player name cannot be null.");

        this.name = name;
    }

    public Frame[] getFrames() {
        return frames;
    }

    public void setFrames(Frame[] frames) {
        this.frames = frames;
    }

    public void setFrame(int index, Frame frame) {
        Objects.requireNonNull(frame, "Frame cannot be null.");

        if (index < 0 || index >= MAX_GAME_FRAME) {
            throw new IllegalArgumentException(String.format("Frame index should be between 0 to %d exclusive.",
                    MAX_GAME_FRAME));
        }

        this.frames[index] = frame;
    }

    public short calculateScore() {
        Frame frame = frames[MAX_GAME_FRAME - 1];
        return frame != null ? frame.getScore() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
