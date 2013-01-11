package BE;

public class Dummy implements Comparable<Dummy> {

    private Team team;
    private int number;
    
    public Dummy() {
        
    }
    
    public Dummy(Team team, int totalGoals) {
        this.team = team;
        this.number = totalGoals;
    }
    
    

    @Override
    public int compareTo(Dummy t) {
        if (this.number < t.number) {
            
            return +1;
        } else if (this.number > t.number) {
            
            return -1;
        } else {
            return 0;
        }
    }
    
    public Team getTeam() {
        return this.team;
    }
}
