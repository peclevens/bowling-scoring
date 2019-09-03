package com.clivenspetit.bowlingscoring.app;

import com.clivenspetit.bowlingscoring.domain.game.AbstractScoreProcessor;
import com.clivenspetit.bowlingscoring.domain.game.Player;
import com.clivenspetit.bowlingscoring.domain.game.repository.BowlingRepository;
import com.clivenspetit.bowlingscoring.domain.utility.FileUtils;
import com.clivenspetit.bowlingscoring.domain.utility.StubFrame;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class BowlingScoringTest {

    private static final String SAMPLE_ALL_STRIKES_FILE_PATH = "stubs/sample_all_strikes_game.txt";

    private BowlingRepository bowlingRepository;
    private AbstractScoreProcessor scoreProcessor;
    private Cache<String, String> gameResultCache;
    private BowlingScoring bowlingScoring;
    private String realPath;
    private String allStrikeContent;

    @Before
    public void setUp() throws Exception {
        bowlingRepository = mock(BowlingRepository.class);
        scoreProcessor = mock(AbstractScoreProcessor.class);

        gameResultCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();

        bowlingScoring = new BowlingScoring(bowlingRepository, scoreProcessor, gameResultCache);

        realPath = FileUtils.getAbsoluteFilePathForResource(this.getClass(), SAMPLE_ALL_STRIKES_FILE_PATH);
        allStrikeContent = new String(Files.readAllBytes(Paths.get(realPath)), StandardCharsets.UTF_8);

        when(bowlingRepository.loadGameResultFromFile(realPath))
                .thenReturn(allStrikeContent);

        when(scoreProcessor.process(allStrikeContent))
                .thenReturn(buildStubAllStrikePlayer());
    }

    private List<Player> buildStubAllStrikePlayer() {
        List<Player> players = new ArrayList<>();

        Player player = new Player("Carl");
        player.addFrame(0, new StubFrame('\0', 'X', '\0', 30));
        player.addFrame(1, new StubFrame('\0', 'X', '\0', 60));
        player.addFrame(2, new StubFrame('\0', 'X', '\0', 90));
        player.addFrame(3, new StubFrame('\0', 'X', '\0', 120));
        player.addFrame(4, new StubFrame('\0', 'X', '\0', 150));
        player.addFrame(5, new StubFrame('\0', 'X', '\0', 180));
        player.addFrame(6, new StubFrame('\0', 'X', '\0', 210));
        player.addFrame(7, new StubFrame('\0', 'X', '\0', 240));
        player.addFrame(8, new StubFrame('\0', 'X', '\0', 270));
        player.addFrame(9, new StubFrame('X', 'X', 'X', 300));

        players.add(player);

        return players;
    }

    @After
    public void tearDown() throws Exception {
        bowlingRepository = null;
        scoreProcessor = null;
        gameResultCache = null;
        bowlingScoring = null;
        realPath = null;
        allStrikeContent = null;
    }

    @Test(expected = NullPointerException.class)
    public void printGameScore_nullArgumentPassed_throwException() throws Exception {
        bowlingScoring.printGameScore(null);
    }

    @Test
    public void printGameScore_validFilePassed_loadFileProcessResultPrintGameScore() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        bowlingScoring.printGameScore(realPath);

        verify(bowlingRepository, times(1))
                .loadGameResultFromFile(argumentCaptor.capture());

        verify(scoreProcessor, times(1))
                .process(argumentCaptor.capture());

        // Make sure that BowlingRepository#loadGameResultFromFile was called with the right path
        assertThat(argumentCaptor.getAllValues().get(0), is(realPath));

        // Make sure that AbstractScoreProcessor#process was called with the right content
        assertThat(argumentCaptor.getAllValues().get(1), is(allStrikeContent));
    }
}
