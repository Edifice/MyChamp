package BE;

/**
 * This is just a dummy class which we are using for storing Team entities and a
 * number by which they are sorted using the Comparable interface.
 *
 * @see http://docs.oracle.com/javase/7/docs/api/java/lang/Comparable.html
 */
public class Dummy implements Comparable<Dummy> {

    private Team team;
    private int number;

    public Dummy() {
    }

    public Dummy(Team team, int totalGoals) {
        this.team = team;
        this.number = totalGoals;
    }

    public Team getTeam() {
        return this.team;
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
}
