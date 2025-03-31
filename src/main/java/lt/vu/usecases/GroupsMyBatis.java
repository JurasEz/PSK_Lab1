package lt.vu.usecases;

import lombok.Getter;
import lombok.Setter;
import lt.vu.mybatis.dao.GroupTableMapper;
import lt.vu.mybatis.model.GroupTable;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Model
public class GroupsMyBatis {

    @Inject
    private GroupTableMapper groupTableMapper; // Inject the mapper

    @Getter @Setter
    private GroupTable groupToCreate = new GroupTable(); // Use GroupTable instead of Group

    @Getter
    private List<GroupTable> allGroups; // Use GroupTable type

    @PostConstruct
    public void init() {
        loadAllGroups();
    }

    private void loadAllGroups() {
        allGroups = groupTableMapper.selectAll(); // Ensure this method is correctly defined in your mapper
    }

    @Transactional
    public String createGroup() {
        groupTableMapper.insert(groupToCreate); // Ensure this method is correctly defined in your mapper
        return "/myBatis/groups?faces-redirect=true";
    }
}