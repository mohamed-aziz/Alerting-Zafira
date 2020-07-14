package entity.filebeat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    private String created;
    private String dataset;
    private String module;
    private String outcome;
    private String type;
    private String origin;
    private String action;

}
