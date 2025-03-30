// GroupService.java
package lt.vu.services;

import lt.vu.entities.Group;
import java.util.List;

public interface GroupService {
    Group create(Group group);
    Group findById(Long id);
    List<Group> findAll();
    Group update(Group group);
    void delete(Long id);
}
