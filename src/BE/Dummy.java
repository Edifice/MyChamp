package BE;

public class Dummy implements Comparable<Dummy> {

    private Team team;
    private int totalGoals, rank;
    
    public Dummy() {
        
    }
    
    public Dummy(Team team, int totalGoals) {
        this.team = team;
        this.totalGoals = totalGoals;
    }
    
    public Dummy(Team team, int totalGoals, int rank) {
        this.team = team;
        this.totalGoals = totalGoals;
        this.rank = rank;
    }

    @Override
    public int compareTo(Dummy t) {
        if (this.totalGoals < t.totalGoals) {
            t.team.ranking = this.team.ranking;
            return +1;
        } else if (this.totalGoals > t.totalGoals) {
            this.team.ranking = t.team.ranking;
            return -1;
        } else {
            return 0;
        }
    }
    
    public Team getTeam() {
        return this.team;
    }
}
