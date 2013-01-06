package UI;

import BE.Team;
import DAL.TeamDBManager;
import UI.MenuStructure.Menu_main;

public class MyChamp {
    public static void main(String[] args) throws Exception {
        System.out.println(" ┌──── ── ── ─ ─ - -");
        System.out.println(" │ Welcome to MyChamp");
        System.out.println(" ├──── ── ── ─ ─ - -");
        Menu_main menu_main = new Menu_main();
        System.out.println(" ├──── ── ── ─ ─ - -");
        System.out.println(" │");
        System.out.println(" │ See you later!");
        System.out.println(" └── ── ─ -");
        
        
//        TeamDBManager tDBM = new TeamDBManager();
//        Team myTeam = new Team(0, "Vestervang", "Martin", "martinrasmussen92@gmail.com", 0);
//        tDBM.addTeam(myTeam);
//        tDBM.removeTeam(1);
//        myTeam.setGroupID(1);
//        tDBM.assignToGroup(myTeam);
    }
    
}
