package UI.MenuStructure;

import BE.Match;
import BE.MenuItem;
import BL.MatchManager;
import BL.TeamManager;
import UI.Menu;
import UI.Table_Project;
import java.util.concurrent.Callable;

public class Menu_Match extends Menu {

    MatchManager mm = new MatchManager();
    TeamManager tm = new TeamManager();

    public Menu_Match() throws Exception {
        super("Match Management");

        if (mm.getAll().isEmpty()) {
            this.addItem(new MenuItem("Start new Tournament", "s", new Callable<Menu_Match>() {
                @Override
                public Menu_Match call() throws Exception {
                    if (tm.getAll().size() < 12) {
                        Menu.Message("There is not enough teams, to start a tournament!");
                        Menu.Message("You will need at least 12 teams!");
                        return new Menu_Match();
                    }
                    if (tm.getAll().size() > 16) {
                        Menu.Message("There is too much teams, to start a tournament!");
                        Menu.Message("You will need maximum 16 teams!");
                        return new Menu_Match();
                    }
                    if (Menu.getInputBoolean("Are you sure?")) {
                        mm.startTournament();
                        Menu.Message("Tournament started!");
                    }
                    return new Menu_Match();
                }
            }));
        }

//        if (mm.readyToFinals()) {
//            this.addItem(new MenuItem("Start Finals", "f", new Callable<Menu_Match>() {
//                @Override
//                public Menu_Match call() throws Exception {
//                    if (Menu.getInputBoolean("Are you sure?")) {
//                        mm.startFinals();
//                        Menu.Message("Finals started!");
//                    };
//                    return new Menu_Match();
//                }
//            }));
//        }
        if (!mm.getAll().isEmpty()) {
            this.addItem(new MenuItem("List all", "l", new Callable<Menu_Match>() {
                @Override
                public Menu_Match call() throws Exception {
                    Table_Project.fromMatches(mm.getAll());
                    return new Menu_Match();
                }
            }));


            if (!mm.isFinals()) {
                this.addItem(new MenuItem("Update score", "u", new Callable<Menu_Match>() {
                    @Override
                    public Menu_Match call() throws Exception {
                        int id = Menu.getInputInt("Match ID to update");
                        if (id < 0) {
                            return new Menu_Match();
                        }
                        Match match;
                        try {
                            match = mm.getMatchById(id);
                        } catch (Exception e) {
                            Menu.Message("Wrong ID!");
                            return new Menu_Match();
                        }
                        int home = Menu.getInputInt(match.getHomeTeamName() + "'s goals");
                        int guest = Menu.getInputInt(match.getGuestTeamName() + "'s goals");

                        if (home < 0 || guest < 0 || id < 0) {
                            return new Menu_Match();
                        }

                        try {
                            mm.updateScore(match, home, guest);
                            Menu.Message("Scores updated!");
                        } catch (Exception e) {
                            Menu.Message("Scores NOT updated!");
                            Menu.Message("SQL Error: " + e.getLocalizedMessage());
                        }

                        return new Menu_Match();
                    }
                }));
            } else {
                this.addItem(new MenuItem("Update score", "u", new Callable<Menu_Match>() {
                    @Override
                    public Menu_Match call() throws Exception {
                        Match match = mm.getNextFinalMatch();

                        if (match == null) {
                            Menu.Message("The finals are all ended, check the scores!");
                            return new Menu_Match();
                        }

                        int home = Menu.getInputInt(match.getHomeTeamName() + "'s goals");
                        int guest = Menu.getInputInt(match.getGuestTeamName() + "'s goals");

                        if (home < 0 || guest < 0) {
                            return new Menu_Match();
                        }

                        if (home == guest) {
                            Menu.Message("In the finals, there needs to be a winner!");
                            return new Menu_Match();
                        }

                        try {
                            mm.updateScore(match, home, guest);
                            Menu.Message("Scores updated!");
                        } catch (Exception e) {
                            Menu.Message("Scores NOT updated!");
                            Menu.Message("SQL Error: " + e.getLocalizedMessage());
                        }

                        return new Menu_Match();
                    }
                }));
            }

            this.addItem(new MenuItem("End Tournament", "e", new Callable<Menu_Match>() {
                @Override
                public Menu_Match call() throws Exception {
                    if (Menu.getInputBoolean("Are you sure? This will delete all of the match data!")) {
                        mm.endTournament(Menu.getInputBoolean("Do you want to delete all the teams?"));
                        Menu.Message("Tournament ended!");
                    }
                    return new Menu_Match();
                }
            }));

            this.addItem(new MenuItem("Random match results for tournament", "r", new Callable<Menu_Match>() {
                @Override
                public Menu_Match call() throws Exception {
                    if (Menu.getInputBoolean("Are you sure?")) {
                        mm.setRandom();
                    }
                    return new Menu_Match();
                }
            }));
        }
        this.start();
    }
}
