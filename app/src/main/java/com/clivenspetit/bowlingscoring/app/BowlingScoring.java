package com.clivenspetit.bowlingscoring.app;

import com.clivenspetit.bowlingscoring.domain.game.AbstractScoreProcessor;
import com.clivenspetit.bowlingscoring.domain.game.Frame;
import com.clivenspetit.bowlingscoring.domain.game.Player;
import com.clivenspetit.bowlingscoring.domain.game.repository.BowlingRepository;
import com.google.common.cache.Cache;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class BowlingScoring {

    private static final Logger logger = LogManager.getLogger(BowlingScoring.class);

    private final BowlingRepository bowlingRepository;
    private final AbstractScoreProcessor scoreProcessor;
    private final Cache<String, String> gameResultCache;

    @Inject
    public BowlingScoring(
            BowlingRepository bowlingRepository, AbstractScoreProcessor scoreProcessor,
            @Named("gameResult") Cache<String, String> gameResultCache) {

        this.bowlingRepository = bowlingRepository;
        this.scoreProcessor = scoreProcessor;
        this.gameResultCache = gameResultCache;
    }

    /**
     * Process the game file and display the game score
     *
     * @param filePath
     * @throws IOException
     */
    public void printGameScore(final String filePath) throws IOException {
        logger.debug("Processing score for game file: {}", filePath);

        try {
            // Read the file content
            String content = bowlingRepository.loadGameResultFromFile(filePath);

            // Process the game content
            List<Player> players = scoreProcessor.process(content);

            // Try to retrieve game result from cache
            String gameResult = gameResultCache.getIfPresent(filePath);
            if (gameResult == null) {
                gameResult = buildGameResult(players);

                // Cache game result
                gameResultCache.put(filePath, gameResult);
            }

            // Display player on screen
            System.out.println(gameResult);
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * Build score for a list of player
     *
     * @param players
     * @return
     */
    private String buildGameResult(final List<Player> players) {
        final StringBuilder resultBuilder = new StringBuilder();
        final String frameTitle = "Frame\t\t1\t\t2\t\t3\t\t4\t\t5\t\t6\t\t7\t\t8\t\t9\t\t10";

        // Append frame title
        resultBuilder.append(frameTitle)
                .append("\n");

        // Build output for each player
        players.forEach(player -> {
            // Append player name
            resultBuilder.append(player.getName())
                    .append("\n");

            // Append player score
            String playerResult = buildPlayerResult(player.getFrames());
            resultBuilder.append(playerResult)
                    .append("\n");

        });

        return resultBuilder.toString().trim();
    }

    /**
     * Build score for a single player frame
     *
     * @param frames
     * @return
     */
    private String buildPlayerResult(final Frame[] frames) {
        StringBuilder pinFallsBuilder = new StringBuilder("PinFalls\t");
        StringBuilder scoreBuilder = new StringBuilder("Score\t\t");

        for (int index = 0; index < frames.length; index++) {
            Frame frame = frames[index];

            // Build pin falls
            if (index == (frames.length - 1)) {
                pinFallsBuilder.append(String.format("%c\t%c\t%c\t",
                        convertNullCharToOneSpace(frame.getFirstBallScore()),
                        convertNullCharToOneSpace(frame.getSecondBallScore()),
                        convertNullCharToOneSpace(frame.getThirdBallScore())));
            } else {
                pinFallsBuilder.append(String.format("%c\t%c\t", convertNullCharToOneSpace(frame.getFirstBallScore()),
                        convertNullCharToOneSpace(frame.getSecondBallScore())));
            }

            // Build score
            scoreBuilder.append(String.format("%d\t\t", frame.getScore()));
        }

        return pinFallsBuilder.toString().trim() + "\n" + scoreBuilder.toString().trim();
    }

    private char convertNullCharToOneSpace(final char c) {
        return c == '\0' ? ' ' : c;
    }
}
