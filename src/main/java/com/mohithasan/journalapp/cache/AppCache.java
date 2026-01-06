package com.mohithasan.journalapp.cache;

import com.mohithasan.journalapp.entity.ConfigJournalAppEntity;
import com.mohithasan.journalapp.repository.ConfigJournalAppRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
public class AppCache {

    public enum keys  {
        WEATHER_API,
        API_KEY,
    }

    private final ConfigJournalAppRepository configJournalAPpRepository;

    @Autowired
    public AppCache(ConfigJournalAppRepository configJournalAPpRepository){
        this.configJournalAPpRepository = configJournalAPpRepository;
    }
    private Map<String, String> cache;

    @PostConstruct
    public void inti(){
        Map<String, String> appCacheTemp = new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAPpRepository.findAll();
        for (ConfigJournalAppEntity config : all){
            appCacheTemp.put(config.getKey(), config.getValue());
        }
        cache = appCacheTemp;
    }

}
