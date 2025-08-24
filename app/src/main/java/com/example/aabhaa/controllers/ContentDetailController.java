package com.example.aabhaa.controllers;

import com.example.aabhaa.models.Content;

import java.util.ArrayList;
import java.util.List;

public class ContentDetailController {

    public List<Content> getSuggestionsByType(List<Content> allContents, String contentType, int excludeId) {
        List<Content> suggestions = new ArrayList<>();
        for (Content c : allContents) {
//            if (c.content_type.equals(contentType) && c.id != excludeId) {
//                suggestions.add(c);
//            }
        }
        return suggestions;
    }
}
