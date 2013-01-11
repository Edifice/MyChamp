package UI.MenuStructure;

import BE.Match;
import BE.Team;
import BL.GroupManager;
import BL.MatchManager;
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

    public Menu_View() throws Exception {
        super("Manage");

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

        this.addItem(new MenuItem("Group schedule", "g", new Callable<Menu_View>() {
            @Override
            public Menu_View call() throws Exception {
                Table_project.GroupTable(tm.getAllByGroup(), gm.getGroupNames());
                return new Menu_View();
            }
        }));

        this.addItem(new MenuItem("Team schedule", "t", new Callable<Menu_View>() {
            @Override
            public Menu_View call() throws Exception {
                Menu.Message("Team schedule");
                return new Menu_View();
            }
        }));

        this.addItem(new MenuItem("Final schedule", "f", new Callable<Menu_View>() {
            @Override
            public Menu_View call() throws Exception {
                Menu.Message("Final schedule");
                return new Menu_View();
            }
        }));

        this.start();
    }
}
