package lt.vu.persistence;

import lt.vu.entities.Group;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class GroupsDAO {

    @Inject
    private EntityManager em;

    public void persist(Group group) {
        this.em.persist(group);
    }

    public Group findOne(Long id) {
        return em.find(Group.class, id);
    }

    public List<Group> loadAll() {
        return em.createNamedQuery("Group.findAll", Group.class).getResultList();
    }

    public Group update(Group group) {
        return em.merge(group);
    }

    public void delete(Group group) {
        em.remove(em.contains(group) ? group : em.merge(group));
    }
}