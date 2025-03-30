package lt.vu.services;

import lt.vu.mybatis.dao.StudentMapper;
import lt.vu.mybatis.model.Student;
import lt.vu.mybatis.model.StudentExample;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.cdi.SessionFactoryProvider;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class StudentService {
    @Inject
    @SessionFactoryProvider
    private SqlSessionFactory sqlSessionFactory;

    public List<Student> getAllStudents() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            return mapper.selectByExample(new StudentExample()); // Empty example = select all
        }
    }
}