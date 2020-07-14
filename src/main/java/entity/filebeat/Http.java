package entity.filebeat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Http {
    private String hostname;
    private String http_content_type;
    private String http_method;
    private String http_refer;
    private String http_user_agent;
    private String length;
    private String protocol;
    private String redirect;
    private String status;
    private String url;
    private Request request;
    private Response response;
    private String version;
}
