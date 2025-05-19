package lt.vu.services;

import lt.vu.entities.Student;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import java.util.Collections;
import java.util.List;

// A mock alternative that returns a single, hard-coded Student.
@Alternative
@ApplicationScoped
public class MockStudentService implements StudentService {

    @Override
    public Student create(Student student) {
        return student;
    }

    @Override
    public Student findById(Long id) {
        return mockStudent();
    }

    @Override
    public List<Student> findAll() {
        return Collections.singletonList(mockStudent());
    }

    @Override
    public List<Student> findByGroupId(Long groupId) {
        return findAll();
    }

    @Override
    public Student update(Student student) {
        return student;
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public void addLectureToStudent(Long studentId, Long lectureId) {
    }

    private Student mockStudent() {
        Student s = new Student();
        s.setId(999L);
        s.setName("Mock");
        s.setSurname("Student");
        return s;
    }
}