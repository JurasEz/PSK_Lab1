package lt.vu.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "Group.findAll", query = "SELECT g FROM Group g"),
})
@Getter @Setter // Automatically generate getter and setter methods for all fields
@Table(name = "GROUP_TABLE") // "GROUP" is a SQL reserved word
@EqualsAndHashCode(of = "id") // Use the id field for equality checks
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String discipline;

    @Basic(optional = false)
    private Integer semester;

    // orphanRemoval = true - If you remove a Student from the students collection, it will be automatically deleted from the database, Only applies to removed children
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true) // Student entity owns the relationship / Cscade-update group-updates student
    private List<Student> students = new ArrayList<>();
}