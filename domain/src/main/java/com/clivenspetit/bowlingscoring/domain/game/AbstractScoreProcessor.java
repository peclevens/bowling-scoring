package com.clivenspetit.bowlingscoring.domain.game;

import com.clivenspetit.bowlingscoring.domain.parser.ScoreParser;
import com.google.common.cache.Cache;

import java.util.List;
import java.util.Map;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public abstract class AbstractScoreProcessor {

    private final ScoreParser scoreParser;
    private final Cache<Integer, Map<String, List<String>>> parsedGameCache;
    private final Cache<Integer, List<Player>> playersCache;

    public AbstractScoreProcessor(
            ScoreParser scoreParser, Cache<Integer, Map<String, List<String>>> parsedGameCache,
            Cache<Integer, List<Player>> playersCache) {

        this.scoreParser = scoreParser;
        this.parsedGameCache = parsedGameCache;
        this.playersCache = playersCache;
    }

    /**
     * Process and calculate player scores.
     *
     * @return
     */
    public List<Player> process(String content) {
        // Calculate a unique key for this content
        Integer contentKey = content.hashCode();

        // Try to retrieve parse content from cache
        Map<String, List<String>> scores = parsedGameCache.getIfPresent(contentKey);

        // Game content is not cached yet, parse and cache it
        if (scores == null) {
            scores = scoreParser.parse(content);

            // Cache game content
            parsedGameCache.put(contentKey, scores);
        }

        // Try to retrieve players from cache
        List<Player> players = playersCache.getIfPresent(contentKey);

        // No players found, process the game
        if (players == null) {
            // Calculate player score
            players = this.process(scores);

            // Cache players for this game
            playersCache.put(contentKey, players);
        }

        return players;
    }

    /**
     * Process and calculate player scores.
     *
     * @param scores
     * @return
     */
    protected abstract List<Player> process(Map<String, List<String>> scores);
}
