package UI.MenuStructure;

import BE.Match;
import BL.MatchManager;
import UI.Menu;
import UI.MenuItem;
import UI.Table_project;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Menu_Match extends Menu {

    MatchManager mm = new MatchManager();

    public Menu_Match() throws Exception {
        super("Manage");

        this.addItem(new MenuItem("Start Tournament", "s", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                if (Menu.getInputBoolean("Are you sure?")) {
                    mm.startTournament();
                    Menu.Message("Tournament started!");
                };
                return new Menu_Match();
            }
        }));
        
        this.addItem(new MenuItem("Start Finals", "f", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                if (Menu.getInputBoolean("Are you sure?")) {
                    mm.startFinals();
                    Menu.Message("Finals started!");
                };
                return new Menu_Match();
            }
        }));

        this.addItem(new MenuItem("List all", "l", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                ArrayList<Match> data = mm.getAll();
                Table_project.fromMatches(data);
                return new Menu_Match();
            }
        }));

        this.addItem(new MenuItem("Update score", "u", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                int id = Menu.getInputInt("Match ID to update");
                if (id < 0) {
                    return new Menu_Match();
                }
                Match match = null;
                try {
                    match = mm.getMatchById(id);
                } catch (Exception e) {
                    Menu.Message("Wrong ID!");
                    return new Menu_Match();
                }
                int home = Menu.getInputInt(match.getHomeTeamName() + "'s goals");
                int guest = Menu.getInputInt(match.getGuestTeamName() + "'s goals");
                
                if (id < 0) {
                    return new Menu_Match();
                }
                
                try {
                    mm.updateScore(match, home, guest);
                    Menu.Message("Scores updated!");
                } catch (SQLException e) {
                    Menu.Message("Scores NOT updated!");
                    Menu.Message("SQL Error: " + e.getLocalizedMessage());
                }

                return new Menu_Match();
            }
        }));


        this.addItem(new MenuItem("End Tournament", "e", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                if (Menu.getInputBoolean("Are you sure? This will delete all of the match data!")) {
                    if (Menu.getInputBoolean("Are you really-really sure?")) {
                        mm.endTournament();
                        Menu.Message("Tournament ended!");
                    }
                };
                return new Menu_Match();
            }
        }));

        this.start();
    }
}
