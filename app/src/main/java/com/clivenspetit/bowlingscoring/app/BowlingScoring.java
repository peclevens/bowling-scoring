package com.clivenspetit.bowlingscoring.app;

import com.clivenspetit.bowlingscoring.domain.game.AbstractScoreProcessor;
import com.clivenspetit.bowlingscoring.domain.game.repository.BowlingRepository;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class BowlingScoring {

    private static final Logger logger = LogManager.getLogger(BowlingScoring.class);

    private final BowlingRepository bowlingRepository;
    private final AbstractScoreProcessor scoreProcessor;

    @Inject
    public BowlingScoring(BowlingRepository bowlingRepository, AbstractScoreProcessor scoreProcessor) {
        this.bowlingRepository = bowlingRepository;
        this.scoreProcessor = scoreProcessor;
    }

    public void printGameScore(String filePath) {
        logger.debug("Processing score for game file: {}", filePath);
    }
}
