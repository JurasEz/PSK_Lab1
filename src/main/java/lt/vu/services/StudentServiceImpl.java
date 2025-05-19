package lt.vu.services;

import lt.vu.entities.Lecture;
import lt.vu.entities.Student;
import lt.vu.interceptors.Loggable;
import lt.vu.persistence.StudentsDAO;
import lt.vu.persistence.LecturesDAO;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Loggable
public class StudentServiceImpl implements StudentService {

    @Inject
    private StudentsDAO studentsDAO;

    @Inject
    private LecturesDAO lecturesDAO;

    @Override
    @Transactional
    public Student create(Student student) {
        studentsDAO.persist(student);
        return student;
    }

    @Override
    public Student findById(Long id) {
        return studentsDAO.findOne(id);
    }

    @Override
    public List<Student> findAll() {
        return studentsDAO.loadAll();
    }

    @Override
    public List<Student> findByGroupId(Long groupId) {
        return studentsDAO.findByGroup(groupId);
    }

    @Override
    @Transactional
    public Student update(Student student) {
        return studentsDAO.update(student);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Student student = studentsDAO.findOne(id);
        if (student != null) {
            studentsDAO.delete(student);
        }
    }

    @Override
    @Transactional
    public void addLectureToStudent(Long studentId, Long lectureId) {
        Student student = studentsDAO.findOne(studentId);
        Lecture lecture = lecturesDAO.findOne(lectureId);
        if (student != null && lecture != null) {
            student.getLectures().add(lecture);
            lecture.getStudents().add(student);
            studentsDAO.update(student);
            lecturesDAO.update(lecture);
        }
    }
}