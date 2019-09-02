package com.clivenspetit.bowlingscoring.domain.parser;

import com.clivenspetit.bowlingscoring.domain.utility.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class TabSeparatedScoreParserTest {

    private static final String SAMPLE_ALL_STRIKES_FILE_PATH = "stubs/sample_all_strikes_game.txt";
    private static final String SAMPLE_ALL_STRIKES_FILE_PATH_WITH_INVALID_SCORE
            = "stubs/sample_all_strikes_game_with_invalid_score.txt";
    private static final String SAMPLE_ALL_STRIKES_FILE_PATH_WITH_INVALID_PLAYER_NAME
            = "stubs/sample_all_strikes_game_with_invalid_player_name.txt";

    private ScoreParser scoreParser;
    private String realPath;
    private String allStrikeContent;
    private String realPathWithInvalidScore;
    private String allStrikeWithInvalidScoreContent;
    private String realPathWithInvalidPlayerName;
    private String allStrikeWithInvalidPlayerNameContent;

    @Before
    public void setUp() throws Exception {
        scoreParser = new TabSeparatedScoreParser();

        realPath = FileUtils.getAbsoluteFilePathForResource(this.getClass(), SAMPLE_ALL_STRIKES_FILE_PATH);
        allStrikeContent = new String(Files.readAllBytes(Paths.get(realPath)), StandardCharsets.UTF_8);

        realPathWithInvalidScore = FileUtils.getAbsoluteFilePathForResource(this.getClass(),
                SAMPLE_ALL_STRIKES_FILE_PATH_WITH_INVALID_SCORE);

        allStrikeWithInvalidScoreContent = new String(Files.readAllBytes(Paths.get(realPathWithInvalidScore)),
                StandardCharsets.UTF_8);

        realPathWithInvalidPlayerName = FileUtils.getAbsoluteFilePathForResource(this.getClass(),
                SAMPLE_ALL_STRIKES_FILE_PATH_WITH_INVALID_PLAYER_NAME);

        allStrikeWithInvalidPlayerNameContent = new String(Files.readAllBytes(Paths.get(realPathWithInvalidPlayerName)),
                StandardCharsets.UTF_8);
    }

    @After
    public void tearDown() throws Exception {
        scoreParser = null;
        realPath = null;
        allStrikeContent = null;
        realPathWithInvalidScore = null;
        allStrikeWithInvalidScoreContent = null;
        realPathWithInvalidPlayerName = null;
        allStrikeWithInvalidPlayerNameContent = null;
    }

    @Test(expected = NullPointerException.class)
    public void parse_nullArgumentPassed_throwException() throws Exception {
        scoreParser.parse(null);
    }

    @Test
    public void parse_allStrikePassed_playerMapReturned() throws Exception {
        Map<String, List<String>> players = scoreParser.parse(allStrikeContent);

        assertThat(players.size(), is(1));

        players.forEach((key, value) -> {
            assertThat(key, is("Carl"));

            value.forEach(score -> assertThat("10", is(score)));
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_invalidScorePassed_throwException() throws Exception {
        scoreParser.parse(allStrikeWithInvalidScoreContent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_invalidPlayerNamePassed_throwException() throws Exception {
        scoreParser.parse(allStrikeWithInvalidPlayerNameContent);
    }
}
