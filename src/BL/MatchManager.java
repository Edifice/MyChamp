package BL;

import BE.Group;
import BE.Match;
import BE.Team;
import DAL.MatchDBManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MatchManager {

    private MatchDBManager DBM;
    private TeamManager TM;
    private GroupManager GM;
    private RankManager RM;
    private final int VALUE_WIN = 3;
    private final int VALUE_TIE = 1;

    public MatchManager() throws SQLException {
        DBM = new MatchDBManager();
        TM = new TeamManager();
        GM = new GroupManager();
    }

    public void addMatch(Match match) throws SQLException {
        DBM.addMatch(match);
    }

    /**
     * Start the tournament.
     *
     * Gets all the teams inside a group and schedules the matched by calling
     * for the groupTeams() and generateMatchesByGroup(group) methods.
     *
     * @throws SQLException Exception, because it deals with the database
     * manager.
     */
    public void startTournament() throws SQLException {
        GM.groupTeams();
        ArrayList<Group> groups = GM.getAll();
        for (Group group : groups) {
            generateMatchesByGroup(group);
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
     * @throws SQLException Exception, because it deals with the database
     * manager.
     */
    public void endTournament(boolean deleteTeams) throws SQLException {
        if (deleteTeams) {
            TM.removeAll();
        }
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
     * @throws SQLException Exception, because it deals with the database
     * manager.
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

    public ArrayList<Match> getMatchesByTeam(Team team) throws SQLException {
        return DBM.getMatchesByTeam(team);
    }

    /**
     *
     * @param match
     * @param homeScore
     * @param awayScore
     * @throws SQLException
     */
    public void updateScore(Match match, int homeScore, int awayScore) throws SQLException {
        match.setHomeGoals(homeScore);
        match.setGuestGoals(awayScore);
        match.setIsPlayed(1);
        DBM.updateScore(match);
        assignPoints(match);

        if (readyToFinals() || (DBM.maxRoundNumber() > 6 && DBM.maxRoundNumber() < 9 && DBM.isAllPlayed())) {
            startFinals();
        }
    }

    /**
     *
     * @param match
     * @throws SQLException
     */
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
     * @throws SQLException Exception, because it deals with the database
     * manager.
     * @see http://en.wikipedia.org/wiki/Round-robin_tournament
     */
    public void generateMatchesByGroup(Group group) throws SQLException {
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
    }

    /**
     *
     * @param team
     * @param match
     * @return
     */
    public int getPointsForTeamAtMatch(Team team, Match match) {
        if (match.getIsPlayed() != 1) {
            return 0;
        }
        if (match.getHomeGoals() == match.getGuestGoals()) {
            return 1;
        }
        return match.getHomeTeamID() == team.getID()
                ? (match.getHomeGoals() > match.getGuestGoals() ? 3 : 0)
                : (match.getHomeGoals() > match.getGuestGoals() ? 0 : 3);
    }

    /**
     *
     * @return @throws SQLException
     */
    public boolean isFinals() throws SQLException {
        return DBM.maxRoundNumber() > 6 || readyToFinals();
    }

    /**
     *
     * @return @throws SQLException
     */
    public boolean readyToFinals() throws SQLException {
        return DBM.readyToFinals();
    }

    /**
     *
     * @throws SQLException
     */
    public void setRandom() throws SQLException {
        Random r = new Random();
        for (Match match : DBM.getAll()) {
            if (!match.isIsPlayed()) {
                updateScore(match, r.nextInt(5), r.nextInt(5));
            }
        }
    }

    /**
     *
     * @return @throws SQLException
     */
    public Match getNextFinalMatch() throws SQLException {
        Match match = DBM.getNextFinalMatch();
        return match;
    }

    /**
     *
     * @throws SQLException
     */
    public void startFinals() throws SQLException {
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
    }

    /**
     *
     * @throws SQLException
     */
    private void startQuarterFinals() throws SQLException {
        //If I move this to the constructor it breaks the menu.
        RM = new RankManager();

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

    }

    /**
     *
     * @throws SQLException
     */
    private void startSemiFinals() throws SQLException {
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

    }

    /**
     *
     * @throws SQLException
     */
    private void startFinal() throws SQLException {
        Match add = new Match();
        ArrayList<Match> matches = DBM.getLast4Match();

        add.setGuestTeamID(matches.get(1).getGuestGoals() > matches.get(1).getHomeGoals() ? matches.get(1).getGuestTeamID() : matches.get(1).getHomeTeamID());
        add.setHomeTeamID(matches.get(0).getGuestGoals() > matches.get(0).getHomeGoals() ? matches.get(0).getGuestTeamID() : matches.get(0).getHomeTeamID());
        add.setRound(9);
        addMatch(add);

    }
}