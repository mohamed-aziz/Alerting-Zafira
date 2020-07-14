package entity.filebeat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Body {
    private long bytes;
    private String content;
}
