package com.clivenspetit.bowlingscoring.domain.game;

import com.clivenspetit.bowlingscoring.domain.IntegrationTest;
import com.clivenspetit.bowlingscoring.domain.parser.ScoreParser;
import com.clivenspetit.bowlingscoring.domain.parser.TabSeparatedScoreParser;
import com.clivenspetit.bowlingscoring.domain.utility.FileUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@Category(IntegrationTest.class)
public class TenPinScoreProcessorTest {

    private static final String SAMPLE_ALL_STRIKES_FILE_PATH = "stubs/sample_all_strikes_game.txt";
    private static final String SAMPLE_ALL_FOULS_FILE_PATH = "stubs/sample_all_fouls_game.txt";
    private static final String SAMPLE_2_PLAYERS_FILE_PATH = "stubs/sample_regular_game_01.txt";

    private ScoreParser scoreParser;
    private Cache<Integer, Map<String, List<String>>> parsedGameCache;
    private Cache<Integer, List<Player>> playersCache;
    private AbstractScoreProcessor scoreProcessor;
    private String allStrikeRealPath;
    private String allStrikeContent;
    private String allFoulRealPath;
    private String allFoulContent;
    private String twoPlayerRealPath;
    private String twoPlayerContent;

    @Before
    public void setUp() throws Exception {
        scoreParser = new TabSeparatedScoreParser();

        parsedGameCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();

        playersCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();

        scoreProcessor = new TenPinScoreProcessor(scoreParser, parsedGameCache, playersCache);

        // All strike setup
        allStrikeRealPath = FileUtils.getAbsoluteFilePathForResource(this.getClass(), SAMPLE_ALL_STRIKES_FILE_PATH);
        allStrikeContent = new String(Files.readAllBytes(Paths.get(allStrikeRealPath)), StandardCharsets.UTF_8);

        // All foul setup
        allFoulRealPath = FileUtils.getAbsoluteFilePathForResource(this.getClass(), SAMPLE_ALL_FOULS_FILE_PATH);
        allFoulContent = new String(Files.readAllBytes(Paths.get(allFoulRealPath)), StandardCharsets.UTF_8);

        // 2 player setup
        twoPlayerRealPath = FileUtils.getAbsoluteFilePathForResource(this.getClass(), SAMPLE_2_PLAYERS_FILE_PATH);
        twoPlayerContent = new String(Files.readAllBytes(Paths.get(twoPlayerRealPath)), StandardCharsets.UTF_8);
    }

    @After
    public void tearDown() throws Exception {
        scoreParser = null;
        parsedGameCache = null;
        playersCache = null;
        scoreProcessor = null;
        allStrikeRealPath = null;
        allStrikeContent = null;
        allFoulRealPath = null;
        allFoulContent = null;
        twoPlayerRealPath = null;
        twoPlayerContent = null;
    }

    @Test
    public void process_perfectScore_playerListReturnWithScore() throws Exception {
        List<Player> players = scoreProcessor.process(allStrikeContent);

        assertThat(players.size(), is(1));

        players.forEach(player -> {
            assertThat(player.getName(), is("Carl"));

            assertThat(player.calculateScore(), is(300));
        });
    }

    @Test
    public void process_allFoul_playerListReturnWithScore() throws Exception {
        List<Player> players = scoreProcessor.process(allFoulContent);

        assertThat(players.size(), is(1));

        players.forEach(player -> {
            assertThat(player.getName(), is("Carl"));

            assertThat(player.calculateScore(), is(0));
        });
    }

    @Test
    public void process_2playerPassed_playerListReturnWithScore() throws Exception {
        List<Player> players = scoreProcessor.process(twoPlayerContent);

        assertThat(players.size(), is(2));

        Player player1 = players.get(0);
        assertThat(player1.getName(), is("Jeff"));
        assertThat(player1.calculateScore(), is(167));

        Player player2 = players.get(1);
        assertThat(player2.getName(), is("John"));
        assertThat(player2.calculateScore(), is(151));
    }
}
