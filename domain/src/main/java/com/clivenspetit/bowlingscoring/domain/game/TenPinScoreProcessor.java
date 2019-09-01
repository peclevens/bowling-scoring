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
     * @param scores
     * @return
     */
    @Override
    protected List<Player> process(Map<String, List<String>> scores) {
        Objects.requireNonNull(scores, "Play map score cannot be null.");

        List<Player> players = new ArrayList<>();

        return players;
    }
}
