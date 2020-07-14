package entity.filebeat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Host{
    private String architecture;
    private String containerized;

    private String hostname;
    private String id;
    private String name;
    private Os os;

}
