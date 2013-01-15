package BE;

public class Team {

    private int ID, groupID, points, goalDeficit, totalGoals;
    private String school, teamCaptain, email, groupName;

    public Team() {
    }

    public Team(int ID, String school, String teamCaptain, String email, int groupID) {
        this.groupID = groupID;
        this.school = school;
        this.teamCaptain = teamCaptain;
        this.email = email;
        this.ID = ID;
    }

    public Team(int ID, String school, String teamCaptain, String email, int groupID, String groupName) {
        this.ID = ID;
        this.school = school;
        this.teamCaptain = teamCaptain;
        this.email = email;
        this.groupID = groupID;
        this.groupName = groupName;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @return the groupID
     */
    public int getGroupID() {
        return groupID;
    }

    /**
     * @param groupID the groupID to set
     */
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @return the school
     */
    public String getSchool() {
        return school;
    }

    /**
     * @param school the school to set
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * @return the teamCaptain
     */
    public String getTeamCaptain() {
        return teamCaptain;
    }

    /**
     * @param teamCaptain the teamCaptain to set
     */
    public void setTeamCaptain(String teamCaptain) {
        this.teamCaptain = teamCaptain;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Prints out the entity in a nicer way.
     *
     * @return a string, in the format: ID. Name of the school | Team captain:
     * Name of the team captain.
     */
    @Override
    public String toString() {
        return getID() + ". " + getSchool() + " | Team captain: " + getTeamCaptain();
    }

    /**
     * @return the goalDeficit
     */
    public int getGoalDeficit() {
        return goalDeficit;
    }

    /**
     * @param goalDeficit the goalDeficit to set
     */
    public void setGoalDeficit(int goalDeficit) {
        this.goalDeficit = goalDeficit;
    }

    /**
     * @return the totalGoals
     */
    public int getTotalGoals() {
        return totalGoals;
    }

    /**
     * @param totalGoals the totalGoals to set
     */
    public void setTotalGoals(int totalGoals) {
        this.totalGoals = totalGoals;
    }
}
