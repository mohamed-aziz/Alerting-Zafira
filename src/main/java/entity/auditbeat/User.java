package entity.auditbeat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long id;
    private String name;
    private String terminal;
}
