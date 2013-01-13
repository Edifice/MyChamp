package BE;

public class Match {

    private int ID, round, homeTeamID, guestTeamID, isPlayed, homeGoals, guestGoals;
    private String homeTeamName, guestTeamName;

    public Match() {
    }

    public Match(int round, int homeTeamID, int guestTeamID) {
        this.round = round;
        this.homeTeamID = homeTeamID;
        this.guestTeamID = guestTeamID;
        this.isPlayed = 0;
    }

    public Match(int ID, int round, int homeTeamID, int guestTeamID, int isPlayed, int homeGoals, int guestGoals) {
        this.ID = ID;
        this.round = round;
        this.homeTeamID = homeTeamID;
        this.guestTeamID = guestTeamID;
        this.isPlayed = isPlayed;
        this.homeGoals = homeGoals;
        this.guestGoals = guestGoals;
    }

    public Match(int ID, int round, int homeTeamID, int guestTeamID, int isPlayed, int homeGoals, int guestGoals, String homeTeamName, String guestTeamName) {
        this.ID = ID;
        this.round = round;
        this.homeTeamID = homeTeamID;
        this.guestTeamID = guestTeamID;
        this.isPlayed = isPlayed;
        this.homeGoals = homeGoals;
        this.guestGoals = guestGoals;
        this.homeTeamName = homeTeamName;
        this.guestTeamName = guestTeamName;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the round
     */
    public int getRound() {
        return round;
    }

    /**
     * @param round the round to set
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * @return the homeTeamID
     */
    public int getHomeTeamID() {
        return homeTeamID;
    }

    /**
     * @param homeTeamID the homeTeamID to set
     */
    public void setHomeTeamID(int homeTeamID) {
        this.homeTeamID = homeTeamID;
    }

    /**
     * @return the guestTeamID
     */
    public int getGuestTeamID() {
        return guestTeamID;
    }

    /**
     * @param guestTeamID the guestTeamID to set
     */
    public void setGuestTeamID(int guestTeamID) {
        this.guestTeamID = guestTeamID;
    }

    /**
     * @return the isPlayed in the form of a boolean
     */
    public boolean isIsPlayed() {
        return isPlayed == 0 ? false : true;
    }

    /**
     * @return the isPlayed in the form of an integer (0 = false, 1 = true)
     */
    public int getIsPlayed() {
        return isPlayed;
    }

    /**
     * @param isPlayed the isPlayed to set
     */
    public void setIsPlayed(int isPlayed) {
        this.isPlayed = isPlayed;
    }

    /**
     * @return the homeGoals
     */
    public int getHomeGoals() {
        return homeGoals;
    }

    /**
     * @param homeGoals the homeGoals to set
     */
    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    /**
     * @return the guestGoals
     */
    public int getGuestGoals() {
        return guestGoals;
    }

    /**
     * @param guestGoals the guestGoals to set
     */
    public void setGuestGoals(int guestGoals) {
        this.guestGoals = guestGoals;
    }

    /**
     * @return the homeTeamName
     */
    public String getHomeTeamName() {
        return homeTeamName;
    }

    /**
     * @return the guestTeamName
     */
    public String getGuestTeamName() {
        return guestTeamName;
    }

    /**
     * @param homeTeamName sets the home team's name
     */
    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    /**
     * @param guestTeamName sets the away team's name
     */
    public void setGuestTeamName(String guestTeamName) {
        this.guestTeamName = guestTeamName;
    }

    /**
     * Prints out the entity in a nicer way.
     *
     * @return a string, in the format: ID. HomeTeamID - GuestTeamID | Score:
     * HomeGoals - GuestGoals | Date
     */
    @Override
    public String toString() {
        return getID() + ". " + getHomeTeamID() + " - " + getGuestTeamID() + " "
                + "| Score: " + getHomeGoals() + " - " + getGuestGoals() + " | ";
    }
}
