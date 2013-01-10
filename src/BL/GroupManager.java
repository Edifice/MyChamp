package BL;

import BE.Group;
import BE.Team;
import DAL.GroupDBManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class GroupManager {

    private GroupDBManager DBM;
    private TeamManager TM;

    public GroupManager() throws SQLException {
        DBM = new GroupDBManager();
    }

    public ArrayList<Group> getAll() throws SQLException {
        return DBM.getAll();
    }

    public void addGroup(Group group) throws SQLException {
        DBM.addGroup(group);
    }

    public void updateGroup(Group group) throws SQLException {
        DBM.updateGroup(group);
    }

    public void removeById(int iden) throws SQLException {
        DBM.removeById(iden);
    }

    /**
     * Groups the teams into four different groups assigning them the ID's from
     * 1 to 4 until every team has a group assigned to it. It uses the
     * assignToGroup method which takes in a team and a group id. All the groups
     * have at least 3, but maximum 4 teams.
     *
     * @throws SQLException
     */
    public void groupTeams() throws SQLException {
        TM = new TeamManager();
        ArrayList<Team> allTeams = TM.getAll();
        Collections.shuffle(allTeams);
        int i = 1;
        for (Team team : allTeams) {
            TM.assignToGroup(team, i);
            if (i / 4 == 1) {
                i = 1;
            } else {
                i++;
            }
        }
    }
}
