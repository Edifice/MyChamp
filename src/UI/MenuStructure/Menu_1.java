package UI.MenuStructure;

import UI.Menu;
import UI.MenuItem;
import java.util.concurrent.Callable;

public class Menu_1 extends Menu {

    public Menu_1() throws Exception {
        super("Manage");
        
        this.addItem(new MenuItem("Song", "s", new Callable<Menu_1>(){
            @Override
            public Menu_1 call() throws Exception{
                return new Menu_1(); // We should call the children of this menu. I just had to changes this because of an error...
            }
        }));
        
        this.addItem(new MenuItem("Playlist", "p", new Callable<Menu_1>(){
            @Override
            public Menu_1 call() throws Exception{
                return new Menu_1(); // We should call the children of this menu. I just had to changes this because of an error...
            }
        }));
        
        this.start();
    }
    
    @Override
    protected void addItem(MenuItem item) {
        this.items.add(item);
    }
}
