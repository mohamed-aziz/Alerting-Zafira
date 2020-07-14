package entity.filebeat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Url {
    private String domain;
    private String fragment;
    private String full;
    private String original;
    private String password;
    private String path;
    private String port;
    private String query;
    private String scheme;
    private String username;
}
