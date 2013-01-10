package BE;

public class Dummy implements Comparable<Dummy> {

    private Team team;
    private int totalGoals;
    
    public Dummy() {
        
    }
    
    public Dummy(Team team, int totalGoals) {
        this.team = team;
        this.totalGoals = totalGoals;
    }

    @Override
    public int compareTo(Dummy t) {
        if (this.totalGoals < t.totalGoals){
            return +1;
        } else if (this.totalGoals > t.totalGoals) {
            return -1;
        } else {
            return 0;
        }
    }
    
    public Team getTeam() {
        return this.team;
    }
}
