package UI.MenuStructure;

import UI.Menu;
import UI.MenuItem;
import java.util.concurrent.Callable;

public class Menu_Match extends Menu {

    public Menu_Match() throws Exception {
        super("Manage");

        this.addItem(new MenuItem("Start Turnament", "s", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                if (Menu.getInputBoolean("Are you sure?")) {
                    //TODO: Start turnament
                    Menu.Message("Turnament started!");
                };
                return new Menu_Match();
            }
        }));

        this.addItem(new MenuItem("List all", "l", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                Menu_View.viewAllMatches();
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


        this.addItem(new MenuItem("End Turnament", "e", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                if (Menu.getInputBoolean("Are you sure? This will delete all of the match data!")) {
                    if (Menu.getInputBoolean("Are you really-really sure?")) {
                        //TODO: Stop turnament
                        Menu.Message("Turnament ended!");
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
