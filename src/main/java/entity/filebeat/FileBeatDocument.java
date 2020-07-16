package entity.filebeat;

import entity.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileBeatDocument extends Document {
    private String title;
    private String subject;
    private String content;
    private Agent agent;
    // private Http http;
    private Url url;
    private Event event;
    private Fileset fileset;
    private Host host;
    private Http http;

    private Input input;
    private Log log;
    private Service service;
    private Source source;
    private UserAgent user_agent;
}
