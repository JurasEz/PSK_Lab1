package lt.vu.services;

import lt.vu.entities.Student;
import lt.vu.interceptors.Loggable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import java.util.List;
import java.time.LocalDateTime;


// Because of @Specializes, this bean *replaces* StudentServiceImpl everywhere.
// CDI then permanently replaces the base class with your subclass wherever the interface or base is injected

//
@Specializes
@ApplicationScoped
public class AuditingStudentService extends StudentServiceImpl {

    // Overrides the default findAll() to wrap it with audit logs.
    @Override
    public List<Student> findAll() {
        log("findAll()"); // Log entry
        List<Student> result = super.findAll();
        log("findAll() returned " + result.size() + " students"); // Log exit
        return result;
    }

    private void log(String msg) {
        System.out.println("[AUDIT " + LocalDateTime.now() + "] StudentService " + msg);
    }
}