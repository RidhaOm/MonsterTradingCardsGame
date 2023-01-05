package model;

public class ScoreboardEntry {
    private String username;
    private int ELO_value;

    public ScoreboardEntry(String username, int ELO_value) {
        this.username = username;
        this.ELO_value = ELO_value;
    }

    public String getUsername() {
        return username;
    }

    public int getELO_value() {
        return ELO_value;
    }
}

