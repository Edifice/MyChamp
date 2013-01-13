package UI.MenuStructure;

import BE.Group;
import BE.Match;
import BE.Team;
import BL.GroupManager;
import BL.MatchManager;
import BL.RankManager;
import BL.TeamManager;
import UI.Menu;
import UI.MenuItem;
import UI.Table_project;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Menu_View extends Menu {

    TeamManager tm = new TeamManager();
    GroupManager gm = new GroupManager();
    MatchManager mm = new MatchManager();
    RankManager rm = new RankManager();

    public Menu_View() throws Exception {
        super("View statistics");

        this.addItem(new MenuItem("All teams", "a", new Callable<Menu_View>() {
            @Override
            public Menu_View call() throws Exception {
                Table_project.fromTeamsWithGroups(tm.getAllWithGroupNames());
                return new Menu_View();
            }
        }));

        this.addItem(new MenuItem("Details about one team", "d", new Callable<Menu_View>() {
            @Override
            public Menu_View call() throws Exception {
                int teamID = Menu.getInputInt("Team ID");
                if (teamID < 1) {
                    Menu.Message("Undo");
                    return new Menu_View();
                }
                Team team;

                try {
                    team = tm.getById(teamID);
                } catch (SQLException e) {
                    Menu.Message("Wrong ID!");
                    //Menu.Catch(e);
                    return new Menu_View();
                }

                ArrayList<Match> data = mm.getMatchesByTeam(team);
                Table_project.fromMatches(data);

                return new Menu_View();
            }
        }));

        this.addItem(new MenuItem("Group table", "g", new Callable<Menu_View>() {
            @Override
            public Menu_View call() throws Exception {
                Table_project.GroupTable(tm.getAllByGroup(), gm.getGroupNames());
                return new Menu_View();
            }
        }));

        this.addItem(new MenuItem("Finals", "f", new Callable<Menu_View>() {
            @Override
            public Menu_View call() throws Exception {
                ArrayList<ArrayList<Team>> teams = new ArrayList<>();
                ArrayList<String> groupNames = new ArrayList<>();
                
                for (int i = 1; i <= 4; i++) {
                    Group group = gm.getGroupById(i);
                    ArrayList<Team> teamG = rm.constructFinalRankings(group);
                    teams.add(teamG);
                    groupNames.add(group.getGroupName());
                }
                
                Table_project.GroupTable(teams, groupNames);
                return new Menu_View();
            }
        }));

        this.start();
    }
}
