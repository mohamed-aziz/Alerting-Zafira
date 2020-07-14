package entity.filebeat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
    private Body body;
    private String status_code;
}
