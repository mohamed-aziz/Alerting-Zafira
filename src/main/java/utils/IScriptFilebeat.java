package utils;

import entity.filebeat.FileBeatDocument;
import org.elasticsearch.index.query.AbstractQueryBuilder;

import java.util.HashMap;

public interface IScriptFilebeat {
    AbstractQueryBuilder search();
    int getPeriod();
    String getAuthor();
    String getDescription();
    HashMap<String, Object> getContext();
    String getTemplate();
}
