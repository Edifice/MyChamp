package UI;

import java.util.concurrent.Callable;

/**
 * Sub Class to handle Menu's Items
 * @author Dani
 */
public class MenuItem {

    private String text;
    private String trigger;
    private Callable func;

    public MenuItem(String text, String trigger, Callable func) {
        this.text = text;
        this.trigger = trigger;
        this.func = func;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the trigger
     */
    public String getTrigger() {
        return trigger;
    }

    /**
     * @param trigger the trigger to set
     */
    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    /**
     * @return the func
     */
    public Callable getFunc() {
        return func;
    }

    /**
     * @param func the func to set
     */
    public void setFunc(Callable func) {
        this.func = func;
    }
}
