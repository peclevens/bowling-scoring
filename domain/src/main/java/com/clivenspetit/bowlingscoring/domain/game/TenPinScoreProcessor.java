package com.clivenspetit.bowlingscoring.domain.game;

import com.clivenspetit.bowlingscoring.domain.game.exception.InsufficientScoreException;
import com.clivenspetit.bowlingscoring.domain.parser.ScoreParser;
import com.google.common.cache.Cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class TenPinScoreProcessor extends AbstractScoreProcessor {

    public TenPinScoreProcessor(
            ScoreParser scoreParser, Cache<Integer, Map<String, List<String>>> parsedGameCache,
            Cache<Integer, List<Player>> playersCache) {

        super(scoreParser, parsedGameCache, playersCache);
    }

    /**
     * Process and calculate player scores.
     *
     * @param playerScores
     * @return
     */
    @Override
    protected List<Player> process(final Map<String, List<String>> playerScores) {
        Objects.requireNonNull(playerScores, "Play map score cannot be null.");

        final List<Player> players = new ArrayList<>();

        // Iterate player scores and calculate frame score
        playerScores.forEach((playerName, scores) -> {
            Player player = new Player(playerName, new Frame[10]);

            // Fill this player frames
            fillPlayerFrames(player, scores);

            // Calculate frames score
            calculateFramesScore(player);

            // Add player
            players.add(player);
        });

        return players;
    }

    /**
     * Process and fill all frames from a player.
     *
     * @param player
     * @param scores
     */
    public void fillPlayerFrames(final Player player, final List<String> scores) {
        int scoreIndex = 0, frameIndex = 0, totalFrame = player.getFrames().length;

        try {
            // Build frames
            while (frameIndex < (totalFrame - 1)) {
                Frame frame = new Frame();

                // Process first frame first ball
                char score = translateScore('\0', getScoreAt(scoreIndex, scores));
                if (score == 'X') frame.setSecondBallScore(score);
                else frame.setFirstBallScore(score);

                // Process first frame second ball
                if (score != 'X') {
                    frame.setSecondBallScore(translateScore(frame.getFirstBallScore(),
                            getScoreAt(++scoreIndex, scores)));
                }

                // Add frame
                player.addFrame(frameIndex, frame);

                scoreIndex++;
                frameIndex++;
            }

            scoreIndex = processLastFrame(player, scores, scoreIndex, frameIndex);

            // Make sure we don't
            if (scoreIndex != (scores.size() - 1)) {
                throw new IllegalArgumentException(String.format("Too many score provided for player %s.",
                        player.getName()));
            }
        } catch (InsufficientScoreException ex) {
            throw new IllegalArgumentException(String.format("Insufficient score for player %s.", player.getName()));
        }
    }

    private int processLastFrame(Player player, List<String> scores, int scoreIndex, int frameIndex) {
        // Handle the last frame
        Frame lastFrame = new Frame();

        lastFrame.setFirstBallScore(translateScore('\0', getScoreAt(scoreIndex, scores)));
        if (lastFrame.getFirstBallScore() == 'X') {
            lastFrame.setSecondBallScore(translateScore(lastFrame.getFirstBallScore(),
                    getScoreAt(++scoreIndex, scores)));

            lastFrame.setThirdBallScore(translateScore(lastFrame.getFirstBallScore(),
                    getScoreAt(++scoreIndex, scores)));
        } else if (lastFrame.getFirstBallScore() != '/') { // The first ball pin-fall is assumed to be between 0-9
            lastFrame.setSecondBallScore(translateScore(lastFrame.getFirstBallScore(),
                    getScoreAt(++scoreIndex, scores)));

            // Process the third row only If the previous roll was a strike or spare
            if ((scoreIndex + 1) < scores.size() && (lastFrame.getSecondBallScore() == 'X'
                    || lastFrame.getSecondBallScore() == '/')) {

                lastFrame.setThirdBallScore(translateScore(lastFrame.getSecondBallScore(),
                        getScoreAt(++scoreIndex, scores)));
            }
        }

        // Add frame
        player.addFrame(frameIndex, lastFrame);
        return scoreIndex;
    }

    /**
     * Calculate the score for all frames of particular player.
     *
     * @param player
     */
    public void calculateFramesScore(Player player) {
        Frame[] frames = player.getFrames();

        for (int index = 0; index < frames.length; index++) {
            Frame frame = frames[index];
            int total;

            if (frame.getSecondBallScore() == 'X') { // Calculate strikes
                total = calculateStrike(index, frame, frames);
            } else if (frame.getSecondBallScore() == '/') { // Calculate spare
                total = calculateSpare(index, frame, frames);
            } else { // Calculate open
                total = calculateScore(frame.getFirstBallScore())
                        + calculateScore(frame.getSecondBallScore());
            }

            // Calculate and/or set frame total score
            Frame previousFrame = getFrameAt(index - 1, frames);
            if (previousFrame != null) {
                frame.setScore(previousFrame.getScore() + total);
            } else {
                frame.setScore(total);
            }
        }

        // Adjust last frame score value if necessary
        Frame lastFrame = frames[frames.length - 1];
        if (lastFrame.getThirdBallScore() != '\0') {
            lastFrame.setScore(lastFrame.getScore() + calculateScore(lastFrame.getThirdBallScore()));
        }
    }

    /**
     * Calculate frame strike score
     *
     * @param index
     * @param frame
     * @param frames
     * @return
     */
    private int calculateStrike(int index, Frame frame, Frame[] frames) {
        int total;
        Frame firstNextFrame = getFrameAt(index + 1, frames);

        if (firstNextFrame != null && firstNextFrame.getSecondBallScore() == 'X') {
            Frame secondNextFrame = getFrameAt(index + 2, frames);
            if (secondNextFrame != null && secondNextFrame.getFirstBallScore() != '\0') {
                total = calculateScore(frame.getSecondBallScore())
                        + calculateScore(firstNextFrame.getSecondBallScore())
                        + calculateScore(secondNextFrame.getFirstBallScore());
            } else if (secondNextFrame != null) {
                total = calculateScore(frame.getSecondBallScore())
                        + calculateScore(frame.getSecondBallScore())
                        + calculateScore(secondNextFrame.getSecondBallScore());
            } else {
                total = calculateScore(frame.getSecondBallScore())
                        + calculateScore(firstNextFrame.getFirstBallScore())
                        + calculateScore(firstNextFrame.getSecondBallScore());
            }
        } else if (firstNextFrame != null && firstNextFrame.getSecondBallScore() == '/') {
            total = calculateScore(frame.getSecondBallScore())
                    + calculateScore(firstNextFrame.getSecondBallScore());
        } else if (firstNextFrame != null) {
            total = calculateScore(frame.getSecondBallScore())
                    + calculateScore(firstNextFrame.getFirstBallScore())
                    + calculateScore(firstNextFrame.getSecondBallScore());
        } else {
            total = calculateScore(frame.getFirstBallScore())
                    + calculateScore(frame.getSecondBallScore());
        }

        return total;
    }

    /**
     * Calculate frame spare score
     *
     * @param index
     * @param frame
     * @param frames
     * @return
     */
    private int calculateSpare(int index, Frame frame, Frame[] frames) {
        int total;
        Frame firstNextFrame = getFrameAt(index + 1, frames);

        if (firstNextFrame != null && firstNextFrame.getSecondBallScore() == 'X') {
            total = calculateScore(frame.getSecondBallScore())
                    + calculateScore(firstNextFrame.getSecondBallScore());
        } else if (firstNextFrame != null) {
            total = calculateScore(frame.getSecondBallScore())
                    + calculateScore(firstNextFrame.getFirstBallScore());
        } else {
            total = calculateScore(frame.getSecondBallScore());
        }

        return total;
    }

    /**
     * Calculate score for frame's roll.
     *
     * @param score
     * @return
     */
    public int calculateScore(final char score) {
        if (score == 'X' || score == '/') {
            return 10;
        } else if (score == 'F') {
            return 0;
        } else if (score >= '0' && score <= '9') {
            return Character.getNumericValue(score);
        }

        throw new IllegalArgumentException(String.format("Invalid ten-pin bowling score. " + "Score: %c.", score));
    }

    /**
     * Translate raw score to bowling friendly characters.
     *
     * @param previousScore
     * @param score
     * @return
     */
    public char translateScore(final char previousScore, final String score) {
        if (previousScore >= '0' && previousScore <= '9' && score.length() == 1
                && score.charAt(0) >= '0' && score.charAt(0) <= '9'
                && ((Character.getNumericValue(previousScore) + Character.getNumericValue(score.charAt(0))) == 10)) {

            return '/';
        } else if (score.length() == 1 && score.charAt(0) >= '0' && score.charAt(0) <= '9') {
            return score.charAt(0);
        } else if (score.length() == 1 && score.charAt(0) == 'F') {
            return 'F';
        } else if ("10".equals(score)) {
            return 'X';
        }

        throw new IllegalArgumentException(String.format("Invalid ten-pin bowling score. " +
                "Previous Score: %c, Score: %s.", previousScore, score));
    }

    /**
     * Get the score at a specific index.
     *
     * @param index
     * @param scores
     * @return
     */
    private String getScoreAt(final int index, final List<String> scores) {
        if (index >= scores.size())
            throw new InsufficientScoreException();

        return scores.get(index);
    }

    /**
     * Get the frame at a specific index.
     *
     * @param index
     * @param frames
     * @return
     */
    private Frame getFrameAt(final int index, final Frame[] frames) {
        if (index < 0 || index >= frames.length)
            return null;

        return frames[index];
    }
}
