package BL;

import BE.*;
import DAL.TeamDBManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Deals with the team management. It has the methods to add, update and remove
 * a team, get the teams either by group names as well or just as they supposed
 * to be with group ids only. This class also takes care of assigning and
 * calculating the points for the teams.
 *
 * @author anthony
 */
public class TeamManager {

    private TeamDBManager DBM;

    public TeamManager() throws Exception {
        try {
            DBM = new TeamDBManager();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Add a team. Takes in a new team entity and pushes it to the database.
     *
     * @param team Team entity which we want to add.
     * @throws Exception because it deals with the database.
     */
    public void addTeam(Team team) throws Exception {
        try {
            DBM.addTeam(team);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Update a team. Takes in an updated team entity and pushes it to the
     * database.
     *
     * @param team Team entity that we wan to push.
     * @throws Exception because it deals with the database.
     */
    public void updateTeam(Team team) throws Exception {
        try {
            DBM.updateTeam(team);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Remove a team by id. It takes in a team id and removes it.
     *
     * @param iden int id that we want to remove.
     * @throws Exception because it deals with the database.
     */
    public void removeTeam(int iden) throws Exception {
        try {
            DBM.removeById(iden);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Gets all the teams.
     *
     * @return ArrayList<Team> with all of the teams.
     * @throws Exception because it deals with the database.
     */
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

    /**
     * Gets a specific team by id.
     *
     * @param iden the id of the team we want to return
     * @return Team entity.
     * @throws Exception because it deals with the database.
     */
    public Team getById(int iden) throws Exception {
        try {
            Team team = DBM.getById(iden);
            team.setPoints(calculatePoints(team));
            return team;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Assign a team to a group. It takes in a team entity, that we want to
     * update and a group id.
     *
     * @param team Team entity which we want to update
     * @param groupId int the group id we want to assign to a group.
     * @throws Exception because it deals with the database.
     */
    public void assignToGroup(Team team, int groupId) throws Exception {
        try {
            DBM.assignToGroup(team, groupId);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Generates default teams. This method is only used for debugging and
     * testing.
     *
     * @throws Exception because it deals with the database.
     */
    public void generateDefaultTeams() throws Exception {
        try {
            DBM.generateDefaultTeams();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Gets all the teams with group names and sets the points for them.
     *
     * @return ArrayList<Team> the list of teams with group names and points.
     * @throws Exception because it deals with the database.
     */
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

    /**
     * Gets all the teams by a specific group.
     *
     * @param group Group entity.
     * @return ArrayList<Team> the list of teams in a specific group.
     * @throws Exception because it deals with the database.
     */
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

    /**
     * Removes all the teams.
     *
     * @throws Exception because it deals with the database.
     */
    public void removeAll() throws Exception {
        try {
            DBM.removeAll();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Assigns points to a team. It takes in an updated team entity with the new
     * points and pushes it to the database.
     *
     * @param team Team entity, which is updated with the points and ready to be
     * pushed.
     * @throws Exception because it deals with the database.
     */
    public void assignPoints(Team team) throws Exception {
        try {
            DBM.assignPoints(team);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Gets the teams in the order of points inside a group.
     *
     * @param group Group entity in which the teams are checked.
     * @return ArrayList<Team> the list of teams in the order of their points.
     * @throws Exception because it deals with the database.
     */
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

    /**
     * Gets all the teams from all the groups.
     *
     * @return ArrayList<ArrayList<Team>> the list of groups which contains list
     * of the teams with points.
     * @throws Exception because it deals with the database.
     */
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

    /**
     * Calculate the points for a team. It takes in a team entity and calculates
     * all of their points they got from they already played matches.
     *
     * @param team Team entity, of which we want to calculate the points.s
     * @return int ret the total number of points for a specific team.
     * @throws Exception because it deals with the database.
     */
    private int calculatePoints(Team team) throws Exception {
        try {
            //Bug: If you move this to the contructor the menu breaks.
            MatchManager MM = new MatchManager();
            ArrayList<Match> matches = MM.getMatchesByTeam(team);
            int ret = 0;

            for (Match match : matches) {
                ret += MM.getPointsForTeamAtMatch(team, match);
            }
            return ret;
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error"); 
        }
    }
}
