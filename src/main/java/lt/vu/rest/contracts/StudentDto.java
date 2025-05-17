package lt.vu.rest.contracts;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudentDto {
    private Long id;
    private Integer version;
    private String name;
    private String surname;
    private Long groupId;
}
