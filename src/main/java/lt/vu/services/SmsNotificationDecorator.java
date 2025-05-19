package lt.vu.services;

import lt.vu.entities.Student;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.annotation.Priority;
import javax.interceptor.Interceptor;
import java.util.List;

/**
 * A CDI Decorator for StudentService that sends a fake "SMS" message
 * after calling the real service.  This adds notifications without
 * touching StudentServiceImpl itself.
 */
@Decorator
@Priority(Interceptor.Priority.APPLICATION + 100)
public abstract class SmsNotificationDecorator implements StudentService {

    @Inject @Delegate
    private StudentService delegate;  // the real implementation

    @Override
    public List<Student> findAll() {
        List<Student> students = delegate.findAll();
        sendSms("Fetched " + students.size() + " students");
        return students;
    }

    @Override
    public Student findById(Long id) {
        Student s = delegate.findById(id);
        sendSms("Fetched student with id=" + id);
        return s;
    }

    // All other methods just delegate without SMS
    @Override public Student create(Student student)              { return delegate.create(student); }
    @Override public Student update(Student student)              { return delegate.update(student); }
    @Override public void    delete(Long id)                      { delegate.delete(id); }
    @Override public List<Student> findByGroupId(Long groupId)    { return delegate.findByGroupId(groupId); }
    @Override public void    addLectureToStudent(Long s, Long l)  { delegate.addLectureToStudent(s,l); }

    private void sendSms(String text) {
        // In a real app you'd integrate an SMS API here.
        System.out.println(" SMS -> " + text);
    }
}