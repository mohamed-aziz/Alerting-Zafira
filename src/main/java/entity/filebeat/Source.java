package entity.filebeat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Source {
    private As as;
    private Geo geo;
    private String ip;
}
