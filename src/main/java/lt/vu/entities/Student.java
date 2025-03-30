package lt.vu.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter // Automatically generate getter and setter methods for all fields
@EqualsAndHashCode(of = "id") // Use the id field for equality checks
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private String surname;

    @ManyToOne
    @JoinColumn(name = "group_id") // creates group_id foreign key column in student table
    private Group group;

    @ManyToMany
    @JoinTable(
            name = "STUDENT_LECTURE",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "lecture_id")
    )
    private List<Lecture> lectures = new ArrayList<>();
}