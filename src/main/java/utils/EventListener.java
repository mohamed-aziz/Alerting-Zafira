package utils;

import entity.Document;

import java.util.List;

public interface EventListener {
    void update(String eventType, List<? extends Document> doc);
}
