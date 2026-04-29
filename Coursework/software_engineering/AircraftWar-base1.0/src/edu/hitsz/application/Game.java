package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.dao.ScoreDAO;
import edu.hitsz.dao.ScoreDAOImpl;
import edu.hitsz.dao.ScoreRecord;
import edu.hitsz.item.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * 游戏主面板类，继承自 JPanel
 * 负责管理整个游戏的核心逻辑，包括：
 * 1. 游戏对象的创建和维护（英雄机、敌机、子弹等）
 * 2. 游戏循环的定时调度
 * 3. 碰撞检测和游戏状态更新
 * 4. 游戏画面的绘制和渲染
 * @author hitsz
 */
public class Game extends JPanel {

    /** 背景图片顶部位置，用于实现背景滚动效果 */
    private int backGroundTop = 0;

    //调度器, 用于定时任务调度
    private final Timer timer;
    //时间间隔(ms)，控制刷新频率
    private final int timeInterval = 40;

    /** 玩家控制的英雄机对象 */
    private final HeroAircraft heroAircraft;
    /** 敌机列表 */
    private final List<AbstractAircraft> enemyAircrafts;
    /** 英雄机发射的子弹列表 */
    private final List<BaseBullet> heroBullets;
    /** 敌机发射的子弹列表 */
    private final List<BaseBullet> enemyBullets;
    /** 道具列表 */
    private final List<BaseItem> items;

    //屏幕中出现的敌机最大数量
    private final int enemyMaxNumber = 5;

    /** 敌机生成周期（帧数），约 0.8 秒生成一架 */
    protected double enemySpawnCycle = 20;
    /** 敌机生成计数器 */
    private int enemySpawnCounter = 0;

    /** 阶段切换周期（帧数），10 秒 = 250 帧 */
    private final int phaseCycle = 250;
    /** 阶段切换计数器 */
    private int phaseCounter = 0;
    
    /** 当前阶段类型（0=精英，1=精锐，2=王牌） */
    private int currentSpawnPhase = 0;

    /** Boss 生成阈值分数 */
    private int bossSpawnScore = 500;
    /** 标记 Boss 是否已生成 */
    private boolean bossSpawned = false;

    /** 射击周期（帧数），控制英雄机和敌机的射击频率 */
    protected double shootCycle = 20;
    /** 射击计数器 */
    private int shootCounter = 0;

    /** 当前玩家得分 */
    private int score = 0;

    /** 游戏结束标志，true 表示游戏已结束 */
    private boolean gameOverFlag = false;

    /** 游戏开始时间（毫秒） */
    private long gameStartTime = 0;

    /** 当前游戏难度 */
    private String difficulty = "普通";

    /** DAO 对象 */
    private ScoreDAO scoreDAO;

    /** 普通敌机原型对象（用于工厂方法模式） */
    private final EnemyAircraft mobEnemyPrototype = new MobEnemy(0, 0, 0, 10, 30);
    /** 精英敌机原型对象（用于工厂方法模式） */
    private final EnemyAircraft eliteEnemyPrototype = new EliteEnemy(0, 0, 0, 8, 60);
    /** 高级精英敌机原型对象（用于工厂方法模式） */
    private final EnemyAircraft elitePlusEnemyPrototype = new EliteplusEnemy(0, 0, 0, 6, 80);
    /** 超级精英敌机原型对象（用于工厂方法模式） */
    private final EnemyAircraft eliteProEnemyPrototype = new EliteproEnemy(0, 0, 0, 7, 100);
    /** Boss 敌机原型对象 */
    private final EnemyAircraft bossPrototype = new Boss(0, 0, 0, 0, 300);

    /**
     * 构造函数：初始化游戏对象和基本设置
     */
    public Game() {
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        items = new LinkedList<>();

        new HeroController(this, heroAircraft);

        this.timer = new Timer("game-action-timer", true);

        this.scoreDAO = new ScoreDAOImpl();

        this.gameStartTime = System.currentTimeMillis();
    }

