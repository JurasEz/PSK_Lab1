package lt.vu.usecases;

import lt.vu.entities.Lecture;
import lt.vu.entities.Student;
import lt.vu.persistence.LecturesDAO;
import lt.vu.persistence.StudentsDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Model
public class LecturesForStudentMyBatis {

    @Inject
    private StudentsDAO studentsDAO;

    @Inject
    private LecturesDAO lecturesDAO;

    private Student student; // This will hold the student object
    private Lecture lectureToCreate = new Lecture();

    @PostConstruct
    public void init() {
        try {
            Map<String, String> requestParameters =
                    FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String studentIdParam = requestParameters.get("studentId");

            if (studentIdParam == null || studentIdParam.isEmpty()) {
                throw new IllegalArgumentException("Student ID parameter is missing");
            }

            Long studentId = Long.parseLong(studentIdParam);
            this.student = studentsDAO.findOne(studentId); // Initialize the student object

            if (this.student == null) {
                throw new IllegalArgumentException("Student not found with ID: " + studentId);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid student ID format", e);
        }
    }

    @Transactional
    public String createLecture() {
        try {
            // Validate lecture data
            if (lectureToCreate.getName() == null || lectureToCreate.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Lecture name is required");
            }

            // Check if lecture already exists
            Lecture existingLecture = lecturesDAO.findByNameAndLecturer(
                    lectureToCreate.getName(),
                    lectureToCreate.getLecturer()
            );

            if (existingLecture != null) {
                // Use existing lecture instead of creating new one
                lectureToCreate = existingLecture;
            } else {
                // Persist new lecture
                lecturesDAO.persist(lectureToCreate);
            }

            // Manage the bidirectional relationship
            if (!lectureToCreate.getStudents().contains(student)) {
                lectureToCreate.getStudents().add(student);
            }
            if (!student.getLectures().contains(lectureToCreate)) {
                student.getLectures().add(lectureToCreate);
            }

            // Update both entities
            lecturesDAO.update(lectureToCreate);
            studentsDAO.update(student);

            // Reset the form (but keep the lecture reference)
            Lecture temp = lectureToCreate;
            lectureToCreate = new Lecture();
            lectureToCreate.setName(temp.getName());
            lectureToCreate.setLecturer(temp.getLecturer());

            return "studentDetails.xhtml?studentId=" + student.getId() + "&faces-redirect=true";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating lecture: " + e.getMessage(), null));
            return null;
        }
    }

    public Student getStudent() {
        return student;
    }

    public Lecture getLectureToCreate() {
        return lectureToCreate;
    }

    public List<Lecture> getLectures() {
        if (student == null || student.getId() == null) {
            return Collections.emptyList();  // Safe empty list
        }
        return lecturesDAO.findByStudentId(student.getId());
    }
}
