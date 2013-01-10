package UI.MenuStructure;

import BE.Match;
import BE.Team;
import BL.MatchManager;
import UI.Menu;
import UI.MenuItem;
import UI.Table;
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

        this.addItem(new MenuItem("List all", "l", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                ArrayList<Match> data = mm.getAll();
                String[][] tableData = new String[data.size()][7];


                int[] tableLayout = {4, 4, 15,15,10,10,10};
                String[] tableHeader = {"ID", "round", "Home Team", "Guest Team", "Played?", "Home Goals", "Guest Goals"};

                for (int i = 0; i < data.size(); i++) {
                    Match match = data.get(i);
                    tableData[i][0] = Integer.toString(match.getID());
                    tableData[i][1] = Integer.toString(match.getRound());
                    tableData[i][2] = match.getHomeTeamName();
                    tableData[i][3] = match.getGuestTeamName();
                    tableData[i][4] = match.getIsPlayed() == 1 ? "yes" : "no";
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
                int id = Menu.getInputInt("Match ID to update");
                if(id < 0){
                    return new Menu_Match();
                }
                Match match = null;
                try {
                    match = mm.getMatchById(id);
                } catch (Exception e) {
                    Menu.Message("Wrong ID!");
                    return new Menu_Match();
                }
                int home = Menu.getInputInt(match.getHomeTeamName()+ "'s goals");
                int guest = Menu.getInputInt(match.getGuestTeamName()+ "'s goals");
                
                try{
                    mm.updateScore(match, home, guest);
                    Menu.Message("Scores updated!");
                }
                catch(SQLException e){
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

    @Override
    protected void addItem(MenuItem item) {
        this.items.add(item);
    }
}
