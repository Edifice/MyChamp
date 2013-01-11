package BL;

import BE.Dummy;
import BE.Group;
import BE.Match;
import BE.Team;
import DAL.TeamDBManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class TeamManager {

    private TeamDBManager DBM;

    public TeamManager() throws SQLException {
        DBM = new TeamDBManager();
    }

    public ArrayList<Team> getAllWithGroupNames() throws SQLException {
        ArrayList<Team> teams = DBM.getAllWithGroupNames();
        for (Team team : teams) {
            team.setPoints(calculatePoints(team));
        }
        return teams;
    }

    public void addTeam(Team team) throws SQLException {
        DBM.addTeam(team);
    }

    ;
    
    public void updateTeam(Team team) throws SQLException {
        DBM.updateTeam(team);
    }

    public void removeTeam(int iden) throws SQLException {
        DBM.removeById(iden);
    }

    public ArrayList<Team> getAll() throws SQLException {
        ArrayList<Team> teams = DBM.getAll();
        for (Team team : teams) {
            team.setPoints(calculatePoints(team));
        }
        return teams;
    }

    public Team getById(int iden) throws SQLException {
        Team team = DBM.getById(iden);
        team.setPoints(calculatePoints(team));
        return team;
    }

    public void assignToGroup(Team team, int groupId) throws SQLException {
        DBM.assignToGroup(team, groupId);
    }

    public ArrayList<Team> getTeamsByGroup(Group group) throws SQLException {
        ArrayList<Team> teams = DBM.getTeamsByGroup(group);
        for (Team team : teams) {
            team.setPoints(calculatePoints(team));
        }
        return teams;
    }

    public void removeAll() throws SQLException {
        DBM.removeAll();
    }

    public void assignPoints(Team team) throws SQLException {
        DBM.assignPoints(team);
    }

    public ArrayList<Team> getTeamByPoints(Group group) throws SQLException {
        ArrayList<Team> teams = DBM.getTeamsByGroup(group);
        ArrayList<Dummy> dumb = new ArrayList<>();
        for (Team team : teams) {
            int points = calculatePoints(team);
            team.setPoints(points);
            dumb.add(new Dummy(team, points));
        }
        teams.clear();
        Collections.sort(dumb);
        for (Dummy db : dumb) {
            teams.add(db.getTeam());
        }
        return teams;
    }

    public ArrayList<ArrayList<Team>> getAllByGroup() throws SQLException {
        ArrayList<ArrayList<Team>> teamss = DBM.getAllByGroup();
        for (ArrayList<Team> teams : teamss) {
            for (Team team : teams) {
                team.setPoints(calculatePoints(team));
            }
        }
        return teamss;
    }

    private int calculatePoints(Team team) throws SQLException {
        MatchManager mm = new MatchManager();
        ArrayList<Match> matches = mm.getMatchesByTeam(team);
        int ret = 0;
        
        for(Match match: matches){
            ret += mm.getPointsForTeamAtMatch(team, match);
        }
        return ret;
    }
}
