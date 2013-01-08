package BL;

import BE.Group;
import BE.Team;
import DAL.MatchDBManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MatchManager {

    private MatchDBManager DBM;

    public MatchManager() throws SQLException {
        DBM = new MatchDBManager();
    }
    
    public void generateMatches() throws SQLException {
        GroupManager GM = new GroupManager();
        for (int rounds = 1; rounds <= 6; rounds++){
            ArrayList<Group> allGroups = GM.getAll();
            for (Group groups : allGroups){
                /*for (Team team : ()) {
                    //
                }*/
            } 
        }
    }
}