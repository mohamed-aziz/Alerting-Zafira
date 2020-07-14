package entity.filebeat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAgent {
    private String name;
    private String original;
    private Device device;
    private Os os;
}
