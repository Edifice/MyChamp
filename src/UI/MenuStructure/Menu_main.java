package UI.MenuStructure;

import UI.Menu;
import UI.MenuItem;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Menu_main extends Menu{

    public Menu_main() throws Exception {
        super(create(), null);
    }

    private static ArrayList<MenuItem> create() {
        ArrayList<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Manage", "m", new Callable<Menu_1>(){
            @Override
            public Menu_1 call() throws Exception{
                return new Menu_1();
            }
        }));
        
        items.add(new MenuItem("Control", "c", new Callable<Menu_1>(){
            @Override
            public Menu_1 call() throws Exception{
                return new Menu_1(); // We should call the children of this menu. I just had to changes this because of an error...
            }
        }));
        
        return items;
    }
}
