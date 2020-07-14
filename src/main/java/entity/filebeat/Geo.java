package entity.filebeat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Geo {
    private String continent_name;
    private String country_iso_code;
    private Location location;
}
