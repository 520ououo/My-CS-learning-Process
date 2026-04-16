package edu.hitsz.dao;

import java.io.Serializable;

/**
 * 分数记录实体类
 * 存储单次游戏的得分、时间和玩家信息
 */
public class ScoreRecord implements Comparable<ScoreRecord>, Serializable {

    private static final long serialVersionUID = 1L;

    // ... existing code ...

    private String playerName;
    private int score;
    private long time;  // 游戏用时（毫秒）
    private String difficulty;  // 难度等级

    public ScoreRecord() {
    }

    public ScoreRecord(String playerName, int score, long time, String difficulty) {
        this.playerName = playerName;
        this.score = score;
        this.time = time;
        this.difficulty = difficulty;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * 格式化时间显示（毫秒转秒）
     */
    public String getFormattedTime() {
        return String.format("%.1f秒", time / 1000.0);
    }

    /**
     * 按分数降序排序
     */
    @Override
    public int compareTo(ScoreRecord other) {
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-10s | %6d | %s",
                playerName, difficulty, score, getFormattedTime());
    }
}
