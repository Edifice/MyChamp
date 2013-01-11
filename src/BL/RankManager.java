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
    private ArrayList<Team> finalRankings;
    private ArrayList<Team> tiedTeams;
    private int index;

    public RankManager() throws SQLException {
        TM = new TeamManager();
        MM = new MatchManager();
        GM = new GroupManager();
        finalRankings = new ArrayList<>();
        tiedTeams = new ArrayList<>();
    }

    public ArrayList<Team> constructFinalRankings(Group group) throws SQLException {
        clear();
        getRankingByPoints(group);
        pointsTie();
        if (tiedTeams.size() > 0) {
            getRankingByTotalGoals();
        }
        return finalRankings;
    }
    
    public void clear() {
        finalRankings.clear();
        tiedTeams.clear();
        index = -1;
    }

    public ArrayList<Dummy> getTotalGoals(Group group) throws SQLException {
        ArrayList<Match> matches = MM.getMatchesByGroup(group);
        ArrayList<Dummy> teamsWithGoals = new ArrayList<>();
        for (Team team : tiedTeams) {
            int totalGoals = 0;
            for (Match match : matches) {
                if (team.getID() == match.getHomeTeamID()) {
                    totalGoals += match.getHomeGoals();
                } else if (team.getID() == match.getGuestTeamID()) {
                    totalGoals += match.getGuestGoals();
                }
            }
            teamsWithGoals.add(new Dummy(team, totalGoals, team.getRanking()));
        }
        return teamsWithGoals;
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
    public void getRankingByTotalGoals() throws SQLException {

        ArrayList<Dummy> teamsWithGoals = new ArrayList<>();

        teamsWithGoals = getTotalGoals(GM.getGroupById(tiedTeams.get(0).getGroupID()));

        Collections.sort(teamsWithGoals);

        for (int i = 0; i < teamsWithGoals.size(); i++) {
            finalRankings.add(index, teamsWithGoals.get(i).getTeam());
            index++;
        }
    }

    /**
     * Ranks a group in a descending order by points.
     *
     * @param group in which group this ranking should be done.
     * @return an ArrayList full of teams in the rank order.
     * @throws SQLException
     */
    public void getRankingByPoints(Group group) throws SQLException {
        this.finalRankings = TM.getTeamByPoints(group);
        for (int i = 0; i < finalRankings.size(); i++) {
            finalRankings.get(i).setRanking(i + 1);
        }
    }

    private void pointsTie() {
        boolean indexFound = false;
        index = -1;
        for (int i = 0; i < finalRankings.size() - 1; i++) {
            if (finalRankings.get(i).getPoints() == finalRankings.get(i + 1).getPoints()) {
                if(!indexFound) { index = i; indexFound = true; }
                
                if (!tiedTeams.contains(finalRankings.get(i))) {
                    tiedTeams.add(finalRankings.get(i));
                }
                tiedTeams.add(finalRankings.get(i + 1));
            }
        }
        for (Team team : tiedTeams) {
            finalRankings.remove(team);
        }
    }
}
