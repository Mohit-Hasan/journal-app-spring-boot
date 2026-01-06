package com.mohithasan.journalapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import com.mohithasan.journalapp.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id
    private ObjectId id;
    @NonNull
    private String title;
    @NonNull
    private  String content;
    private LocalDateTime date;
    private Sentiment sentiment;

}
