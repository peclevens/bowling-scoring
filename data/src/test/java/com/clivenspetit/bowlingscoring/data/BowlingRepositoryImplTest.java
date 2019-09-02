package com.clivenspetit.bowlingscoring.data;

import com.clivenspetit.bowlingscoring.domain.utility.FileUtils;
import com.clivenspetit.bowlingscoring.domain.game.repository.BowlingRepository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class BowlingRepositoryImplTest {

    private static final String FILE_NOT_EXIST_PATH = "stubs/no_file.txt";
    private static final String EMPTY_FILE_PATH = "stubs/empty.txt";
    private static final String SAMPLE_ALL_STRIKES_FILE_PATH = "stubs/sample_all_strikes_game.txt";

    private BowlingRepository bowlingRepository;
    private Cache<String, GameContent> gameContentCache;

    @Before
    public void setUp() throws Exception {
        gameContentCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();

        bowlingRepository = new BowlingRepositoryImpl(gameContentCache);
    }

    @After
    public void tearDown() throws Exception {
        gameContentCache = null;
        bowlingRepository = null;
    }

    @Test(expected = NullPointerException.class)
    public void loadGameResultFromFile_nullArgumentPassed_throwException() throws Exception {
        bowlingRepository.loadGameResultFromFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadGameResultFromFile_fileNotExist_throwException() throws Exception {
        bowlingRepository.loadGameResultFromFile(FILE_NOT_EXIST_PATH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadGameResultFromFile_fileEmpty_throwException() throws Exception {
        String realPath = FileUtils.getAbsoluteFilePathForResource(this.getClass(), EMPTY_FILE_PATH);

        bowlingRepository.loadGameResultFromFile(realPath);
    }

    @Test
    public void loadGameResultFromFile_validFile_stringContentReturned() throws Exception {
        String realPath = FileUtils.getAbsoluteFilePathForResource(this.getClass(), SAMPLE_ALL_STRIKES_FILE_PATH);
        String content = bowlingRepository.loadGameResultFromFile(realPath);

        // Load file content with standard library
        String sameContent = new String(Files.readAllBytes(Paths.get(realPath)), StandardCharsets.UTF_8);

        assertThat(sameContent, equalTo(content));
    }
}
