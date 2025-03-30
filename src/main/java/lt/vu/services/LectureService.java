package lt.vu.services;

import lt.vu.entities.Lecture;
import java.util.List;

public interface LectureService {
    Lecture create(Lecture lecture);
    Lecture findById(Long id);
    List<Lecture> findAll();
    List<Lecture> findByStudentId(Long studentId);
    Lecture update(Lecture lecture);
    void delete(Long id);
}