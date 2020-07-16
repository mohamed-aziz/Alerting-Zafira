package entity.auditbeat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Process {
    private String working_directory;
    private String executable;
    private String[] args;
}
