package BL;

import BE.Group;
import BE.Match;
import BE.Team;
import DAL.MatchDBManager;
import DAL.TeamDBManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class MatchManager {

    private MatchDBManager DBM;
    private TeamDBManager TMC;

    public MatchManager() throws SQLException {
        DBM = new MatchDBManager();
    }
    
    public void createNewMatch(int round, Team homeTeam, Team guestTeam) throws SQLException{
        Match newMatch = new Match(round, homeTeam.getID(), guestTeam.getID());
        DBM.addMatch(newMatch);
        
    }
    
    public ArrayList<Match> getAll() throws SQLException {
        return DBM.getAll();
    }
    
    public void generateMatchesByGroup(Group group) throws SQLException {
        TMC = new TeamDBManager();
        ArrayList<Team> teams = TMC.getTeamsByGroup(group.getID());
        
        //Adds the dummy team.
        Team dummy = new Team();
        if (teams.size() % 2 != 0) {
            teams.add(dummy);
        }
        
        int rounds = teams.size() - 1;
        
        Team fixed = teams.get(0);
        teams.remove(0);
        
        for (int i=1; i <= rounds; i++) {
            Team second = teams.get(0);
            Team third = teams.get(1);
            Team fourth = teams.get(2);
            
            if (!second.equals(dummy)){
                createNewMatch(i, fixed, second);
                createNewMatch(i+rounds, second, fixed);
            }
            
            if (!third.equals(dummy) && !fourth.equals(dummy)) {
                createNewMatch(i, third, fourth);
                createNewMatch(i+rounds, fourth, third);
            }
            
            Collections.rotate(teams, 1);
        }
    }
}