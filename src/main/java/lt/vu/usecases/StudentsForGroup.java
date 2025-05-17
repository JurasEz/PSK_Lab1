package lt.vu.usecases;

import lt.vu.entities.Group;
import lt.vu.entities.Student;
import lt.vu.services.GroupService;
import lt.vu.services.StudentService;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Model // @RequestScoped ir @Named kombinacija
public class StudentsForGroup {

    @Inject
    private GroupService groupService;

    @Inject
    private StudentService studentService;

    private Group group;
    private Student studentToCreate = new Student();

    @PostConstruct
    public void init() {
        Map<String, String> requestParameters =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Long groupId = Long.parseLong(requestParameters.get("groupId"));
        this.group = groupService.findById(groupId);

        if (this.group == null) {
            throw new IllegalStateException("Group not found");
        }
    }

    @Transactional
    public void createStudent() {
        try {
            // Validate required fields
            if (studentToCreate.getName() == null || studentToCreate.getName().isEmpty() ||
                    studentToCreate.getSurname() == null || studentToCreate.getSurname().isEmpty()) {
                throw new IllegalArgumentException("Name and surname are required");
            }

            // Set the group
            studentToCreate.setGroup(this.group);

            // Persist the student
            studentService.create(studentToCreate);

            // Reset the form
            studentToCreate = new Student();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating student: " + e.getMessage(), null));
        }
    }

    public Group getGroup() {
        return group;
    }

    public Student getStudentToCreate() {
        return studentToCreate;
    }

    public List<Student> getAllStudents() {
        return studentService.findByGroupId(group.getId());
    }
    public int getLecturesCount(Student student) {
        if (student == null || student.getLectures() == null) {
            return 0;
        }
        return student.getLectures().size();
    }
}