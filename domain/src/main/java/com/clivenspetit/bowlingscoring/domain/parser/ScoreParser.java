package com.clivenspetit.bowlingscoring.domain.parser;

import java.util.List;
import java.util.Map;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public interface ScoreParser {

    /**
     * Parse content to build a map of all players and their respective scores.
     *
     * @param content
     * @return
     */
    Map<String, List<String>> parse(String content);
}
