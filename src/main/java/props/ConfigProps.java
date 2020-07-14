package props;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigProps {
    private Clients clients = new Clients();
    // private Index index = new Index();
    private Index index;

}
