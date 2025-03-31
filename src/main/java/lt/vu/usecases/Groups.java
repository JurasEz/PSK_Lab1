package lt.vu.usecases;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.Group;
import lt.vu.persistence.GroupsDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Model // @RequestScoped ir @Named (kad būtų pasiekiama per JSF/XHTML) kombinacija
public class Groups {

    @Inject
    private GroupsDAO groupsDAO;

    @Getter @Setter
    private Group groupToCreate = new Group();

    @Getter
    private List<Group> allGroups;

    @PostConstruct
    public void init() {
        loadAllGroups();
    }

    @Transactional
    public String createGroup() {
        // Check for existing group with same name and semester
        if (groupExists(groupToCreate.getDiscipline(), groupToCreate.getSemester())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Group already exists", null));
            return null;
        }

        groupsDAO.persist(groupToCreate);
        groupToCreate = new Group(); // Reset the form

        // Return with redirect to refresh the page
        return "/index.xhtml?faces-redirect=true";
    }

    private boolean groupExists(String discipline, Integer semester) {
        return allGroups.stream()
                .anyMatch(g -> g.getDiscipline().equals(discipline)
                        && g.getSemester().equals(semester));
    }

    private void loadAllGroups() {
        this.allGroups = groupsDAO.loadAll();
    }
}

/*
@RequestScoped:
        Komponentas sukurtas kiekvienam HTTP užklausai
        Gyvuoja tik vienos užklausos metu
        Naudingas formų apdorojimui

@SessionScoped:
        Komponentas susiejamas su konkrečia vartotojo sesija
        Gyvuoja visą vartotojo sesijos laiką
        Naudingas vartotojo prisijungimo duomenims saugoti

@ApplicationScoped:
        Komponentas sukurtas vieną kartą visai aplikacijai
        Gyvuoja visą aplikacijos veikimo laiką
        Naudingas bendriems resursams ar konfigūracijai

@Inject:
        Nurodo, kad CDI sistema turi "įšvirkšti" (inject) reikalingą priklausomybę
        Automatiškai sujungia komponentus
 */