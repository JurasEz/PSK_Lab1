// src/main/java/lt/vu/rest/StudentController.java
package lt.vu.rest;

import lt.vu.entities.Group;
import lt.vu.entities.Student;
import lt.vu.interceptors.Loggable;
import lt.vu.persistence.StudentsDAO;
import lt.vu.rest.contracts.StudentDto;
import lt.vu.services.StudentService;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
@Transactional
@Loggable // wraps the bean (or method) with your LoggingInterceptor
public class StudentController {

    @PersistenceContext(unitName = "StudentPersistenceUnit")
    private EntityManager em;

    //@Inject
    //private StudentsDAO studentsDAO;

    @Inject
    private StudentService studentService;  // inject the service for getAll()

    @GET
    public Response getAll() {
        // now goes through AuditingStudentService.findAll()
        List<Student> students = studentService.findAll();
        List<StudentDto> dtos = students.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("{id}")
    public Response getOne(@PathParam("id") Long id) {
        // Student student = em.find(Student.class, id);
        Student student = studentService.findById(id);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(toDto(student)).build();
    }

    @POST
    public Response create(StudentDto dto, @Context UriInfo uriInfo) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setSurname(dto.getSurname());
        if (dto.getGroupId() != null) {
            Group group = em.find(Group.class, dto.getGroupId());
            student.setGroup(group);
        }
        em.persist(student);
        em.flush(); // generate the ID

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(student.getId().toString())
                .build();
        return Response.created(uri)
                .entity(toDto(student))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, StudentDto dto) {
        // Build a _detached_ entity carrying the client’s version
        Student detached = new Student();
        detached.setId(id);
        detached.setVersion(dto.getVersion());
        detached.setName(dto.getName());
        detached.setSurname(dto.getSurname());
        if (dto.getGroupId() != null) {
            detached.setGroup(em.getReference(Group.class, dto.getGroupId())); // sets the FK without touching the db
        }

        try {
            // Student merged = studentsDAO.update(detached);
            Student merged = em.merge(detached);  // will check version (compares the DB’s 'version' against detached.getVersion())
            return Response.ok(toDto(merged)).build(); // returns HTTP 200 with the updated entity
        } catch (OptimisticLockException ole) {
            // 1) the transaction is rollback-only
            // 2) the EM is unusable—container will clear it
            //    Return a 409 Conflict so the client knows there was a version mismatch
            return Response.status(Response.Status.CONFLICT)
                    .entity("Conflict: resource was updated by another transaction")
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        Student student = em.find(Student.class, id);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        em.remove(student);
        return Response.noContent().build();
    }

    private StudentDto toDto(Student s) {
        StudentDto dto = new StudentDto();
        dto.setId(s.getId());
        dto.setVersion(s.getVersion());
        dto.setName(s.getName());
        dto.setSurname(s.getSurname());
        if (s.getGroup() != null) {
            dto.setGroupId(s.getGroup().getId());
        }
        return dto;
    }
}