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

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setTeamCaptain(String teamCaptain) {
        this.teamCaptain = teamCaptain;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    

}
