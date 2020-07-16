package utils;

import entity.auditbeat.AuditBeatDocument;
import org.elasticsearch.index.query.AbstractQueryBuilder;

import java.util.HashMap;

public interface IScriptAuditbeat {
    AbstractQueryBuilder search();
    int getPeriod();
    String getAuthor();
    String getDescription();
    HashMap<String, Object> getContext();
    String getTemplate();
}