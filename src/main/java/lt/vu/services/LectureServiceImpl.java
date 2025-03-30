package lt.vu.services;

import lt.vu.entities.Lecture;
import lt.vu.persistence.LecturesDAO;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class LectureServiceImpl implements LectureService {

    @Inject
    private LecturesDAO lecturesDAO;

    @Override
    @Transactional
    public Lecture create(Lecture lecture) {
        lecturesDAO.persist(lecture);
        return lecture;
    }

    @Override
    public Lecture findById(Long id) {
        return lecturesDAO.findOne(id);
    }

    @Override
    public List<Lecture> findAll() {
        return lecturesDAO.loadAll();
    }

    @Override
    public List<Lecture> findByStudentId(Long studentId) {
        return lecturesDAO.loadAllByStudent(studentId);  // Changed from findByStudent to loadAllByStudent
    }

    @Override
    @Transactional
    public Lecture update(Lecture lecture) {
        return lecturesDAO.update(lecture);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Lecture lecture = lecturesDAO.findOne(id);
        if (lecture != null) {
            lecturesDAO.delete(lecture);
        }
    }
}