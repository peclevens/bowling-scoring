package com.clivenspetit.bowlingscoring.domain.game;

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
            calculateFramesScore(player.getFrames());

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

        // Handle the last frame
        Frame lastFrame = new Frame();

        lastFrame.setFirstBallScore(translateScore('\0', getScoreAt(scoreIndex, scores)));
        if (lastFrame.getFirstBallScore() == 'X') {
            lastFrame.setSecondBallScore(translateScore(lastFrame.getFirstBallScore(),
                    getScoreAt(++scoreIndex, scores)));

            lastFrame.setThirdBallScore(translateScore(lastFrame.getFirstBallScore(),
                    getScoreAt(++scoreIndex, scores)));
        } else if (lastFrame.getFirstBallScore() != '/') {
            lastFrame.setSecondBallScore(translateScore(lastFrame.getFirstBallScore(),
                    getScoreAt(++scoreIndex, scores)));

            lastFrame.setThirdBallScore(translateScore(lastFrame.getSecondBallScore(),
                    getScoreAt(++scoreIndex, scores)));
        }

        // Add frame
        player.addFrame(frameIndex, lastFrame);
    }

    /**
     * Calculate the score for all frames.
     *
     * @param frames
     */
    public void calculateFramesScore(Frame[] frames) {

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
     * Get the at a specific index.
     *
     * @param index
     * @param scores
     * @return
     */
    private String getScoreAt(final int index, final List<String> scores) {
        if (index >= scores.size())
            throw new IllegalArgumentException("Insufficient score.");

        return scores.get(index);
    }
}
