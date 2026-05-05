package edu.hitsz.strategy;

/**
 * 游戏难度配置类
 * 封装不同难度的游戏参数配置
 */
public class DifficultyConfig {

    private String difficulty;

    // 初始设定参数
    private int maxEnemyNumber;           // 屏幕中出现的敌机最大数量
    private double mobSpawnProbability;   // 普通敌机出现概率
    private double eliteSpawnProbability; // 精英敌机出现概率
    private double elitePlusSpawnProbability; // 高级精英敌机出现概率
    private double eliteProSpawnProbability;  // 超级精英敌机出现概率
    private int heroShootCycle;           // 英雄机的射击周期
    private int enemyShootCycle;          // 敌机的射击周期
    private double enemySpawnCycle;       // 敌机产生周期
    private int bossSpawnThreshold;       // Boss 敌机产生阈值

    // Boss相关设定
    private boolean allowBossSpawn;       // 是否允许生成Boss
    private boolean bossHpIncreases;      // Boss血量是否随召唤次数提升

    // 动态难度
    private boolean dynamicDifficulty;    // 是否随游戏时间提升难度
    private boolean enemySpawnCycleDecreases; // 敌机产生周期减少
    private boolean enemySpeedIncreases;      // 敌机速度增加
    private boolean enemyHpIncreases;         // 敌机血量增加
    private boolean heroShootCycleIncreases;  // 英雄机射击周期增加
    private boolean enemyShootCycleDecreases; // 敌机射击周期减少
    private int difficultyIncreaseInterval;   // 难度提升的时间间隔（帧数）

    public DifficultyConfig(String difficulty) {
        this.difficulty = difficulty;
        initializeConfig();
    }

    private void initializeConfig() {
        switch (difficulty) {
            case "简单":
                this.maxEnemyNumber = 3;
                this.mobSpawnProbability = 0.9;
                this.eliteSpawnProbability = 0.1;
                this.elitePlusSpawnProbability = 0.0;
                this.eliteProSpawnProbability = 0.0;
                this.heroShootCycle = 15;
                this.enemyShootCycle = 30;
                this.enemySpawnCycle = 25;
                this.bossSpawnThreshold = Integer.MAX_VALUE;
                this.allowBossSpawn = false;
                this.bossHpIncreases = false;
                this.dynamicDifficulty = false;
                break;

            case "普通":
                this.maxEnemyNumber = 5;
                this.mobSpawnProbability = 0.7;
                this.eliteSpawnProbability = 0.2;
                this.elitePlusSpawnProbability = 0.08;
                this.eliteProSpawnProbability = 0.02;
                this.heroShootCycle = 20;
                this.enemyShootCycle = 25;
                this.enemySpawnCycle = 20;
                this.bossSpawnThreshold = 500;
                this.allowBossSpawn = true;
                this.bossHpIncreases = false;
                this.dynamicDifficulty = true;
                this.enemySpawnCycleDecreases = true;
                this.enemySpeedIncreases = true;
                this.enemyHpIncreases = true;
                this.heroShootCycleIncreases = false;
                this.enemyShootCycleDecreases = false;
                this.difficultyIncreaseInterval = 500;
                break;

            case "困难":
                this.maxEnemyNumber = 7;
                this.mobSpawnProbability = 0.5;
                this.eliteSpawnProbability = 0.25;
                this.elitePlusSpawnProbability = 0.15;
                this.eliteProSpawnProbability = 0.1;
                this.heroShootCycle = 25;
                this.enemyShootCycle = 20;
                this.enemySpawnCycle = 15;
                this.bossSpawnThreshold = 400;
                this.allowBossSpawn = true;
                this.bossHpIncreases = true;
                this.dynamicDifficulty = true;
                this.enemySpawnCycleDecreases = true;
                this.enemySpeedIncreases = true;
                this.enemyHpIncreases = true;
                this.heroShootCycleIncreases = true;
                this.enemyShootCycleDecreases = true;
                this.difficultyIncreaseInterval = 375;
                break;

            default:
                throw new IllegalArgumentException("不支持的难度: " + difficulty);
        }
    }

    public String getDifficulty() { return difficulty; }
    public int getMaxEnemyNumber() { return maxEnemyNumber; }
    public double getMobSpawnProbability() { return mobSpawnProbability; }
    public double getEliteSpawnProbability() { return eliteSpawnProbability; }
    public double getElitePlusSpawnProbability() { return elitePlusSpawnProbability; }
    public double getEliteProSpawnProbability() { return eliteProSpawnProbability; }
    public int getHeroShootCycle() { return heroShootCycle; }
    public int getEnemyShootCycle() { return enemyShootCycle; }
    public double getEnemySpawnCycle() { return enemySpawnCycle; }
    public int getBossSpawnThreshold() { return bossSpawnThreshold; }
    public boolean isAllowBossSpawn() { return allowBossSpawn; }
    public boolean isBossHpIncreases() { return bossHpIncreases; }
    public boolean isDynamicDifficulty() { return dynamicDifficulty; }
    public boolean isEnemySpawnCycleDecreases() { return enemySpawnCycleDecreases; }
    public boolean isEnemySpeedIncreases() { return enemySpeedIncreases; }
    public boolean isEnemyHpIncreases() { return enemyHpIncreases; }
    public boolean isHeroShootCycleIncreases() { return heroShootCycleIncreases; }
    public boolean isEnemyShootCycleDecreases() { return enemyShootCycleDecreases; }
    public int getDifficultyIncreaseInterval() { return difficultyIncreaseInterval; }
}