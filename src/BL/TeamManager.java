package BL;

import BE.Team;
import DAL.TeamDBManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamManager {

    private TeamDBManager DBM;

    public TeamManager() throws SQLException{
        DBM = new TeamDBManager();
    }
    
    public void addTeam(Team team) throws SQLException{
        DBM.addTeam(team);
    };
    
    public void updateTeam(Team team) throws SQLException {
        DBM.updateTeam(team);
    }
    
    public void removeTeam(int iden) throws SQLException {
        DBM.removeById(iden);
    }
    
    public ArrayList<Team> getAll() throws SQLException {
        return DBM.getAll();
    }
    
    public Team getById(int iden) throws SQLException {
        return DBM.getById(iden);
    }
    
    public void assignToGroup(Team team, int groupId) throws SQLException {
        DBM.assignToGroup(team, groupId);
    }
    
    public ArrayList<Team> getTeamsByGroup(int groupID) throws SQLException {
        return DBM.getTeamsByGroup(groupID);
    }
}
