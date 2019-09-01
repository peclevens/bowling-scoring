package com.clivenspetit.bowlingscoring.domain.parser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class TabSeparatedScoreParser implements ScoreParser {

    /**
     * Parse content to build a map of all players and their respective scores.
     *
     * @param content
     * @return
     */
    @Override
    public Map<String, List<Character>> parse(String content) {
        Objects.requireNonNull(content, "Content cannot be null.");

        Map<String, List<Character>> players = new LinkedHashMap<>();

        return players;
    }
}
