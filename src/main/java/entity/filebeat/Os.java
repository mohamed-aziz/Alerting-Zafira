package entity.filebeat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Os {
    private String codename;
    private String family;
    private String kernel;
    private String name;
    private String platform;
    private String version;

}
