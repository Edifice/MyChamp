package BL;

import BE.Group;
import BE.Match;
import BE.Team;
import DAL.MatchDBManager;
import DAL.TeamDBManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class RankManager {

    private TeamDBManager TM;
    private MatchDBManager MM;
    
    public RankManager() {
        
    }
    
    public void calculateTotalGoals(Group group) throws SQLException {
        ArrayList<Team> teams = TM.getTeamsByGroup(group);
        ArrayList<Match> matches = MM.getMatchesByGroup(group);
    }
    
    public ArrayList<Team> getRankingByPoints(Group group) throws SQLException {
        return TM.getTeamByPoints(group);
    }
}
