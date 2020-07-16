package utils;

import entity.Document;

import java.util.HashMap;
import java.util.List;

public interface EventListener {
    void update(String eventType, List<? extends Document> doc, String template, HashMap<String, Object> context);
}
