package com.clivenspetit.bowlingscoring.domain.game;

import com.clivenspetit.bowlingscoring.domain.game.validator.Validator;
import com.clivenspetit.bowlingscoring.domain.parser.ScoreParser;
import com.google.common.cache.Cache;

import java.util.List;
import java.util.Map;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class TenPinAbstractScoreProcessor extends AbstractScoreProcessor {

    public TenPinAbstractScoreProcessor(
            ScoreParser scoreParser, Validator<Player> playerValidator,
            Cache<Integer, Map<String, List<Character>>> parsedGameCache, Cache<Integer, List<Player>> playersCache) {

        super(scoreParser, playerValidator, parsedGameCache, playersCache);
    }

    /**
     * Process and calculate player scores.
     *
     * @param scores
     * @return
     */
    @Override
    protected List<Player> process(Map<String, List<Character>> scores) {
        return null;
    }
}
