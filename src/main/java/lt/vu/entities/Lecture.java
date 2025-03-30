package lt.vu.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "lecturer"})
})
@NamedQueries({
        @NamedQuery(name = "Lecture.findAll", query = "SELECT l FROM Lecture l"),
        @NamedQuery(name = "Lecture.findByStudent",
                query = "SELECT l FROM Lecture l JOIN l.students s WHERE s.id = :studentId"),
})
@Getter @Setter // Automatically generate getter and setter methods for all fields
@EqualsAndHashCode(of = "id") // Use the id field for equality checks
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String name;

    @Basic
    private String lecturer;

    @ManyToMany(mappedBy = "lectures") // Student entity owns the relationship
    private List<Student> students = new ArrayList<>();
}