package BL;

import BE.Group;
import BE.Team;
import DAL.GroupDBManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class deals with the group management.
 *
 * It has the methods to add, get, delete, update groups and also just to get
 * the names of the groups in an ArrayList of string.
 *
 * @author anthony
 */
public class GroupManager {

    private GroupDBManager DBM;
    private TeamManager TM;

    public GroupManager() throws Exception {
        try {
            DBM = new GroupDBManager();
            TM = new TeamManager();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Get all the groups.
     *
     * @return ArrayList<Group>
     * @throws Exception because it deals with the database.
     */
    public ArrayList<Group> getAll() throws Exception {
        try {
            return DBM.getAll();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }

    }

    /**
     * Add a group. Takes in a new group and pushes it to the database.
     *
     * @param group the group we want to add.
     * @throws Exception because it deals with the database.
     */
    public void addGroup(Group group) throws Exception {
        try {
            DBM.addGroup(group);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Update a group. Takes in a updated group and pushes it to the database.
     *
     * @param group an updated group
     * @throws Exception because it deals with the database.
     */
    public void updateGroup(Group group) throws Exception {
        try {
            DBM.updateGroup(group);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Remove a group by ID.
     *
     * @param iden id of the group we want to remove.
     * @throws Exception because it deals with the database.
     */
    public void removeById(int iden) throws Exception {
        try {
            DBM.removeById(iden);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Gets a group by ID.
     *
     * @param id the id of the group we want to get.
     * @return Group
     * @throws Exception because it deals with the database.
     */
    public Group getGroupById(int id) throws Exception {
        try {
            return DBM.getGroupById(id);
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Gets the group names.
     *
     * Gets the group names from stores them in an ArrayList of Strings.
     *
     * @return ArrayList<String> the names of the groups.
     * @throws Exception because it deals with the database.
     */
    public ArrayList<String> getGroupNames() throws Exception {
        try {
            return DBM.getGroupNames();
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }

    /**
     * Groups the teams into four different groups assigning them the ID's from
     * 1 to 4 until every team has a group assigned to it. It uses the
     * assignToGroup method which takes in a team and a group id. All the groups
     * have at least 3, but maximum 4 teams.
     *
     * @throws Exception because it deals with the database. manager.
     */
    public void groupTeams() throws Exception {
        try {
            ArrayList<Team> allTeams = TM.getAll();
            Collections.shuffle(allTeams);
            int i = 1;
            for (Team team : allTeams) {
                TM.assignToGroup(team, i);
                if (i / 4 == 1) {
                    i = 1;
                } else {
                    i++;
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Couldn't access the database due to a database error");
        }
    }
}
