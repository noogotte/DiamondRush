package fr.aumgn.diamondrush.game;

public class Statistics {

    private int kills;
    private int deaths;
    private int blocksBroken;

    public Statistics() {
        kills = 0;
        deaths = 0;
        blocksBroken = 0;
    }

    public void incrKills() {
        kills++;
    }

    public void incrDeaths() {
        deaths++;
    }

    public void incrBlocksBroken() {
        blocksBroken++;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBlocksBroken() {
        return blocksBroken;
    }

    public int getScore() {
        int score = 0;
        score += kills;
        score -= deaths;
        score += blocksBroken * 2;
        return score;
    }
}
