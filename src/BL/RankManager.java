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
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Constructs the final rankings for a group. It starts by checking the
     * rankings by points, if there are any ties it goes into the next ranking
     * rule, which is the one that ranks them by goal deficit. If there are
     * still any ties, it goes into the third rule, which ranks them by total
     * goals scored. After all checks are done it constructs the ArrayList and
     * returns it.
     *
     * @param group Group entity.
     * @return ArrayList<Team> in order of their final rankings.
     * @throws Exception because it deals with the database.
     */
    public ArrayList<Team> constructFinalRankings(Group group) throws Exception {
        try {
            getRankingByPoints(group); // First rule.
            pointsTie(); // Checks for ties by points.

            if (tiedTeams.size() > 1) {
                getRankingByGoalDeficit(); // Second rule
                goalsDefTie(); // Checks for ties by goal deficit.

                if (tiedTeamsWithGoals.size() > 1) {
                    getRankingByTotalGoals(); // Third rule.
                    //Reconstructs the final rankings.
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

                tiedTeamsWithGoals.clear(); // Preparing it for the next group ranking.
            }
            return finalRankings;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Ranks a group in a descending order by points.
     *
     * @param group Group entity in which group this ranking should be done.
     * @throws Exception because it deals with the database.
     */
    public void getRankingByPoints(Group group) throws Exception {
        try {
            this.finalRankings = TM.getTeamByPoints(group);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Checks for ties by points. If there are any it adds them to the tiedTeams
     * ArrayList and removes them from finalRankings and saves the index from
     * where the first team is removed in order to be added back to the right
     * positions in the finalRankings ArrayList.
     */
    private void pointsTie() {
        boolean indexFound = false;
        index = -1; // Initial value for the index.
        for (int i = 0; i < finalRankings.size() - 1; i++) {
            if (finalRankings.get(i).getPoints() == finalRankings.get(i + 1).getPoints()) {
                if (!indexFound) {
                    index = i; // Sets it to the position of the first found tied Team entity.
                    indexFound = true; // Sets it to true in case index is set.
                }
                // It makes sure that it doesn't add the same Team entity twice to the tiedTeams.
                if (!tiedTeams.contains(finalRankings.get(i))) {
                    tiedTeams.add(finalRankings.get(i));
                }
                tiedTeams.add(finalRankings.get(i + 1));
            }
        }
        // Removes the tied teams from the finalRankings.
        for (Team team : tiedTeams) {
            finalRankings.remove(team);
        }
    }

    /**
     * Calculates and stores the goal deficits for each team in a group. It
     * stores all the matches in an ArrayList<Match> and also creates an
     * ArrayList<Dummy> called teamsWithGoals. It loops through all the teams
     * and all their matches and calculates the goal deficit. It creates a dummy
     * entity out of a team and their goal deficit and adds it to the
     * teamsWithGoals ArrayList.
     *
     * @param group Group entity where we do the checking.
     * @return ArrayList<Dummy> containing teams and their goal deficit.
     * @throws Exception because it deals with the database.
     */
    public ArrayList<Dummy> getGoalDeficit(Group group) throws Exception {
        try {
            ArrayList<Match> matches = MM.getMatchesByGroupPlayed(group);
            ArrayList<Dummy> teamsWithGoals = new ArrayList<>();

            for (Team team : tiedTeams) {
                int goalsScored = 0;
                int goalsAgainst = 0;
                int goalsDeficit;
                for (Match match : matches) {
                    if (team.getID() == match.getHomeTeamID()) {
                        goalsScored += match.getHomeGoals();
                        goalsAgainst += match.getGuestGoals();
                    } else if (team.getID() == match.getGuestTeamID()) {
                        goalsAgainst += match.getHomeGoals();
                        goalsScored += match.getGuestGoals();
                    }
                }

                goalsDeficit = goalsScored - goalsAgainst; // Calculates the goal deficit.
                team.setGoalDeficit(goalsDeficit);
                teamsWithGoals.add(new Dummy(team, goalsDeficit)); // Creates a new Dummy entity with a team and it's goal deficit.
            }
            return teamsWithGoals;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Gets the rankings by goal deficit. It fills up the teamsWithGoals
     * ArrayList with the tied teams by goal deficit by calling a different
     * method in the same class called getGoalDeficit(Group group). After that
     * it sorts the ArrayList (using the compareTo method inherited from
     * Comparable) in a descending order by goal deficit. It adds back the
     * entities to the tiedTeams ArrayList in the right order.
     *
     * @throws Exception because it deals with the database.
     */
    public void getRankingByGoalDeficit() throws Exception {
        try {
            // Dummy is a comparable entity.
            ArrayList<Dummy> teamsWithGoals = getGoalDeficit(GM.getGroupById(tiedTeams.get(0).getGroupID()));

            Collections.sort(teamsWithGoals);

            tiedTeams.clear(); // Clears the tiedTeams to get the entities back in the right order.
            // Adds back the entities in the right order.
            for (int i = 0; i < teamsWithGoals.size(); i++) {
                tiedTeams.add(teamsWithGoals.get(i).getTeam());
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Checks for ties by goal deficit. It loops through the tiedTeams, adds the
     * entities that are tied to the tiedTeamsWithGoals ArrayList and removes
     * them from tiedTeams. The teams that are not tied on goals are added back
     * to the finalRankings to the right position using the index variable
     */
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
        tiedTeams.clear(); // It removes the remaining teams that were added to the finalRankings.
    }

    /**
     * Calculates and stores the total goals scored for each team in a group. It
     * stores all the matches in an ArrayList<Match> and also creates an
     * ArrayList<Dummy> called teamsWithGoals. It loops through all the teams
     * and all their matches and calculates their total goals scored. It creates
     * a dummy entity out of a team and their total goals scored and adds it to
     * the teamsWithGoals ArrayList.
     *
     * @param group Group entity where we do the checking.
     * @return ArrayList<Dummy> containing teams and their total goals scored.
     * @throws Exception because it deals with the database.
     */
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
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Gets the rankings by total goals scored. It fills up the teamsWithGoals
     * ArrayList with the tied teams by total goals scored by calling a
     * different method in the same class called getTotalGoals(Group group).
     * After that it sorts the ArrayList (using the compareTo method inherited
     * from Comparable) in a descending order by total goals scored. It adds
     * back the entities to the tiedTeamsWithGoals ArrayList in the right order.
     *
     * @throws Exception because it deals with the database.
     */
    public void getRankingByTotalGoals() throws Exception {
        try {
            ArrayList<Dummy> teamsWithGoals = getTotalGoals(GM.getGroupById(tiedTeamsWithGoals.get(0).getGroupID()));

            Collections.sort(teamsWithGoals);

            tiedTeamsWithGoals.clear();
            for (int i = 0; i < teamsWithGoals.size(); i++) {
                tiedTeamsWithGoals.add(teamsWithGoals.get(i).getTeam());
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }
}