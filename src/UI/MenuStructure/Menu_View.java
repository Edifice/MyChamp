package UI.MenuStructure;

import BE.Team;
import BL.GroupManager;
import BL.MatchManager;
import BL.TeamManager;
import UI.Menu;
import UI.MenuItem;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Menu_View extends Menu {
    
    TeamManager tm = new TeamManager();
    GroupManager gm = new GroupManager();
    MatchManager mm = new MatchManager();

    public Menu_View() throws Exception {
        super("Manage");
        
        this.addItem(new MenuItem("All teams", "a", new Callable<Menu_View>(){
            @Override
            public Menu_View call() throws Exception{
                Menu.Message("all team data");
                return new Menu_View();
            }
        }));
        
        this.addItem(new MenuItem("Details about one team", "d", new Callable<Menu_View>(){
            @Override
            public Menu_View call() throws Exception{
                Menu.Message("1 team");
                return new Menu_View();
            }
        }));
        
        this.addItem(new MenuItem("Group schedule", "g", new Callable<Menu_View>(){
            @Override
            public Menu_View call() throws Exception{
                Menu.Message("Group schedule");
                return new Menu_View();
            }
        }));
        
        this.addItem(new MenuItem("Team schedule", "t", new Callable<Menu_View>(){
            @Override
            public Menu_View call() throws Exception{
                Menu.Message("Team schedule");
                return new Menu_View();
            }
        }));
        
        this.addItem(new MenuItem("Final schedule", "f", new Callable<Menu_View>(){
            @Override
            public Menu_View call() throws Exception{
                Menu.Message("Final schedule");
                return new Menu_View();
            }
        }));
        
        this.start();
    }
    
    @Override
    protected void addItem(MenuItem item) {
        this.items.add(item);
    }
    
    protected static void viewAllMatches(){
        Menu.Message("All matches");
    }
}
