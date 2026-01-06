package com.mohithasan.journalapp.cache;

import com.mohithasan.journalapp.entity.ConfigJournalAppEntity;
import com.mohithasan.journalapp.repository.ConfigJournalAppRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppCacheTests {

    @Mock
    private ConfigJournalAppRepository configJournalAppRepository;

    private AppCache appCache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appCache = new AppCache(configJournalAppRepository);
    }

    @Test
    void testCacheInitialization() {
        ConfigJournalAppEntity config1 = new ConfigJournalAppEntity("WEATHER_API", "some_api_value");
        ConfigJournalAppEntity config2 = new ConfigJournalAppEntity("API_KEY", "another_api_value");

        List<ConfigJournalAppEntity> mockConfigs = Arrays.asList(config1, config2);
        when(configJournalAppRepository.findAll()).thenReturn(mockConfigs);
        appCache.inti();
        Map<String, String> cache = appCache.getCache();

        assertEquals("some_api_value", cache.get("WEATHER_API"));
        assertEquals("another_api_value", cache.get("API_KEY"));
    }

    @Test
    void testCacheEmptyWhenNoConfigs() {
        when(configJournalAppRepository.findAll()).thenReturn(Arrays.asList());
        appCache.inti();
        Map<String, String> cache = appCache.getCache();
        assertEquals(0, cache.size());
    }
}
