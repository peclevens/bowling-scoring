package com.clivenspetit.bowlingscoring.app;

import com.clivenspetit.bowlingscoring.app.di.AppModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        // Make sure that at least one argument is supplied to the program
        if (args.length == 0) {
            logger.error("Please provide a game result file to process.");
            System.exit(0);
        }

        // Warn the user that additional file won't be processed if provided
        if (args.length > 1) {
            logger.warn("More than one argument has been passed to the program, please note that " +
                    "only the first one will be processed.");
        }

        // Create with app module
        Injector injector = Guice.createInjector(new AppModule());

        // Get bowling scoring instance for DI graph
        BowlingScoring bowlingScoring = injector.getInstance(BowlingScoring.class);

        // Process result
        bowlingScoring.printGameScore(args[0]);

        System.exit(0);
    }
}
