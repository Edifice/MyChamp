package UI.MenuStructure;

import BE.Match;
import BE.Team;
import BL.MatchManager;
import UI.Menu;
import UI.MenuItem;
import UI.Table;
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

        this.addItem(new MenuItem("List all", "l", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                ArrayList<Match> data = mm.getAll();
                String[][] tableData = new String[data.size()][7];


                int[] tableLayout = {4, 4, 10,10,10,10,10};
                String[] tableHeader = {"ID", "round", "homeTeamID", "guestTeamID", "isPlayed", "homeGoals", "guestGoals"};

                for (int i = 0; i < data.size(); i++) {
                    Match match = data.get(i);
                    tableData[i][0] = Integer.toString(match.getID());
                    tableData[i][1] = Integer.toString(match.getRound());
                    tableData[i][2] = Integer.toString(match.getHomeTeamID());
                    tableData[i][3] = Integer.toString(match.getGuestTeamID());
                    tableData[i][4] = Integer.toString(match.getIsPlayed());
                    tableData[i][5] = Integer.toString(match.getHomeGoals());
                    tableData[i][6] = Integer.toString(match.getGuestGoals());

                }
                Table.draw(tableHeader, tableLayout, tableData);
                return new Menu_Match();
            }
        }));

        this.addItem(new MenuItem("Update score", "u", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                Menu.Message("update...");
                return new Menu_Match();
            }
        }));


        this.addItem(new MenuItem("End Tournament", "e", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                if (Menu.getInputBoolean("Are you sure? This will delete all of the match data!")) {
                    if (Menu.getInputBoolean("Are you really-really sure?")) {
                        //TODO: Stop tournament
                        Menu.Message("Tournament ended!");
                    }
                };
                return new Menu_Match();
            }
        }));

        this.start();
    }

    @Override
    protected void addItem(MenuItem item) {
        this.items.add(item);
    }
}
