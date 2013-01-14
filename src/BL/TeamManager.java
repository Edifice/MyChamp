package BL;

import BE.*;
import DAL.TeamDBManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class TeamManager {

    private TeamDBManager DBM;

    public TeamManager() throws Exception {
        try {
            DBM = new TeamDBManager();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public void generateDefaultTeams() throws Exception {
        try {
            DBM.generateDefaultTeams();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public ArrayList<Team> getAllWithGroupNames() throws Exception {
        try {
            ArrayList<Team> teams = DBM.getAllWithGroupNames();
            for (Team team : teams) {
                team.setPoints(calculatePoints(team));
            }
            return teams;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public void addTeam(Team team) throws Exception {
        try {
            DBM.addTeam(team);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public void updateTeam(Team team) throws Exception {
        try {
            DBM.updateTeam(team);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public void removeTeam(int iden) throws Exception {
        try {
            DBM.removeById(iden);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public ArrayList<Team> getAll() throws Exception {
        try {
            ArrayList<Team> teams = DBM.getAll();
            for (Team team : teams) {
                team.setPoints(calculatePoints(team));
            }
            return teams;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public Team getById(int iden) throws Exception {
        try {
            Team team = DBM.getById(iden);
            team.setPoints(calculatePoints(team));
            return team;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public void assignToGroup(Team team, int groupId) throws Exception {
        try {
            DBM.assignToGroup(team, groupId);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public ArrayList<Team> getTeamsByGroup(Group group) throws Exception {
        try {
            ArrayList<Team> teams = DBM.getTeamsByGroup(group);
            for (Team team : teams) {
                team.setPoints(calculatePoints(team));
            }
            return teams;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public void removeAll() throws Exception {
        try {
            DBM.removeAll();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public void assignPoints(Team team) throws Exception {
        try {
            DBM.assignPoints(team);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public ArrayList<Team> getTeamByPoints(Group group) throws Exception {
        try {
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
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    public ArrayList<ArrayList<Team>> getAllByGroup() throws Exception {
        try {
            ArrayList<ArrayList<Team>> teamss = DBM.getAllByGroup();
            for (ArrayList<Team> teams : teamss) {
                for (Team team : teams) {
                    team.setPoints(calculatePoints(team));
                }
            }
            return teamss;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    private int calculatePoints(Team team) throws Exception {
        try {
            MatchManager mm = new MatchManager();
            ArrayList<Match> matches = mm.getMatchesByTeam(team);
            int ret = 0;

            for (Match match : matches) {
                ret += mm.getPointsForTeamAtMatch(team, match);
            }
            return ret;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }
}
