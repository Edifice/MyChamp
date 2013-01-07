package BL;

import BE.Team;
import DAL.GroupDBManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class GroupManager {
    
    private GroupDBManager DBM;
    
    public GroupManager(){
        DBM = new GroupDBManager();
    }
    
    public void groupTeams() throws SQLException {
        TeamManager TM = new TeamManager(); 
        ArrayList<Team> teams = TM.getAll();
        int i = 1;
        for (Team team : teams) { 
            TM.assignToGroup(team, i);
            if ((i/4) == 1) {i = 1;} else {i++;}
        }
    }
}
