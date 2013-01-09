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

    public ArrayList<Match> getAll() throws SQLException {
        return DBM.getAll();
    }

    public void updateScore(Match match) throws SQLException {
        DBM.updateScore(match);
    }

    public void updateMatch(Match match) throws SQLException {
        DBM.updateMatch(match);
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
        ArrayList<Team> teams = TM.getTeamsByGroup(group.getID());

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