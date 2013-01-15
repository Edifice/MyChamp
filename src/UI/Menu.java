package UI;

import BE.MenuItem;
import UI.MenuStructure.Menu_Main;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 *
 * This Class is the parent of all the Menu objects.
 *
 * @author Daniel
 */
public class Menu {

    protected ArrayList<MenuItem> items;
    private String parent;

    protected void addItem(MenuItem item) {
        this.items.add(item);
    }

    /**
     * constructor It gets the MenuItems, adds a back option to it and calls the
     * drawer and listener methods.
     *
     * @param items MenuItem-s to show
     * @param parent If it's a submenu, we want to write the parent
     * @throws Exception
     */
    public Menu(String parent) {
        ArrayList<MenuItem> list = new ArrayList<>();
        if (parent == null) {
            list.add(new MenuItem("Quit", "q", new Callable<Menu>() {
                @Override
                public Menu call() throws Exception {
                    return null;
                }
            }));

        } else {
            list.add(new MenuItem("<- Back", "b", new Callable<Menu>() {
                @Override
                public Menu call() throws Exception {
                    return new Menu_Main();
                }
            }));
        }
        //list.addAll(items);
        this.items = list;
        this.parent = parent;
    }

    public void start() throws Exception {
        draw();
        listen();
    }

    /**
     * Draws the header of the menu
     */
    private void draw() {
        int lineLength = 2;
        for (MenuItem i : items) {
            lineLength += lengthOfHeader(i);
        }

        if (lineLength > 20) {
            System.out.println(" │");
            System.out.println(" │");
            System.out.println(" ├───── ── ── ── ── ─ ─ ─ ─ - - - -");
            System.out.println(" ├─» " + (parent == null ? "Main Menu" : parent));
        }
        System.out.print(" ├┬");
        for (MenuItem i : items) {
            for (int j = 0; j < lengthOfHeader(i); j++) {
                System.out.print("─");
            }
            System.out.print("┬");
        }
        System.out.print("┐\n ││");
        for (MenuItem item : items) {
            System.out.print(formatMenuItem(item) + "│");
        }
        System.out.print("│\n ├┴");
        for (MenuItem i : items) {
            for (int j = 0; j < lengthOfHeader(i); j++) {
                System.out.print("─");
            }
            System.out.print("┴");
        }
        System.out.println("┘");
    }

    /**
     * Listen to user inputs and calls the submenu's call() method if the user
     * entered a good command.
     *
     * @throws Exception
     */
    private void listen() throws Exception {
        Scanner in = new Scanner(System.in);
        String userInput;
        boolean isCommandWrong = true;

        while (isCommandWrong) {
            //System.out.print(" │ " + (parent != null && !parent.isEmpty() ? parent : "Command") + " > ");
            System.out.print(" │ Command > ");
            userInput = in.nextLine();

            for (MenuItem i : items) {
                if (i.getTrigger().equalsIgnoreCase(userInput)) {
                    isCommandWrong = false;

                    i.getFunc().call();
                    break;
                }
            }
        }
    }

    /**
     * Returns the length of a formatted MenuItem.
     *
     * @param item item to format and get the length
     * @return
     */
    private int lengthOfHeader(MenuItem item) {
        String s = formatMenuItem(item);
        return s.length();
    }

    /**
     * Returns the formatted version of a MenuItem.
     *
     * @param item item to format
     * @return
     */
    private String formatMenuItem(MenuItem item) {
        return " " + item.getText() + " (" + item.getTrigger() + ") ";
    }

    /**
     * Waits for the user to input a String
     *
     * @param label Message to the user before the he/she inputs something.
     * @return Returns what the user wrote.
     */
    public static String getInput(String label) {
        Scanner in = new Scanner(System.in);
        String userInput;
        System.out.print(" │ " + label + " > ");
        try {
            userInput = in.nextLine();
        } catch (Error e) {
            throw new Error(e);
        }
        return userInput;
    }

    /**
     * Waits for the user to input an int
     *
     * @param label Message to the user before the he/she inputs something.
     * @return Returns what the user wrote.
     */
    public static int getInputInt(String label) {
        Scanner in = new Scanner(System.in);
        String userInput;
        int ret;
        System.out.print(" │ " + label + " (Undo: X < 0) > ");
        try {
            userInput = in.nextLine();
        } catch (Error e) {
            throw e;
        }

        if (userInput.equals("") || userInput.isEmpty()) {
            return -1;
        }

        try {
            ret = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            Message("You entered a wrong number!");
            ret = -1;
        }
        if (ret < 0) {
            return -1;
        }

        return ret;
    }

    /**
     * Waits for the user to input a boolean value (1/0, true/false, t/f,
     * yes/no, y/n)
     *
     * @param label Message to the user before the he/she inputs something.
     * @return Returns what the user wrote.
     */
    public static Boolean getInputBoolean(String label) {
        Scanner in = new Scanner(System.in);
        String userInput;
        System.out.print(" │ " + label + " [y/n] (back: empty string) > ");
        try {
            userInput = in.nextLine();
        } catch (Error e) {
            throw e;
        }

        if (userInput.equalsIgnoreCase("1")
                || userInput.equalsIgnoreCase("true")
                || userInput.equalsIgnoreCase("t")
                || userInput.equalsIgnoreCase("yes")
                || userInput.equalsIgnoreCase("y")) {
            return true;
        }

        return false;
    }

    /**
     * It writes out what it gets in the parameter.
     *
     * @param message message to write
     */
    public static void Message(String message) {
        System.out.println(" │ " + message + "...");
    }
}