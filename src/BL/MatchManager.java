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
    private TeamDBManager TM;
    private GroupManager GM;
    private final int VALUE_WIN = 3;
    private final int VALUE_TIE = 1;

    public MatchManager() throws SQLException {
        DBM = new MatchDBManager();
    }

    /**
     * Start the tournament.
     *
     * Gets all the teams inside a group and schedules the matched by calling
     * for the groupTeams() and generateMatchesByGroup(group) methods.
     *
     * @throws SQLException
     */
    public void startTournament() throws SQLException {
        GM = new GroupManager();
        GM.groupTeams();
        ArrayList<Group> groups = GM.getAll();
        for (Group group : groups) {
            generateMatchesByGroup(group);
        }
    }
    
    public void endTournament() throws SQLException {
        TM.removeAll();
        DBM.removeAll();
    }

    /**
     * Creates a match.
     *
     * Creates a match and sends in to the database.
     *
     * @param round the round of the game
     * @param homeTeam the home team
     * @param guestTeam the guest team
     * @throws SQLException
     */
    public void createNewMatch(int round, Team homeTeam, Team guestTeam) throws SQLException {
        Match newMatch = new Match(round, homeTeam.getID(), guestTeam.getID());
        DBM.addMatch(newMatch);
    }
    
    public Match getMatchById(int id) throws SQLException {
        return DBM.getMatchById(id);
    }

    public ArrayList<Match> getAll() throws SQLException {
        return DBM.getAll();
    }
    
    public ArrayList<Match> getMatchesByGroup(Group group) throws SQLException {
        return DBM.getMatchesByGroup(group);
    }

    public void updateScore(Match match, int homeScore, int awayScore) throws SQLException {
        match.setHomeGoals(homeScore);
        match.setGuestGoals(awayScore);
        match.setIsPlayed(1);
        DBM.updateScore(match);
        assignPoints(match);
    }
    
    public void assignPoints(Match match) throws SQLException {
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
     * @throws SQLException
     * @see http://en.wikipedia.org/wiki/Round-robin_tournament
     */
    public void generateMatchesByGroup(Group group) throws SQLException {
        TM = new TeamDBManager();
        ArrayList<Team> teams = TM.getTeamsByGroup(group);

        //Adds the dummy team.
        Team dummy = new Team();
        if (teams.size() % 2 != 0) {
            teams.add(dummy);
        }

        int rounds = teams.size() - 1;

        Team fixed = teams.get(0);
        teams.remove(0);

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

            Collections.rotate(teams, 1);
        }
    }
}