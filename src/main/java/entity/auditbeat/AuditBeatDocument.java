package entity.auditbeat;

import entity.Document;
import entity.filebeat.Agent;
import entity.filebeat.Event;
import entity.filebeat.Host;
import entity.filebeat.Source;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditBeatDocument extends Document {
    private String id;
    private Agent agent;
    private Event event;
    private Host host;
    private User user;
    private String message;
    private Source source;
}
