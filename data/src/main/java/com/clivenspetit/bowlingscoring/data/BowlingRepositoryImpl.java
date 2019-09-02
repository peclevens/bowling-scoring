package com.clivenspetit.bowlingscoring.data;

import com.clivenspetit.bowlingscoring.domain.game.repository.BowlingRepository;
import com.google.common.cache.Cache;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Manage Bowling game data
 *
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class BowlingRepositoryImpl implements BowlingRepository {

    private final Cache<String, GameContent> gameContentCache;

    public BowlingRepositoryImpl(Cache<String, GameContent> gameContentCache) {
        this.gameContentCache = gameContentCache;
    }

    /**
     * Load the content of the file specified by file path. File content is cached for a certain period of time
     * and get reload if is changed.
     *
     * @param filePath
     * @return
     */
    @Override
    public String loadGameResultFromFile(String filePath) throws IOException {
        Objects.requireNonNull(filePath, "File path cannot be null.");

        File file = new File(filePath);
        if (!file.exists()) {
            // Remove file from cache immediately if it got deleted
            gameContentCache.invalidate(filePath);

            throw new IllegalArgumentException(String.format("File '%s' does not exist.", filePath));
        }

        // Try to retrieve game content from cache
        GameContent gameContent = gameContentCache.getIfPresent(filePath);

        // If game has not load yet or the game content is outdated, reload it.
        if (gameContent == null || gameContent.getLastModified() < file.lastModified()) {
            byte[] contentBytes = Files.readAllBytes(Paths.get(filePath));

            gameContent = new GameContent(new String(contentBytes, StandardCharsets.UTF_8), file.lastModified());

            // Make sure the file has some content in it
            if (gameContent.getContent() == null || gameContent.getContent().trim().equals("")) {
                throw new IllegalArgumentException(String.format("File '%s' should not be empty.", filePath));
            }

            // Cache this game content
            gameContentCache.put(filePath, gameContent);
        }

        return gameContent.getContent();
    }
}
