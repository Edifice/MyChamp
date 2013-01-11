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
            getRankingByGoalDeficit();
            
            goalsDefTie();
            if (tiedTeams.size() > 0) {
                getRankingByTotalGoals();
            }
            
        }
        return finalRankings;
    }

    public void clear() {
        finalRankings.clear();
        tiedTeams.clear();
        index = -1;
    }

    public ArrayList<Dummy> getGoalDeficit(Group group) throws SQLException {
        ArrayList<Match> matches = MM.getMatchesByGroup(group);
        ArrayList<Dummy> teamsWithGoals = new ArrayList<>();

        for (Team team : tiedTeams) {
            int goalsScored = 0;
            int goalsAgainst = 0;
            int goalsDeficit = 0;
            for (Match match : matches) {
                if (team.getID() == match.getHomeTeamID()) {
                    goalsScored += match.getHomeGoals();
                    goalsAgainst += match.getGuestGoals();

                } else if (team.getID() == match.getGuestTeamID()) {
                    goalsAgainst += match.getHomeGoals();
                    goalsScored += match.getGuestGoals();
                }
            }
            goalsDeficit = goalsScored - goalsAgainst;
            team.setGoalDeficit(goalsDeficit);
            teamsWithGoals.add(new Dummy(team, goalsDeficit));
        }
        return teamsWithGoals;
    }
    public void getRankingByGoalDeficit() throws SQLException {

        ArrayList<Dummy> teamsWithGoals = new ArrayList<>();

        teamsWithGoals = getGoalDeficit(GM.getGroupById(tiedTeams.get(0).getGroupID()));

        Collections.sort(teamsWithGoals);

        for (int i = 0; i < teamsWithGoals.size(); i++) {
            finalRankings.add(index, teamsWithGoals.get(i).getTeam());
            index++;
        }
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
            team.setTotalGoals(totalGoals);
            teamsWithGoals.add(new Dummy(team, totalGoals));
        }
        return teamsWithGoals;
    }

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
//        for (int i = 0; i < finalRankings.size(); i++) {
//            finalRankings.get(i).setRanking(i + 1);
//        }
    }

    private void pointsTie() {
        boolean indexFound = false;
        index = -1;
        for (int i = 0; i < finalRankings.size() - 1; i++) {
            if (finalRankings.get(i).getPoints() == finalRankings.get(i + 1).getPoints()) {
                if (!indexFound) {
                    index = i;
                    indexFound = true;
                }

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
    private void goalsDefTie() {
        boolean indexFound = false;
        index = -1;
        for (int i = 0; i < tiedTeams.size() - 1; i++) {
            if (tiedTeams.get(i).getGoalDeficit() == tiedTeams.get(i + 1).getGoalDeficit()) {
                if (!indexFound) {
                    index = i;
                    indexFound = true;
                }

//                if (!tiedTeams.contains(finalRankings.get(i))) {
//                    tiedTeams.add(finalRankings.get(i));
//                }
//                tiedTeams.add(finalRankings.get(i + 1));
            }
        }
        for (Team team : tiedTeams) {
            finalRankings.remove(team);
        }
    }
}
