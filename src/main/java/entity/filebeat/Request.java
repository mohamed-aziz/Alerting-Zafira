package entity.filebeat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private Body body;
    private String bytes;
    private String method ;
    private String referrer;
}
