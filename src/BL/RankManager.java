package BL;

import BE.Group;
import BE.Team;
import DAL.TeamDBManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class RankManager {

    private TeamDBManager TM;
    
    public RankManager() {
        
    }
    
    public ArrayList<Team> getRankingByPoints(Group group) throws SQLException {
        return TM.getTeamByPoints(group);
    }
}
