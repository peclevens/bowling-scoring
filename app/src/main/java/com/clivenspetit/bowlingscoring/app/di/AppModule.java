package com.clivenspetit.bowlingscoring.app.di;

import com.clivenspetit.bowlingscoring.data.BowlingRepositoryImpl;
import com.clivenspetit.bowlingscoring.data.GameContent;
import com.clivenspetit.bowlingscoring.domain.game.AbstractScoreProcessor;
import com.clivenspetit.bowlingscoring.domain.game.Player;
import com.clivenspetit.bowlingscoring.domain.game.TenPinScoreProcessor;
import com.clivenspetit.bowlingscoring.domain.game.repository.BowlingRepository;
import com.clivenspetit.bowlingscoring.domain.parser.ScoreParser;
import com.clivenspetit.bowlingscoring.domain.parser.TabSeparatedScoreParser;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.clivenspetit.bowlingscoring.app.Constants.DEFAULT_CACHE_INVALIDATE_AFTER_WRITE;
import static com.clivenspetit.bowlingscoring.app.Constants.DEFAULT_CACHE_MAX_ITEM;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class AppModule extends AbstractModule {

    @Singleton
    @Provides
    public ScoreParser provideTenPinScoreProcessor() {
        return new TabSeparatedScoreParser();
    }

    @Provides
    @Singleton
    public Cache<String, GameContent> provideGameContentCache() {

        return CacheBuilder.newBuilder()
                .maximumSize(DEFAULT_CACHE_MAX_ITEM)
                .expireAfterWrite(DEFAULT_CACHE_INVALIDATE_AFTER_WRITE, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public Cache<Integer, Map<String, List<String>>> provideGamePlayerScoresCache() {

        return CacheBuilder.newBuilder()
                .maximumSize(DEFAULT_CACHE_MAX_ITEM)
                .expireAfterWrite(DEFAULT_CACHE_INVALIDATE_AFTER_WRITE, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public Cache<Integer, List<Player>> provideGamePlayerCache() {

        return CacheBuilder.newBuilder()
                .maximumSize(DEFAULT_CACHE_MAX_ITEM)
                .expireAfterWrite(DEFAULT_CACHE_INVALIDATE_AFTER_WRITE, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    @Named("gameResult")
    public Cache<String, String> provideGameResultCache() {

        return CacheBuilder.newBuilder()
                .maximumSize(DEFAULT_CACHE_MAX_ITEM)
                .expireAfterWrite(DEFAULT_CACHE_INVALIDATE_AFTER_WRITE, TimeUnit.SECONDS)
                .build();
    }

    @Singleton
    @Provides
    public BowlingRepository provideBowlingRepository(Cache<String, GameContent> gameContentCache) {
        return new BowlingRepositoryImpl(gameContentCache);
    }

    @Singleton
    @Provides
    public AbstractScoreProcessor provideAbstractScoreProcessor(
            ScoreParser scoreParser, Cache<Integer, Map<String, List<String>>> parsedGameCache,
            Cache<Integer, List<Player>> playersCache) {

        return new TenPinScoreProcessor(scoreParser, parsedGameCache, playersCache);
    }
}
