package DAL;

import BE.Group;
import BE.Match;
import BE.Team;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MatchDBManager extends DBManager {

    public MatchDBManager() throws SQLException {
        super();
    }

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

    @Override
    public void removeById(int iden) throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qMatch = con.prepareStatement("DELETE FROM Match WHERE ID = ?");
        qMatch.setInt(1, iden);

        qMatch.executeUpdate();

        con.close();
    }

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

    @Override
    public void removeAll() throws SQLException {
        Connection con = dS.getConnection();
        PreparedStatement qData = con.prepareStatement("DELETE FROM Match; DBCC CHECKIDENT (Match, RESEED, 0)");
        qData.executeUpdate();

        con.close();
    }

    public int maxRoundNumber() throws SQLException {
        Connection con = dS.getConnection();

        PreparedStatement qAllMatches = con.prepareStatement("SELECT MAX(MatchRound) as MaxRound FROM Match");
        ResultSet allMatches = qAllMatches.executeQuery();

        allMatches.next();

        int ret = allMatches.getInt("MaxRound");

        con.close();
        return ret;

    }

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

    public boolean readyToFinals() throws SQLException {
        return maxRoundNumber() == 6 && isAllPlayed();
    }

    public Match getNextFinalMatch() throws SQLException {
        Connection con = dS.getConnection();

        PreparedStatement qAllMatches = con.prepareStatement("SELECT TOP 1 Match.*, t1.School as HomeTeamName, t2.School as GuestTeamName FROM Match INNER JOIN Team as t1 ON t1.ID = Match.HomeTeamID INNER JOIN Team as t2 ON t2.ID = Match.GuestTeamID WHERE MatchRound > 6 AND isPlayed = 0");
        ResultSet allMatches = qAllMatches.executeQuery();
        Match ret = null;
        if (!!allMatches.next()) {
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
