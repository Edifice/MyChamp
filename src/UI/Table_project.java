package UI;

import BE.Match;
import BE.Team;
import java.util.ArrayList;

public class Table_project extends Table {

    public static void fromTeams(ArrayList<Team> data) {
        String[][] tableData = new String[data.size()][4];

        int[] tableLayout = {2, 20, 20, 20};
        String[] tableHeader = {"ID", "School", "Team Captain", "Contact E-mail"};

        for (int i = 0; i < data.size(); i++) {
            Team team = data.get(i);
            tableData[i][0] = Integer.toString(team.getID());
            tableData[i][1] = team.getSchool();
            tableData[i][2] = team.getTeamCaptain();
            tableData[i][3] = team.getEmail();

        }
        Table.draw(tableHeader, tableLayout, tableData);
    }

    public static void fromTeamsWithGroups(ArrayList<Team> data) {
        String[][] tableData = new String[data.size()][6];

        int[] tableLayout = {2, 7, 20, 20, 20, 6};
        String[] tableHeader = {"ID", "Group", "School", "Team Captain", "Contact E-mail", "Points"};

        for (int i = 0; i < data.size(); i++) {
            Team team = data.get(i);
            tableData[i][0] = Integer.toString(team.getID());
            tableData[i][1] = team.getGroupName() == null ? "?" : team.getGroupName();
            tableData[i][2] = team.getSchool();
            tableData[i][3] = team.getTeamCaptain();
            tableData[i][4] = team.getEmail();
            tableData[i][5] = Integer.toString(team.getPoints());

        }
        Table.draw(tableHeader, tableLayout, tableData);
    }

    public static void fromMatches(ArrayList<Match> data) {
        String[][] tableData = new String[data.size()][7];

        int[] tableLayout = {4, 4, 20, 10, 10, 20, 6};
        String[] tableHeader = {"ID", "Round", "Home Team", "Home Goals", "Guest Goals", "Guest Team", "Played?"};

        for (int i = 0; i < data.size(); i++) {
            Match match = data.get(i);
            tableData[i][0] = Integer.toString(match.getID());
            tableData[i][1] = Integer.toString(match.getRound());
            tableData[i][2] = match.getHomeTeamName();
            tableData[i][3] = match.getIsPlayed() == 1 ? Integer.toString(match.getHomeGoals()) : "-";
            tableData[i][4] = match.getIsPlayed() == 1 ? Integer.toString(match.getGuestGoals()) : "-";
            tableData[i][5] = match.getGuestTeamName();
            tableData[i][6] = match.getIsPlayed() == 1 ? "yes" : "no";

        }
        Table.draw(tableHeader, tableLayout, tableData);
    }

    public static void GroupTable(ArrayList<ArrayList<Team>> data, ArrayList<String> groupNames) {
        ArrayList<Team> A = data.get(0);
        ArrayList<Team> B = data.get(1);
        ArrayList<Team> C = data.get(2);
        ArrayList<Team> D = data.get(3);

        int groupmax = Math.max(Math.max(A.size(), B.size()), Math.max(C.size(), D.size()));
        String[][] tableData = new String[groupmax][4];

        int[] tableLayout = {26, 26, 26, 26};
        String[] tableHeader = {groupNames.get(0), groupNames.get(1), groupNames.get(2), groupNames.get(3)};

        for (int i = 0; i < A.size(); i++) {
            Team team = A.get(i);
            tableData[i][0] = team.getSchool() + '|' + formatToLength(Integer.toString(team.getPoints()), 2);
        }
        if (groupmax > A.size()) {
            tableData[groupmax - 1][0] = "-";
        }


        for (int i = 0; i < B.size(); i++) {
            Team team = B.get(i);
            tableData[i][1] = team.getSchool() + '|' + formatToLength(Integer.toString(team.getPoints()), 2);
        }
        if (groupmax > B.size()) {
            tableData[groupmax - 1][1] = "-";
        }


        for (int i = 0; i < C.size(); i++) {
            Team team = C.get(i);
            tableData[i][2] = team.getSchool() + '|' + formatToLength(Integer.toString(team.getPoints()), 2);
        }
        if (groupmax > C.size()) {
            tableData[groupmax - 1][2] = "-";
        }


        for (int i = 0; i < D.size(); i++) {
            Team team = D.get(i);
            tableData[i][3] = team.getSchool() + '|' + formatToLength(Integer.toString(team.getPoints()), 2);
        }
        if (groupmax > D.size()) {
            tableData[groupmax - 1][3] = "-";
        }
        Table.draw(tableHeader, tableLayout, tableData);
    }
}
