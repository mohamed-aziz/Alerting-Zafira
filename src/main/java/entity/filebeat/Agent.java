package entity.filebeat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Agent {
    private String id;
    private String ephemeral_id;
    private String hostname;
    private String name;
    private String type;
    private String version;
}
