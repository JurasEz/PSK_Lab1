package lt.vu.persistence;

import lt.vu.entities.Lecture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class LecturesDAO {

    @Inject
    private EntityManager em;

    public void persist(Lecture lecture) {
        this.em.persist(lecture);
    }

    public Lecture findOne(Long id) {
        return em.find(Lecture.class, id);
    }

    public List<Lecture> loadAll() {
        return em.createNamedQuery("Lecture.findAll", Lecture.class).getResultList();
    }

    // Changed from findByStudent to loadAllByStudent and accepts Long
    public List<Lecture> loadAllByStudent(Long studentId) {
        return em.createNamedQuery("Lecture.findByStudent", Lecture.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    public Lecture update(Lecture lecture) {
        return em.merge(lecture);
    }

    public void delete(Lecture lecture) {
        em.remove(em.contains(lecture) ? lecture : em.merge(lecture));
    }

    public List<Lecture> findByStudentId(Long studentId) {
        try {
            return em.createQuery(
                            "SELECT l FROM Lecture l JOIN l.students s WHERE s.id = :studentId",
                            Lecture.class)
                    .setParameter("studentId", studentId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Lecture findByNameAndLecturer(String name, String lecturer) {
        try {
            return em.createQuery(
                            "SELECT l FROM Lecture l WHERE l.name = :name AND l.lecturer = :lecturer",
                            Lecture.class)
                    .setParameter("name", name)
                    .setParameter("lecturer", lecturer)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}