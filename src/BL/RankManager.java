package BL;

import BE.Dummy;
import BE.Group;
import BE.Match;
import BE.Team;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class RankManager {

    private TeamManager TM;
    private MatchManager MM;
    private GroupManager GM;
    
    public RankManager() throws SQLException {
        TM = new TeamManager();
        MM = new MatchManager();
        GM = new GroupManager();
    }
    
    public void rank() throws SQLException {
        ArrayList<Group> groups = GM.getAll();
        for (Group group : groups) {
            getRankingByTotalGoals(group);
        }
    }

    /**
     * Ranks a group in a descending order by their total scored goals.
     *
     * First we get the teams and the matches in a specific group and store them
     * in two different ArrayLists. We loop through all the teams and calculate
     * the total goals their scored by looping through all the matches and
     * selecting only those that we are interested in. After we got the total
     * goals for a certain team, at the end of the loop we create Dummy entities
     * with the teams and the totalGoals and put them inside an ArrayList. This
     * way we can easily sort them by the total amount of goals they scored and
     * we can easily re-create the ArrayList of Teams that we need to return at
     * the end of this method. We use the Comparable interface on the Dummy
     * class so we can sort them by using the Collections.sort(List<>) method.
     *
     * @param group the group in which the ranking should be done.
     * @return the ArrayList full of teams in the right order.
     * @throws SQLException
     */
    public ArrayList<Team> getRankingByTotalGoals(Group group) throws SQLException {
        ArrayList<Team> teams = TM.getTeamsByGroup(group);
        ArrayList<Match> matches = MM.getMatchesByGroup(group);
        ArrayList<Dummy> teamsWithGoals = new ArrayList<>();
        ArrayList<Team> res = new ArrayList<>();

        for (Team team : teams) {
            int totalGoals = 0;
            for (Match match : matches) {
                if (team.getID() == match.getHomeTeamID()) {
                    totalGoals += match.getHomeGoals();
                } else if (team.getID() == match.getGuestTeamID()) {
                    totalGoals += match.getGuestGoals();
                }
            }
            teamsWithGoals.add(new Dummy(team, totalGoals));
        }

        Collections.sort(teamsWithGoals);
        for (Dummy dumb : teamsWithGoals) {
            res.add(dumb.getTeam());
        }

        return res;

    }

    /**
     * Ranks a group in a descending order by points.
     *
     * @param group in which group this ranking should be done.
     * @return an ArrayList full of teams in the rank order.
     * @throws SQLException
     */
    public ArrayList<Team> getRankingByPoints(Group group) throws SQLException {
        return TM.getTeamByPoints(group);
    }
}
