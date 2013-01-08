package UI.MenuStructure;

import UI.Menu;
import UI.MenuItem;
import java.util.concurrent.Callable;

//FUCK VERSION CONTROLL BUGS!

public class Menu_Main extends Menu {

    public Menu_Main() throws Exception {
        super(null);

        this.addItem(new MenuItem("Team management", "t", new Callable<Menu_Team>() {
            @Override
            public Menu_Team call() throws Exception {
                return new Menu_Team();
            }
        }));

        this.addItem(new MenuItem("Match management", "m", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
                return new Menu_Match();
            }
        }));

        this.addItem(new MenuItem("View results", "v", new Callable<Menu_Match>() {
            @Override
            public Menu_Match call() throws Exception {
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
