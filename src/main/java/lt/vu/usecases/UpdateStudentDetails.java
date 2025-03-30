package lt.vu.usecases;

import lt.vu.entities.Student;
import lt.vu.persistence.StudentsDAO;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Map;

@ViewScoped
@Named
public class UpdateStudentDetails implements Serializable {

    private Student student;

    @Inject
    private StudentsDAO studentsDAO;

    @PostConstruct
    private void init() {
        try {
            Map<String, String> requestParameters =
                    FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String studentIdParam = requestParameters.get("studentId");

            if (studentIdParam == null || studentIdParam.isEmpty()) {
                throw new IllegalArgumentException("Student ID parameter is missing");
            }

            Long studentId = Long.parseLong(studentIdParam);
            this.student = studentsDAO.findOne(studentId);

            if (this.student == null) {
                throw new IllegalArgumentException("Student not found with ID: " + studentId);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid student ID format", e);
        }
    }

    @Transactional
    public String updateStudent() {
        try {
            if (student == null || student.getGroup() == null) {
                throw new IllegalStateException("Student or student's group is not properly initialized");
            }

            studentsDAO.update(this.student);
            return "students.xhtml?groupId=" + this.student.getGroup().getId() + "&faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating student: " + e.getMessage(), null));
            return null;
        }
    }

    public Student getStudent() {
        return student;
    }
}