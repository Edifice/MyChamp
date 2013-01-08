package DAL;

import BE.Match;
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
        qMatch.setBoolean(4, match.isIsPlayed());
        qMatch.setNull(5, java.sql.Types.INTEGER);
        qMatch.setNull(6, java.sql.Types.INTEGER);
                
        qMatch.executeUpdate();

        con.close();
    }

    public void updateMatch(Match match) throws SQLException {
        Connection con = dS.getConnection();

        PreparedStatement qTeam = con.prepareStatement("UPDATE Match SET  Round = ?, HomeTeamID = ?, GuestTeamID = ?, isPlayed = ? WHERE ID = ?");

        qTeam.setInt(1, match.getRound());
        qTeam.setInt(2, match.getHomeTeamID());
        qTeam.setInt(3, match.getGuestTeamID());
        qTeam.setBoolean(4, match.isIsPlayed());

        qTeam.setInt(5, match.getID());

        qTeam.executeUpdate();

        con.close();
    }
    
    public void updateScore(Match match) throws SQLException{
        Connection con = dS.getConnection();

        PreparedStatement qTeam = con.prepareStatement("UPDATE Match SET  HomeGoals = ?, GuestGoals = ? WHERE ID = ?");

        
        qTeam.setInt(2, match.getHomeGoals());
        qTeam.setInt(3, match.getGuestGoals());        

        qTeam.setInt(5, match.getID());

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

    @Override
    public ArrayList getAll() throws SQLException {
        Connection con = dS.getConnection();
        ArrayList<Match> matches = new ArrayList<>();

        PreparedStatement qAllMatches = con.prepareStatement("SELECT * FROM Match");
        ResultSet allMatches = qAllMatches.executeQuery();

        while (allMatches.next()) {
            matches.add(
                    new Match(
                    allMatches.getInt("ID"),
                    allMatches.getInt("MatchRound"),
                    allMatches.getInt("HomeTeamID"),
                    allMatches.getInt("GuesTeamID"),
                    allMatches.getBoolean("IsPlayed"),
                    allMatches.getInt("HomeGoals"),
                    allMatches.getInt("GuestGoals")));
        }

        con.close();
        return matches;

    }

}
