package BE;

public class Group {

    private int ID;
    private String groupName;

    public Group(int ID, String groupName) {
        this.ID = ID;
        this.groupName = groupName;
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
     * @return a string, in the format: ID - Group Name
     */
    @Override
    public String toString() {
        return getID() + " - " + getGroupName();
    }
}
