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
    private ArrayList<Team> tiedTeamsWithGoals;
    private int index;

    public RankManager() throws Exception {
        try {
            TM = new TeamManager();
            MM = new MatchManager();
            GM = new GroupManager();
            finalRankings = new ArrayList<>();
            tiedTeams = new ArrayList<>();
            tiedTeamsWithGoals = new ArrayList<>();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public ArrayList<Team> constructFinalRankings(Group group) throws Exception {
        try {
            getRankingByPoints(group);

            pointsTie();
            if (tiedTeams.size() > 1) {
                getRankingByGoalDeficit();
                goalsDefTie();
                if (tiedTeamsWithGoals.size() > 1) {
                    getRankingByTotalGoals();
                    int localIndex = index;
                    for (int i = 0; i < tiedTeamsWithGoals.size(); i++) {
                        finalRankings.add(localIndex, tiedTeamsWithGoals.get(i));
                        localIndex++;
                    }
                } else {
                    int localIndex = index;
                    for (int i = 0; i < tiedTeams.size(); i++) {
                        finalRankings.add(localIndex, tiedTeams.get(i));
                        localIndex++;
                    }
                }

                tiedTeamsWithGoals.clear();
            }
            return finalRankings;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public ArrayList<Dummy> getGoalDeficit(Group group) throws Exception {
        try {
            ArrayList<Match> matches = MM.getMatchesByGroupPlayed(group);
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
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public void getRankingByGoalDeficit() throws Exception {
        try {
            ArrayList<Dummy> teamsWithGoals;

            teamsWithGoals = getGoalDeficit(GM.getGroupById(tiedTeams.get(0).getGroupID()));

            Collections.sort(teamsWithGoals);
            tiedTeams.clear();
            for (int i = 0; i < teamsWithGoals.size(); i++) {
                tiedTeams.add(teamsWithGoals.get(i).getTeam());
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public ArrayList<Dummy> getTotalGoals(Group group) throws Exception {
        try {
            ArrayList<Match> matches = MM.getMatchesByGroupPlayed(group);
            ArrayList<Dummy> teamsWithGoals = new ArrayList<>();
            for (Team team : tiedTeamsWithGoals) {
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
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public void getRankingByTotalGoals() throws Exception {
        try {
            ArrayList<Dummy> teamsWithGoals = new ArrayList<>();

            teamsWithGoals = getTotalGoals(GM.getGroupById(tiedTeamsWithGoals.get(0).getGroupID()));

            Collections.sort(teamsWithGoals);
            tiedTeamsWithGoals.clear();
            for (int i = 0; i < teamsWithGoals.size(); i++) {
                tiedTeamsWithGoals.add(teamsWithGoals.get(i).getTeam());
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Ranks a group in a descending order by points.
     *
     * @param group in which group this ranking should be done.
     * @return an ArrayList full of teams in the rank order.
     * @throws SQLException
     */
    public void getRankingByPoints(Group group) throws Exception {
        try {
            this.finalRankings = TM.getTeamByPoints(group);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
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

        for (int i = 0; i < tiedTeams.size() - 1; i++) {
            if (tiedTeams.get(i).getGoalDeficit() == tiedTeams.get(i + 1).getGoalDeficit()) {

                if (!tiedTeamsWithGoals.contains(tiedTeams.get(i))) {
                    tiedTeamsWithGoals.add(tiedTeams.get(i));
                }
                tiedTeamsWithGoals.add(tiedTeams.get(i + 1));
            }

        }
        for (Team team : tiedTeamsWithGoals) {
            tiedTeams.remove(team);
        }

        for (Team team : tiedTeams) {
            if (!finalRankings.contains(team)) {
                finalRankings.add(index, team);
            }
        }
        tiedTeams.clear();
    }
}