package com.clivenspetit.bowlingscoring.domain.parser;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class TabSeparatedScoreParser implements ScoreParser {

    private static final String LINE_BREAK = "\n";
    private static final String TAB_BREAK = "\t";
    private static final String PLAYER_NAME_REGEX = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
    private static final String PLAYER_SCORE_REGEX = "^(?:[0-9]0?|F)$";

    /**
     * Parse content to build a map of all players and their respective scores.
     *
     * @param content
     * @return
     */
    @Override
    public Map<String, List<String>> parse(String content) {
        Objects.requireNonNull(content, "Content cannot be null.");

        // Player score map
        final Map<String, List<String>> players = new LinkedHashMap<>();

        // Parse each line to build a map of player name and their corresponding score
        String[] lines = content.split(LINE_BREAK);
        IntStream.range(0, lines.length)
                .filter(index -> lines[index] != null && !lines[index].trim().equals(""))
                .forEach(index -> processPlayerScore(index, lines[index], players));

        return players;
    }

    private void processPlayerScore(final int lineNumber, final String line, Map<String, List<String>> players) {
        // Split line to retrieve player name and score
        final String[] parts = line.split(TAB_BREAK);

        // Make sure this line includes exactly a tab separated player name and score
        if (parts.length != 2) {
            throw new IllegalArgumentException(String.format("Line %d should contain a tab separated " +
                    "player name and score. Ex: John\t5.", (lineNumber + 1)));
        }

        String player = parts[0];
        String score = parts[1];

        // Validate player name
        if (!player.matches(PLAYER_NAME_REGEX)) {
            throw new IllegalArgumentException(String.format("Invalid player name '%s' in line %d.",
                    player, lineNumber));
        }

        // Validate player score
        if (!score.matches(PLAYER_SCORE_REGEX)) {
            throw new IllegalArgumentException(String.format("Invalid score '%s' for player name '%s' in line %d.",
                    score, player, lineNumber));
        }

        // Add player score
        if (players.containsKey(player)) {
            players.get(player).add(score);
        } else {
            List<String> scores = new ArrayList<>();
            scores.add(score);

            players.put(player, scores);
        }
    }
}
