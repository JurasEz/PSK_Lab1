package lt.vu.services;

import lt.vu.entities.Group;
import lt.vu.persistence.GroupsDAO;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class GroupServiceImpl implements GroupService {

    @Inject
    private GroupsDAO groupsDAO;

    @Override
    @Transactional
    public Group create(Group group) {
        groupsDAO.persist(group);
        return group;
    }

    @Override
    public Group findById(Long id) {
        return groupsDAO.findOne(id);
    }

    @Override
    public List<Group> findAll() {
        return groupsDAO.loadAll();
    }

    @Override
    @Transactional
    public Group update(Group group) {
        return groupsDAO.update(group);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Group group = groupsDAO.findOne(id);
        if (group != null) {
            groupsDAO.delete(group);
        }
    }
}