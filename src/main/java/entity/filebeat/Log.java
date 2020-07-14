package entity.filebeat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Log {
    private File file;
    private String offset;

}
