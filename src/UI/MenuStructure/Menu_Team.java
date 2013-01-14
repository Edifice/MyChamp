package UI.MenuStructure;

import BE.Team;
import BL.MatchManager;
import BL.TeamManager;
import UI.Menu;
import UI.MenuItem;
import UI.Table_project;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Menu_Team extends Menu {

    TeamManager tm = new TeamManager();
    MatchManager mm = new MatchManager();
    private final int MAX_TEAM_CAPACITY = 16;

    public Menu_Team() throws Exception {
        super("Team Management");

        this.addItem(new MenuItem("List all", "l", new Callable<Menu_Team>() {
            @Override
            public Menu_Team call() throws Exception {
                Table_project.fromTeams(tm.getAll());
                return new Menu_Team();
            }
        }));
        if (mm.getAll().isEmpty()) {
            this.addItem(new MenuItem("Add team", "a", new Callable<Menu_Team>() {
                @Override
                public Menu_Team call() throws Exception {
                    ArrayList<Team> teams = tm.getAll();
                    if (teams.size() < MAX_TEAM_CAPACITY) {
                        Team add = new Team();
                        add.setSchool(Menu.getInput("School name"));
                        add.setTeamCaptain(Menu.getInput("Team Captain's name"));
                        add.setEmail(Menu.getInput("Contact E-mail"));
                        tm.addTeam(add);
                        Menu.Message("Team added");
                    } else {
                        Menu.Message("The maximum of team capacity is reached");
                    }
                    return new Menu_Team();
                }
            }));
        }
        this.addItem(new MenuItem("Update team", "u", new Callable<Menu_Team>() {
            @Override
            public Menu_Team call() throws Exception {
                Team update;
                try {
                    int in = Menu.getInputInt("Team ID to update");
                    if (in <= 0) {
                        return new Menu_Team();
                    }
                    update = tm.getById(in);
                } catch (Exception e) {
                    Menu.Message("Wrong ID!");
                    return new Menu_Team();
                }

                if (update == null) {
                    Menu.Message("Wrong ID!");
                    return new Menu_Team();
                }

                Menu.Message("What data would you like to change?");
                Menu.Message("1: School name");
                Menu.Message("2: Team Captain");
                Menu.Message("3: E-mail address");
                int submenu;
                do {
                    submenu = Menu.getInputInt("Please choose from above");
                } while (submenu > 3);

                if (submenu < 1) {
                    Menu.Message("Undo");
                    return new Menu_Team();
                }

                String newValue = Menu.getInput("New value");
                switch (submenu) {
                    case 1:
                        update.setSchool(newValue);
                        break;
                    case 2:
                        update.setTeamCaptain(newValue);
                        break;
                    case 3:
                        update.setEmail(newValue);
                        break;
                }
                tm.updateTeam(update);
                return new Menu_Team();
            }
        }));
        if (mm.getAll().isEmpty()) {
            this.addItem(new MenuItem("Remove team", "r", new Callable<Menu_Team>() {
                @Override
                public Menu_Team call() throws Exception {
                    int remove = Menu.getInputInt("Team ID to remove");
                    if (remove < 1) {
                        Menu.Message("Undo");
                        return new Menu_Team();
                    }
                    try {
                        tm.getById(remove);
                    } catch (Exception e) {
                        Menu.Message("Wrong ID!");
                        return new Menu_Team();
                    }
                    if (remove > 0) {
                        tm.removeTeam(remove);
                        Menu.Message("Team removed from the database!");
                    }

                    return new Menu_Team();
                }
            }));

            this.addItem(new MenuItem("Remove all teams", "x", new Callable<Menu_Team>() {
                @Override
                public Menu_Team call() throws Exception {
                    if (Menu.getInputBoolean("Do you want to delete all the teams?")) {
                        tm.removeAll();
                        Menu.Message("All the teams are removed!");
                        return new Menu_Team();
                    }
                    return new Menu_Team();
                }
            }));

            this.addItem(new MenuItem("Add defaut teams", "d", new Callable<Menu_Team>() {
                @Override
                public Menu_Team call() throws Exception {
                    ArrayList<Team> teams = tm.getAll();
                    if (teams.isEmpty()) {
                        tm.generateDefaultTeams();
                        Menu.Message("16 teams added");
                    } else {
                        Menu.Message("Remove all teams before using this feature");
                    }
                    return new Menu_Team();
                }
            }));
        }
        this.start();
    }
}
