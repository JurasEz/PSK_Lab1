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

@Model
public class StudentsForGroupMyBatis {

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

        // Retrieve the groupId from the request parameters
        String groupIdParam = requestParameters.get("groupId");
        if (groupIdParam == null || groupIdParam.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No groupId parameter provided", null));
            return;
        }

        // Parse the groupId
        Long groupId = Long.parseLong(groupIdParam);
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
}