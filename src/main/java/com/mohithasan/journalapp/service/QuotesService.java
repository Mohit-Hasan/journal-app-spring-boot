package com.mohithasan.journalapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QuotesService {

    @Value("${quote.api.key}")
    private String apiKey;
}
