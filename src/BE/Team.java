package BE;

public class Team {
    protected int ID, groupID;
    protected String school, teamCaptain, email;

    public Team(int ID, String school, String teamCaptain, String email, int groupID) {
        this.ID = ID;        
        this.school = school;
        this.teamCaptain = teamCaptain;
        this.email = email;
        this.groupID = groupID;
    }

    public int getID() {
        return ID;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getSchool() {
        return school;
    }

    public String getTeamCaptain() {
        return teamCaptain;
    }

    public String getEmail() {
        return email;
    }
    
    

}
