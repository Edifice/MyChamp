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
        
        ArrayList<Integer> totalgoalsofteams = new ArrayList<>();
        for (Team team : teams) {
            int totalGoals = 0;
            for (Match match : matches) {
                if (team.getID() == match.getHomeTeamID()) {
                    totalGoals += match.getHomeGoals();
                } else if (team.getID() == match.getGuestTeamID()) {
                    totalGoals += match.getGuestGoals();
                }
            }
            totalgoalsofteams.add(totalGoals);
        }
    }

    public ArrayList<Team> getRankingByPoints(Group group) throws SQLException {
        return TM.getTeamByPoints(group);
    }
}
