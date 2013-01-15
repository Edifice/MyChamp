package BL;

import BE.Group;
import BE.Match;
import BE.Team;
import DAL.MatchDBManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class deals with the match management.
 *
 * You can add, delete and update a team, list the teams, start and end a
 * tournament, get matches by group and only those that are already played and a
 * lot of other methods.
 *
 * @author anthony
 */
public class MatchManager {

    private MatchDBManager DBM;
    private TeamManager TM;
    private GroupManager GM;
    private RankManager RM;
    private final int VALUE_WIN = 3;
    private final int VALUE_TIE = 1;
    private final int VALUE_LOSE = 0;
    private final int MAX_GROUP_ROUND = 6;

    public MatchManager() throws Exception {
        try {
            DBM = new MatchDBManager();
            TM = new TeamManager();
            GM = new GroupManager();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Add a new match. Takes in a new match entity and pushes it to the
     * database.
     *
     * @param match new match entity
     * @throws Exception because it deals with the database.
     */
    public void addMatch(Match match) throws Exception {
        try {
            DBM.addMatch(match);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Start the tournament.
     *
     * Gets all the teams inside a group and schedules the matched by calling
     * for the groupTeams() and generateMatchesByGroup(group) methods.
     *
     * @throws Exception because it deals with the database.
     */
    public void startTournament() throws Exception {
        try {
            GM.groupTeams();
            ArrayList<Group> groups = GM.getAll();
            for (Group group : groups) {
                generateMatchesByGroup(group);
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * End the tournament.
     *
     * Deletes all the teams, depending the boolean parameter that is passed to
     * this method, and the matches from the database. Cleans up everything for
     * the next tournament.
     *
     * @param deleteTeams In case of true the teams are deleted from the
     * database, in case of false they're not.
     * @throws Exception because it deals with the database.
     */
    public void endTournament(boolean deleteTeams) throws Exception {
        try {
            if (deleteTeams) {
                TM.removeAll();
            }
            DBM.removeAll();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Creates a match.
     *
     * Creates a match and sends in to the database.
     *
     * @param round the round of the game
     * @param homeTeam the home team
     * @param guestTeam the guest team
     * @throws Exception because it deals with the database.
     */
    public void createNewMatch(int round, Team homeTeam, Team guestTeam) throws Exception {
        try {
            Match newMatch = new Match(round, homeTeam.getID(), guestTeam.getID());
            DBM.addMatch(newMatch);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Gets a match by a specific ID.
     *
     * @param id the id of the match we want to get
     * @return Match entity.
     * @throws Exception because it deals with the database.
     */
    public Match getMatchById(int id) throws Exception {
        try {
            return DBM.getMatchById(id);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Gets all the matches.
     *
     * @return ArrayList<Match> full of all the matches.
     * @throws Exception because it deals with the database.
     */
    public ArrayList<Match> getAll() throws Exception {
        try {
            return DBM.getAll();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Gets all the matches by a specific group.
     *
     * @param group Group entity, by which we get all the matches.
     * @return ArrayList<Match> full of matches from that group.
     * @throws Exception because it deals with the database.
     */
    public ArrayList<Match> getMatchesByGroup(Group group) throws Exception {
        try {
            return DBM.getMatchesByGroup(group);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Gets all the matches for a specific team.
     *
     * @param team Team entity.
     * @return ArrayList<Match> all the matches for that specific team.
     * @throws Exception because it deals with the database.
     */
    public ArrayList<Match> getMatchesByTeam(Team team) throws Exception {
        try {
            return DBM.getMatchesByTeam(team);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Gets all the played matches for a specific group.
     *
     * @param group Group entity, by which we get the matches.
     * @return ArrayList<Match> all the played matches inside the group.
     * @throws Exception because it deals with the database.
     */
    public ArrayList<Match> getMatchesByGroupPlayed(Group group) throws Exception {
        try {
            return DBM.getMatchesByGroupPlayed(group);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Update the score. This method takes in a match and the two scores and
     * updates it in the database.
     *
     * @param match the match we want to update.
     * @param homeScore the home score.
     * @param awayScore the guest score.
     * @throws Exception because it deals with the database.
     */
    public void updateScore(Match match, int homeScore, int awayScore) throws Exception {
        try {
            match.setHomeGoals(homeScore);
            match.setGuestGoals(awayScore);
            match.setIsPlayed(1);
            DBM.updateScore(match);
            assignPoints(match);

            if (readyToFinals() || (DBM.maxRoundNumber() > 6 && DBM.maxRoundNumber() < 9 && DBM.isAllPlayed())) {
                startFinals();
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Assign points in a match to both teams. It takes in a match and it
     * assigns the right points for each team by the match scores. The winner
     * gets 3 points, the loser 0 and in the event of a tie both team gets 1
     * point.
     *
     * @param match the match in which we want to assign the points.
     * @throws Exception because it deals with the database.
     */
    public void assignPoints(Match match) throws Exception {
        try {
            Team homeTeam = TM.getById(match.getHomeTeamID());
            Team guestTeam = TM.getById(match.getGuestTeamID());
            if (match.getHomeGoals() > match.getGuestGoals()) {
                homeTeam.setPoints(3);
                TM.assignPoints(homeTeam);
            } else if (match.getHomeGoals() < match.getGuestGoals()) {
                guestTeam.setPoints(VALUE_WIN);
                TM.assignPoints(guestTeam);
            } else if (match.getHomeGoals() == match.getGuestGoals()) {
                homeTeam.setPoints(VALUE_TIE);
                guestTeam.setPoints(1);
                TM.assignPoints(homeTeam);
                TM.assignPoints(guestTeam);
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Schedules the matches by group.
     *
     * This method uses the Round-robin tournament algorithm (see:
     * http://en.wikipedia.org/wiki/Round-robin_tournament) to generate the
     * matches in each group separately dividing them into rounds. Every team
     * meets the other teams in the group twice (once home once away) but do not
     * meet each other twice in consecutive rounds. In case where there are only
     * 3 team (odd number) in a group a dummy team is created but no matches are
     * generated for them, meaning the team who plays the dummy team stays for a
     * round. We store the teams inside an ArrayList where the first team is
     * fixed and the three others are rotated. For more information about the
     * algorithm please look for the source.
     *
     * @param group the group where the matches are generated
     * @throws Exception because it deals with the database.
     * @see http://en.wikipedia.org/wiki/Round-robin_tournament
     */
    public void generateMatchesByGroup(Group group) throws Exception {
        try {
            ArrayList<Team> teams = TM.getTeamsByGroup(group);

            //Adds the dummy team.
            Team dummy = new Team();
            if (teams.size() % 2 != 0) {
                teams.add(dummy);
            }

            //Calculates the rounds.
            int rounds = teams.size() - 1;

            //Fixes team at position 0 and then removes it from the list.
            Team fixed = teams.get(0);
            teams.remove(0);

            //Saves the remaining teams in separate instances and creates the matches.
            for (int i = 1; i <= rounds; i++) {
                Team second = teams.get(0);
                Team third = teams.get(1);
                Team fourth = teams.get(2);

                if (!second.equals(dummy)) {
                    createNewMatch(i, fixed, second);
                    createNewMatch(i + rounds, second, fixed);
                }

                if (!third.equals(dummy) && !fourth.equals(dummy)) {
                    createNewMatch(i, third, fourth);
                    createNewMatch(i + rounds, fourth, third);
                }

                //Rotates the list for the next round.
                Collections.rotate(teams, 1);
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Gets the points to one team in a specific match. It takes a team and a
     * match entity and returns the number of points for the team in that
     * specific match.
     *
     * @param team Team entity for which we want to get the points.
     * @param match Match entity in which we check for the results.
     * @return int the number of points for that specific team in that specific
     * match.
     */
    public int getPointsForTeamAtMatch(Team team, Match match) {
        if (match.getIsPlayed() != 1) {
            return 0;
        }
        if (match.getHomeGoals() == match.getGuestGoals()) {
            return VALUE_TIE;
        }
        return match.getHomeTeamID() == team.getID()
                ? (match.getHomeGoals() > match.getGuestGoals() ? VALUE_WIN : VALUE_LOSE)
                : (match.getHomeGoals() > match.getGuestGoals() ? VALUE_LOSE : VALUE_WIN);
    }

    /**
     * Is it the finals yet? //It checks if the current round number is the last
     * one yet, meaning if all the group matches are played it will begin the
     * quarter finals.
     *
     * @return true in case of **** , false in case of
     **** @throws Exception because it deals with the database.
     */
    public boolean isFinals() throws Exception {
        try {
            return DBM.maxRoundNumber() > MAX_GROUP_ROUND || readyToFinals();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Ready for the Finals.
     *
     * @return true in case of it is the last round in the group stage and all
     * matches are played, false in case of it is not.
     * @throws Exception because it deals with the database.
     */
    public boolean readyToFinals() throws Exception {
        try {
            return DBM.maxRoundNumber() == MAX_GROUP_ROUND && DBM.isAllPlayed();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Generates random scores for all the group matches. This method is only
     * for debugging and testing. Shouldn't be in the final product.
     *
     * @throws Exception because it deals with the database.
     */
    public void setRandom() throws Exception {
        try {
            Random r = new Random();
            for (Match match : DBM.getAll()) {
                if (!match.isIsPlayed()) {
                    updateScore(match, r.nextInt(5), r.nextInt(5));
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Gets the next match in the knock-out stage. //
     *
     * @return
     * @throws Exception because it deals with the database.
     */
    public Match getNextFinalMatch() throws Exception {
        try {
            Match match = DBM.getNextFinalMatch();
            return match;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Starts the knock-out stage. If the last round number is 6 it starts the
     * quarter finals, if it is 7 the semi-finals if it is 8 it starts the
     * finals.
     *
     * @throws Exception because it deals with the database.
     */
    public void startFinals() throws Exception {
        try {
            switch (DBM.maxRoundNumber()) {
                case 6:
                    startQuarterFinals();
                    break;
                case 7:
                    startSemiFinals();
                    break;
                case 8:
                    startFinal();
                    break;
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Generates the matches for the quarter finals. The team who finished in
     * the first place in one group will play the team who finished the second
     * place in the other group, and so on.
     *
     * @throws Exception because it deals with the database.
     */
    private void startQuarterFinals() throws Exception {
        RM = new RankManager();
        try {
            Match add = new Match();
            add.setHomeTeamID(RM.constructFinalRankings(GM.getGroupById(1)).get(0).getID());
            add.setGuestTeamID(RM.constructFinalRankings(GM.getGroupById(2)).get(1).getID());
            add.setRound(7);
            addMatch(add);

            add = new Match();
            add.setHomeTeamID(RM.constructFinalRankings(GM.getGroupById(2)).get(0).getID());
            add.setGuestTeamID(RM.constructFinalRankings(GM.getGroupById(1)).get(1).getID());
            add.setRound(7);
            addMatch(add);

            add = new Match();
            add.setHomeTeamID(RM.constructFinalRankings(GM.getGroupById(3)).get(0).getID());
            add.setGuestTeamID(RM.constructFinalRankings(GM.getGroupById(4)).get(1).getID());
            add.setRound(7);
            addMatch(add);

            add = new Match();
            add.setHomeTeamID(RM.constructFinalRankings(GM.getGroupById(4)).get(0).getID());
            add.setGuestTeamID(RM.constructFinalRankings(GM.getGroupById(3)).get(1).getID());
            add.setRound(7);
            addMatch(add);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Generate the matches for the semi-finals. The team who won the first
     * quarter finals will play the other team who won the second quarter finals
     * and so on.
     *
     * @throws Exception because it deals with the database.
     */
    private void startSemiFinals() throws Exception {
        try {
            Match add = new Match();
            ArrayList<Match> matches = DBM.getLast4Match();

            add.setGuestTeamID(matches.get(3).getGuestGoals() > matches.get(3).getHomeGoals() ? matches.get(3).getGuestTeamID() : matches.get(3).getHomeTeamID());
            add.setHomeTeamID(matches.get(2).getGuestGoals() > matches.get(2).getHomeGoals() ? matches.get(2).getGuestTeamID() : matches.get(2).getHomeTeamID());
            add.setRound(8);
            addMatch(add);

            add = new Match();
            add.setGuestTeamID(matches.get(1).getGuestGoals() > matches.get(1).getHomeGoals() ? matches.get(1).getGuestTeamID() : matches.get(1).getHomeTeamID());
            add.setHomeTeamID(matches.get(0).getGuestGoals() > matches.get(0).getHomeGoals() ? matches.get(0).getGuestTeamID() : matches.get(0).getHomeTeamID());
            add.setRound(8);
            addMatch(add);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Generates the matches for the finals. The team who won the first
     * semi-final will play the other team who won the other semi-final.
     *
     * @throws Exception because it deals with the database.
     */
    private void startFinal() throws Exception {
        try {
            Match add = new Match();
            ArrayList<Match> matches = DBM.getLast4Match();

            add.setGuestTeamID(matches.get(1).getGuestGoals() > matches.get(1).getHomeGoals() ? matches.get(1).getGuestTeamID() : matches.get(1).getHomeTeamID());
            add.setHomeTeamID(matches.get(0).getGuestGoals() > matches.get(0).getHomeGoals() ? matches.get(0).getGuestTeamID() : matches.get(0).getHomeTeamID());
            add.setRound(9);
            addMatch(add);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error: " + ex.getLocalizedMessage());
        }
    }
}