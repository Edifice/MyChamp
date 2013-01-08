package BE;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
public class Match {

    public int ID;
    public int round;
    public int homeTeamID;
    public int guestTeamID;
    public int isPlayed;
    public int homeGoals;
    public int guestGoals;
    public String homeTeamName;
    public String guestTeamName;
    // public Date date = new Date();

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
        //  this.date.setTime(date);
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
        //  this.date.setTime(date);
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
     * @return the isPlayed
     */
    public boolean isIsPlayed() {
        return isPlayed == 0 ? false : true;
    }

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

//    /**
//     * @return the date
//     */
//    public Date getDate() {
//        return date;
//    }
//
//    /**
//     * Formats the Date into a format like it's stored in the DB.
//     *
//     * @param date the date that will be formatted
//     * @return reportDate, the formatted date, which can be printed out.
//     */
//    public String formatDateToString(Date date) {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String reportDate = df.format(date);
//        return reportDate;
//    }
    /**
     * Prints out the entity in a nicer way.
     *
     * @return a string, in the format: ID. HomeTeamID - GuestTeamID | Score:
     * HomeGoals - GuestGoals | Date
     */
    @Override
    public String toString() {
        return getID() + ". " + getHomeTeamID() + " - " + getGuestTeamID() + " | Score: " + getHomeGoals() + " - " + getGuestGoals() + " | ";
    }
}
