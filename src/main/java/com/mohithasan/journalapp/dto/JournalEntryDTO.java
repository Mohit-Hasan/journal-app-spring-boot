package com.mohithasan.journalapp.dto;

import com.mohithasan.journalapp.enums.Sentiment;
import lombok.*;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryDTO {
    private ObjectId id;

    @NotBlank(message = "Title is required")
    @Size(max = 256)
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private LocalDateTime date;
    private Sentiment sentiment;
}