    public Game(String difficulty) {
        this();
        this.difficulty = difficulty;
        ImageManager.setBackgroundImage(difficulty);
        adjustDifficulty(difficulty);
        
        SoundManager.playBGM();
    }

    /**
     * 根据难度调整游戏参数
     * @param difficulty 难度等级
     */
    private void adjustDifficulty(String difficulty) {
        switch (difficulty) {
            case "简单":
                bossSpawnScore = 600;
                break;
            case "普通":
                bossSpawnScore = 500;
                break;
            case "困难":
                bossSpawnScore = 400;
                break;
            case "地狱":
                bossSpawnScore = 300;
                break;
        }
    }

    /**
     * 游戏启动入口方法
     * 启动定时任务，开始执行游戏主循环
     * 每个时间间隔执行一次完整的游戏逻辑更新和画面绘制
     */
    public void action() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                phaseCounter++;
                if (phaseCounter >= phaseCycle) {
                    phaseCounter = 0;
                    currentSpawnPhase = (currentSpawnPhase + 1) % 3;
                }

                if (!bossSpawned && score >= bossSpawnScore) {
                    bossSpawned = true;
                    enemyAircrafts.add(bossPrototype.createInstance(0, 0));
                    System.out.println("🔥 Boss 出现！");
                    SoundManager.playBossBGM();
                }

                heroAircraft.checkPowerUpExpiration();

