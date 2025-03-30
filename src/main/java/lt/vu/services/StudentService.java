package lt.vu.services;

import lt.vu.entities.Student;
import java.util.List;

public interface StudentService {
    Student create(Student student);
    Student findById(Long id);
    List<Student> findAll();
    List<Student> findByGroupId(Long groupId);
    Student update(Student student);
    void delete(Long id);
    void addLectureToStudent(Long studentId, Long lectureId);
}