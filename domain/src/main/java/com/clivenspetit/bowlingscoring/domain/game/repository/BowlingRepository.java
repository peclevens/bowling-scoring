package com.clivenspetit.bowlingscoring.domain.game.repository;

import java.io.IOException;

/**
 * Manage Bowling game data
 *
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public interface BowlingRepository {

    /**
     * Load the content of the file specified by file path. File content is cached for a certain period of time
     * and get reload if is changed.
     *
     * @param filePath
     * @return
     */
    String loadGameResultFromFile(String filePath) throws IOException;
}
