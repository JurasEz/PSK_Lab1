package lt.vu.persistence;

import lt.vu.entities.Student;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class StudentsDAO {

    @Inject
    private EntityManager em;

    public void persist(Student student) {
        this.em.persist(student);
    }

    public Student findOne(Long id) {
        return em.find(Student.class, id);
    }

    public List<Student> loadAll() {
        return em.createNamedQuery("Student.findAll", Student.class).getResultList();
    }

    public List<Student> findByGroup(Long groupId) {
        return em.createNamedQuery("Student.findByGroup", Student.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }

    public Student update(Student student) {
        return em.merge(student);
    }

    public void delete(Student student) {
        em.remove(em.contains(student) ? student : em.merge(student));
    }
}