                enemySpawnCounter++;
                if (enemySpawnCounter >= enemySpawnCycle) {
                    enemySpawnCounter = 0;
                    
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        EnemyAircraft specialEnemy = eliteEnemyPrototype;
                        if (currentSpawnPhase == 1) {
                            specialEnemy = elitePlusEnemyPrototype;
                        } else if (currentSpawnPhase == 2) {
                            specialEnemy = eliteProEnemyPrototype;
                        }
                        
                        if (Math.random() < 0.7) {
                            enemyAircrafts.add(mobEnemyPrototype.createInstance(0, 0));
                        } else {
                            enemyAircrafts.add(specialEnemy.createInstance(0, 0));
                        }
                    }
                }

                shootAction();
                bulletsMoveAction();
                aircraftsMoveAction();
                itemsMoveAction();
                crashCheckAction();
                postProcessAction();
                repaint();
                checkResultAction();
            }
        };
        timer.schedule(task, 0, timeInterval);
    }

    //***********************
    //      Action 各部分 - 游戏逻辑更新
    //***********************

    private void shootAction() {
        shootCounter++;
        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            heroBullets.addAll(heroAircraft.shoot());
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void itemsMoveAction() {
        for (BaseItem item : items) {
            item.forward();
        }
    }

    private void crashCheckAction() {
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
                SoundManager.playBulletHit();
            }
        }

        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    SoundManager.playBulletHit();
                    if (enemyAircraft.notValid()) {
                        score +=  ((EnemyAircraft) enemyAircraft).getScore();
                        
                        if (enemyAircraft instanceof Boss) {
                            int itemY = enemyAircraft.getLocationY();
                            for (int i = 0; i < 3; i++) {
                                spawnRandomItem(enemyAircraft.getLocationX(), itemY);
                                itemY += 40;
                            }
                            System.out.println("🔥 Boss 被击毁！掉落 3 个道具");
                            SoundManager.playBombExplosion();
                            bossSpawned = false;
                            bossSpawnScore += 700;
                            SoundManager.stopBossBGM();
                            SoundManager.playBGM();
                        }
                        else if (enemyAircraft instanceof EliteEnemy) {
                            if (Math.random() < 0.7) {
                                spawnItem(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                            }
                        }
                        else if (enemyAircraft instanceof EliteplusEnemy) {
                            if (Math.random() < 0.7) {
                                spawnElitePlusItem(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                            }
                        }
                        else if (enemyAircraft instanceof EliteproEnemy) {
                            if (Math.random() < 0.7) {
                                spawnEliteProItem(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                            }
                        }
                    }
                }
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(50);
                    SoundManager.playBombExplosion();
                }
            }
        }

        for (BaseItem item : items) {
            if (item.notValid()) {
                continue;
            }
            if (heroAircraft.crash(item)) {
                if (item instanceof BombItem) {
                    BombItem bomb = (BombItem) item;
                    int totalScore = 0;
                    
                    for (AbstractAircraft enemy : enemyAircrafts) {
                        if (!enemy.notValid() && !(enemy instanceof Boss)) {
                            bomb.addObserver((ItemObserver) enemy);
                        }
                    }
                    
                    for (BaseBullet bullet : enemyBullets) {
                        if (!bullet.notValid()) {
                            bomb.addObserver((ItemObserver) bullet);
                        }
                    }
                    
                    bomb.active(heroAircraft);
                    
                    for (AbstractAircraft enemy : enemyAircrafts) {
                        if (enemy.notValid() && enemy instanceof EnemyAircraft) {
                            totalScore += ((EnemyAircraft) enemy).getScore();
                        }
                    }
                    
                    score += totalScore;
                    if (totalScore > 0) {
                        System.out.println("炸弹摧毁敌机，获得 " + totalScore + " 分");
                    }
                } else if (item instanceof FreezeItem) {
                    FreezeItem freeze = (FreezeItem) item;
                    
                    for (AbstractAircraft enemy : enemyAircrafts) {
                        if (!enemy.notValid() && !(enemy instanceof Boss)) {
                            freeze.addObserver((ItemObserver) enemy);
                        }
                    }
                    
                    for (BaseBullet bullet : enemyBullets) {
                        if (!bullet.notValid()) {
                            freeze.addObserver((ItemObserver) bullet);
                        }
                    }
                    
                    freeze.active(heroAircraft);
                } else {
                    item.active(heroAircraft);
                }
                item.vanish();
                SoundManager.playGetSupply();
            }
        }

    }

    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        items.removeIf(AbstractFlyingObject::notValid);
        
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy instanceof EnemyAircraft) {
                ((EnemyAircraft) enemy).updateFreezeState();
            }
        }
        for (BaseBullet bullet : enemyBullets) {
            if (bullet instanceof EnemyBullet) {
                ((EnemyBullet) bullet).updateFreezeState();
            }
        }
    }

    private void spawnItem(int x, int y) {
        BaseItem item = BaseItem.createEliteDropItem(x, y);
        if (item != null) {
            items.add(item);
        }
    }

    private void spawnElitePlusItem(int x, int y) {
        BaseItem item = BaseItem.createElitePlusDropItem(x, y);
        if (item != null) {
            items.add(item);
        }
    }

    private void spawnEliteProItem(int x, int y) {
        BaseItem item = BaseItem.createEliteProDropItem(x, y);
        if (item != null) {
            items.add(item);
        }
    }

    private void spawnRandomItem(int x, int y) {
        BaseItem item = BaseItem.createRandomItem(x, y);
        if (item != null) {
            items.add(item);
        }
    }

    private void checkResultAction() {
        if (heroAircraft.getHp() <= 0) {
            timer.cancel();
            gameOverFlag = true;
            System.out.println("Game Over!");
            
            SoundManager.playGameOver();
            
            showLeaderboardWindow();
        }
    }

    private void showLeaderboardWindow() {
        long gameDuration = System.currentTimeMillis() - gameStartTime;
        
        SwingUtilities.invokeLater(() -> {
            LeaderboardWindow leaderboard = new LeaderboardWindow(
                "玩家",
                score,
                gameDuration,
                difficulty
            );
            leaderboard.setVisible(true);
        });
    }

    //***********************
    //      Paint 各部分 - 画面绘制
    //***********************
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;  
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;  
        }

        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        paintImageWithPositionRevised(g, items);

        g.drawImage(ImageManager.HERO_IMAGE, 
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, 
                null);

        paintScoreAndLife(g);
    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            if (image != null) {
                g.drawImage(image, 
                        object.getLocationX() - image.getWidth() / 2,
                        object.getLocationY() - image.getHeight() / 2, 
                        null);
            }
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }

}
