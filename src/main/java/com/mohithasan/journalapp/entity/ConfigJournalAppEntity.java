package com.mohithasan.journalapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "journal_app_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigJournalAppEntity {

    private String key;
    private String value;

}
