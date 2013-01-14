package DAL;

import BE.Group;
import BE.Match;
import BE.Team;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class extends DBManager, and takes care of all the handling of the Match
 * table in the database.
 *
 * @author Martin
 */
public class MatchDBManager extends DBManager {
    
    private final int MAX_GROUP_ROUND = 6;

    public MatchDBManager() throws SQLException {

        super(); // Gets the connection to the database
    }

    /**
     * This method adds a Match entity's values into the Match table of the
     * database. Be aware that the match ID is auto-generated.
     *
     * @param match
     * @throws SQLException
     */
    public void addMatch(Match match) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qMatch = con.prepareStatement("INSERT INTO Match VALUES (?, ?, ?, ?, ?, ?)");


        qMatch.setInt(1, match.getRound());
        qMatch.setInt(2, match.getHomeTeamID());
        qMatch.setInt(3, match.getGuestTeamID());
        qMatch.setInt(4, match.getIsPlayed());
        qMatch.setNull(5, java.sql.Types.INTEGER);
        qMatch.setNull(6, java.sql.Types.INTEGER);

        qMatch.executeUpdate();

        con.close();
    }

    /**
     * This method updates an existing match in the Match table of the database,
     * with the new entity's values. Be aware that the match ID is
     * auto-generated.
     *
     * @param match
     * @throws SQLException s
     */
    public void updateScore(Match match) throws SQLException {
        Connection con = dS.getConnection();

        PreparedStatement qTeam = con.prepareStatement("UPDATE Match SET  HomeGoals = ?, GuestGoals = ?, IsPlayed = ? WHERE ID = ?");


        qTeam.setInt(1, match.getHomeGoals());
        qTeam.setInt(2, match.getGuestGoals());
        qTeam.setInt(3, match.getIsPlayed());

        qTeam.setInt(4, match.getID());

        qTeam.executeUpdate();

        con.close();
    }

    /**
     * This method is an abstract method; It removes a match from the database,
     * matching the id.
     *
     * @param iden is the id.
     * @throws SQLException
     */
    @Override
    public void removeById(int iden) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qMatch = con.prepareStatement("DELETE FROM Match WHERE ID = ?");
        qMatch.setInt(1, iden);

        qMatch.executeUpdate();

        con.close();
    }

    /**
     * This method gets a specific Match from the database, that matches the id
     *
     * @param id is the id
     * @return a Match entity.
     * @throws SQLException
     */
    public Match getMatchById(int id) throws SQLException {
        Connection con = dS.getConnection();
        Match match;

        PreparedStatement qAllMatches = con.prepareStatement("SELECT Match.*, t1.School as HomeTeamName, t2.School as GuestTeamName FROM Match INNER JOIN Team as t1 ON t1.ID = Match.HomeTeamID INNER JOIN Team as t2 ON t2.ID = Match.GuestTeamID WHERE Match.ID = ? ORDER BY Match.MatchRound ASC;");

        qAllMatches.setInt(1, id);
        ResultSet allMatches = qAllMatches.executeQuery();

        allMatches.next();
        match = new Match(
                allMatches.getInt("ID"),
                allMatches.getInt("MatchRound"),
                allMatches.getInt("HomeTeamID"),
                allMatches.getInt("GuestTeamID"),
                allMatches.getInt("IsPlayed"),
                allMatches.getInt("HomeGoals"),
                allMatches.getInt("GuestGoals"),
                allMatches.getString("HomeTeamName"),
                allMatches.getString("GuestTeamName"));

        con.close();
        return match;

    }

    /**
     * This method is an abstract method; It gets all the matches from the
     * database, and adds them to an ArrayList.
     *
     * @return the ArrayList containing all the matches.
     * @throws SQLException
     */
    @Override
    public ArrayList<Match> getAll() throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Match> matches = new ArrayList<>();

        PreparedStatement qAllMatches = con.prepareStatement("SELECT Match.ID, Match.MatchRound, Match.HomeTeamID, Match.GuestTeamID, Match.IsPlayed, Match.HomeGoals, Match.GuestGoals, t1.School as HomeTeamName, t2.School as GuestTeamName FROM Match INNER JOIN Team as t1 ON t1.ID = Match.HomeTeamID INNER JOIN Team as t2 ON t2.ID = Match.GuestTeamID ORDER BY Match.MatchRound ASC;");
        ResultSet allMatches = qAllMatches.executeQuery();

        while (allMatches.next()) {
            matches.add(
                    new Match(
                    allMatches.getInt("ID"),
                    allMatches.getInt("MatchRound"),
                    allMatches.getInt("HomeTeamID"),
                    allMatches.getInt("GuestTeamID"),
                    allMatches.getInt("IsPlayed"),
                    allMatches.getInt("HomeGoals"),
                    allMatches.getInt("GuestGoals"),
                    allMatches.getString("HomeTeamName"),
                    allMatches.getString("GuestTeamName")));
        }

        con.close();
        return matches;

    }

    /**
     * This method gets all the matches, where either home or guest team,
     * matches the group id; So it gets all the matches within one group.
     *
     * @param group is the group entity, containing the group ID.
     * @return an ArrayList of all matches within one group.
     * @throws SQLException
     */
    public ArrayList<Match> getMatchesByGroup(Group group) throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Match> matches = new ArrayList<>();

        PreparedStatement qAllMatches = con.prepareStatement("SELECT Match.* FROM Match INNER JOIN Team ON Match.HomeTeamID = Team.ID WHERE Team.GroupID = ?");
        qAllMatches.setInt(1, group.getID());
        ResultSet allMatches = qAllMatches.executeQuery();

        while (allMatches.next()) {
            matches.add(
                    new Match(
                    allMatches.getInt("ID"),
                    allMatches.getInt("MatchRound"),
                    allMatches.getInt("HomeTeamID"),
                    allMatches.getInt("GuestTeamID"),
                    allMatches.getInt("IsPlayed"),
                    allMatches.getInt("HomeGoals"),
                    allMatches.getInt("GuestGoals")));
        }

        con.close();
        return matches;

    }

    /**
     * This method gets all the matches, where either home or guest team,
     * matches the group id, and where the match has been played; So it gets all
     * the matches within one group, that has been played;
     *
     * @param group is the group entity, containing the group ID.
     * @return an ArrayList of all matches within one group.
     * @throws SQLException
     */
    public ArrayList<Match> getMatchesByGroupPlayed(Group group) throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Match> matches = new ArrayList<>();

        PreparedStatement qAllMatches = con.prepareStatement("SELECT Match.* FROM Match INNER JOIN Team ON Match.HomeTeamID = Team.ID WHERE Team.GroupID = ? AND Match.IsPlayed = 1");
        qAllMatches.setInt(1, group.getID());
        ResultSet allMatches = qAllMatches.executeQuery();

        while (allMatches.next()) {
            matches.add(
                    new Match(
                    allMatches.getInt("ID"),
                    allMatches.getInt("MatchRound"),
                    allMatches.getInt("HomeTeamID"),
                    allMatches.getInt("GuestTeamID"),
                    allMatches.getInt("IsPlayed"),
                    allMatches.getInt("HomeGoals"),
                    allMatches.getInt("GuestGoals")));
        }

        con.close();
        return matches;

    }

    /**
     * Gets all matches where either the home or guest team id matches the given
     * id.
     *
     * @param t is the team entity, containing the given id.
     * @return
     * @throws SQLException
     */
    public ArrayList<Match> getMatchesByTeam(Team t) throws SQLException {

        Connection con = dS.getConnection();
        ArrayList<Match> matches = new ArrayList<>();

        PreparedStatement qAllMatches = con.prepareStatement(
                "SELECT Match.*, t1.School as HomeTeamName, t2.School as GuestTeamName "
                + "FROM Match "
                + "INNER JOIN Team as t1 ON t1.ID = Match.HomeTeamID "
                + "INNER JOIN Team as t2 ON t2.ID = Match.GuestTeamID "
                + "WHERE t1.ID = ? OR t2.ID = ?" /*+ "ORDER BY Match.MatchRound ASC "*/);

        qAllMatches.setInt(1, t.getID());
        qAllMatches.setInt(2, t.getID());

        ResultSet allMatches = qAllMatches.executeQuery();

        while (allMatches.next()) {
            matches.add(
                    new Match(
                    allMatches.getInt("ID"),
                    allMatches.getInt("MatchRound"),
                    allMatches.getInt("HomeTeamID"),
                    allMatches.getInt("GuestTeamID"),
                    allMatches.getInt("IsPlayed"),
                    allMatches.getInt("HomeGoals"),
                    allMatches.getInt("GuestGoals"),
                    allMatches.getString("HomeTeamName"),
                    allMatches.getString("GuestTeamName")));
        }

        con.close();
        return matches;
    }

    /**
     * This method is an abstract method; It removes all matches from the
     * database, and resets the Identity to 0;
     *
     * @throws SQLException
     */
    @Override
    public void removeAll() throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qData = con.prepareStatement("DELETE FROM Match; DBCC CHECKIDENT (Match, RESEED, 0)");
        qData.executeUpdate();

        con.close();
    }

    /**
     * This method calculates the total amount of rounds.
     *
     * @return an Integer containing the total number of rounds.
     * @throws SQLException
     */
    public int maxRoundNumber() throws SQLException {
        Connection con = dS.getConnection();

        PreparedStatement qAllMatches = con.prepareStatement("SELECT MAX(MatchRound) as MaxRound FROM Match");
        ResultSet allMatches = qAllMatches.executeQuery();

        allMatches.next();

        int ret = allMatches.getInt("MaxRound");

        con.close();
        return ret;

    }

    /**
     * This method makes a check, to see if all matches has been played.
     *
     * @return true if matches are played, false if not.
     * @throws SQLException
     */
    public boolean isAllPlayed() throws SQLException {
        Connection con = dS.getConnection();

        boolean allPlayed;

        PreparedStatement qAllMatches = con.prepareStatement("SELECT COUNT(s.IsPlayed) as Res FROM (SELECT isPlayed FROM Match WHERE isPlayed = 0 GROUP BY isPlayed) as s");
        ResultSet allMatches = qAllMatches.executeQuery();
        allMatches.next();
        allPlayed = allMatches.getInt("Res") == 0;

        con.close();
        return allPlayed;
    }

    /**
     * This method gets the next match for the finals.
     * @return a match for the finals. 
     * @throws SQLException 
     */
    public Match getNextFinalMatch() throws SQLException {
        Connection con = dS.getConnection();

        PreparedStatement qAllMatches = con.prepareStatement("SELECT TOP 1 Match.*, t1.School as HomeTeamName, t2.School as GuestTeamName FROM Match INNER JOIN Team as t1 ON t1.ID = Match.HomeTeamID INNER JOIN Team as t2 ON t2.ID = Match.GuestTeamID WHERE MatchRound > ? AND isPlayed = 0");
        qAllMatches.setInt(1, MAX_GROUP_ROUND);
        ResultSet allMatches = qAllMatches.executeQuery();
        Match ret = null;
        if (! !allMatches.next()) {
            ret = new Match(
                    allMatches.getInt("ID"),
                    allMatches.getInt("MatchRound"),
                    allMatches.getInt("HomeTeamID"),
                    allMatches.getInt("GuestTeamID"),
                    allMatches.getInt("IsPlayed"),
                    allMatches.getInt("HomeGoals"),
                    allMatches.getInt("GuestGoals"),
                    allMatches.getString("HomeTeamName"),
                    allMatches.getString("GuestTeamName"));
        }

        con.close();

        return ret;
    }
    /**
     * This method gets the last four matches to be played. 
     * @return an ArrayList containing the matches. 
     * @throws SQLException 
     */
    public ArrayList<Match> getLast4Match() throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Match> matches = new ArrayList<>();

        //PreparedStatement qAllMatches = con.prepareStatement("SELECT TOP 4 * FROM Match ORDER BY ID DESC");
        PreparedStatement qAllMatches = con.prepareStatement("SELECT TOP 4 Match.*, t1.School as HomeTeamName, t2.School as GuestTeamName FROM Match INNER JOIN Team as t1 ON t1.ID = Match.HomeTeamID INNER JOIN Team as t2 ON t2.ID = Match.GuestTeamID ORDER BY ID DESC");
        ResultSet allMatches = qAllMatches.executeQuery();

        while (allMatches.next()) {
            matches.add(
                    new Match(
                    allMatches.getInt("ID"),
                    allMatches.getInt("MatchRound"),
                    allMatches.getInt("HomeTeamID"),
                    allMatches.getInt("GuestTeamID"),
                    allMatches.getInt("IsPlayed"),
                    allMatches.getInt("HomeGoals"),
                    allMatches.getInt("GuestGoals"),
                    allMatches.getString("HomeTeamName"),
                    allMatches.getString("GuestTeamName")));
        }

        con.close();
        return matches;
    }
}
