package com.mohithasan.journalapp.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentimentData {
    private String email;
    private String sentiment;
}