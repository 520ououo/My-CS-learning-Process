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
        // 初始化英雄机，放置在窗口底部中间位置
        heroAircraft = HeroAircraft.getInstance();

        // 初始化各种游戏对象列表
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        items = new LinkedList<>();

        // 启动英雄机的鼠标控制器，用于响应玩家操作
        new HeroController(this, heroAircraft);

        // 创建定时器，用于驱动游戏循环
        this.timer = new Timer("game-action-timer", true);

        // 初始化 DAO
        this.scoreDAO = new ScoreDAOImpl();

        // 记录游戏开始时间
        this.gameStartTime = System.currentTimeMillis();
    }

    /**
     * 带难度参数的构造函数
     * @param difficulty 游戏难度
     */
    public Game(String difficulty) {
        this();
        this.difficulty = difficulty;
        adjustDifficulty(difficulty);
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

        // 创建定时任务，包含完整的游戏循环逻辑
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // 阶段切换逻辑（每 10 秒切换一次）
                phaseCounter++;
                if (phaseCounter >= phaseCycle) {
                    phaseCounter = 0;
                    currentSpawnPhase = (currentSpawnPhase + 1) % 3;
                }

                // Boss 生成逻辑：当玩家分数达到阈值时生成
                if (!bossSpawned && score >= bossSpawnScore) {
                    bossSpawned = true;
                    enemyAircrafts.add(bossPrototype.createInstance(0, 0));
                    System.out.println("🔥 Boss 出现！");
                }

                // 敌机生成逻辑（使用工厂方法模式）
                enemySpawnCounter++;
                if (enemySpawnCounter >= enemySpawnCycle) {
                    enemySpawnCounter = 0;
                    
                    // 产生敌机
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        // 根据当前阶段决定生成哪种特殊敌机
                        EnemyAircraft specialEnemy = eliteEnemyPrototype;
                        if (currentSpawnPhase == 1) {
                            specialEnemy = elitePlusEnemyPrototype;
                        } else if (currentSpawnPhase == 2) {
                            specialEnemy = eliteProEnemyPrototype;
                        }
                        
                        // 7:3 概率生成
                        if (Math.random() < 0.7) {
                            // 70% 生成普通敌机
                            enemyAircrafts.add(mobEnemyPrototype.createInstance(0, 0));
                        } else {
                            // 30% 生成当前阶段的特殊敌机
                            enemyAircrafts.add(specialEnemy.createInstance(0, 0));
                        }
                    }
                }

                // 飞机发射子弹
                shootAction();
                // 子弹移动
                bulletsMoveAction();
                // 飞机移动
                aircraftsMoveAction();
                // 道具移动
                itemsMoveAction();
                // 撞击检测
                crashCheckAction();
                // 后处理：清理无效对象
                postProcessAction();
                // 重绘界面
                repaint();
                // 游戏结束检查
                checkResultAction();
            }
        };
        // 以固定延迟时间进行执行：本次任务执行完成后，延迟 timeInterval 再执行下一次
        timer.schedule(task, 0, timeInterval);
    }

    //***********************
    //      Action 各部分 - 游戏逻辑更新
    //***********************

    /**
     * 射击动作：控制英雄机和敌机发射子弹
     */
    private void shootAction() {
        shootCounter++;
        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            // 英雄机射击
            heroBullets.addAll(heroAircraft.shoot());
            // 敌机射击
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }
    }

    /**
     * 子弹移动：更新所有子弹的位置
     */
    private void bulletsMoveAction() {
        // 更新英雄机子弹位置
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        // 更新敌机子弹位置
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    /**
     * 飞机移动：更新所有敌机的位置
     */
    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    /**
     * 道具移动：更新所有道具的位置
     */
    private void itemsMoveAction() {
        for (BaseItem item : items) {
            item.forward();
        }
    }

    /**
     * 碰撞检测：处理所有碰撞事件
     * 包括：
     * 1. 敌机子弹攻击英雄机
     * 2. 英雄机子弹攻击敌机
     * 3. 英雄机与敌机相撞
     * 4. 英雄机获得补给道具
     */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机被英雄机子弹击中
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();  // 子弹消失
                    if (enemyAircraft.notValid()) {
                        // 敌机被击毁，执行奖励逻辑
                        score +=  ((EnemyAircraft) enemyAircraft).getScore();
                        
                        // Boss 被击毁时，随机掉落 3 个道具
                        if (enemyAircraft instanceof Boss) {
                            int itemY = enemyAircraft.getLocationY();
                            for (int i = 0; i < 3; i++) {
                                spawnRandomItem(enemyAircraft.getLocationX(), itemY);
                                // 每个道具 Y 坐标向下偏移 40 像素，实现竖直紧密排列
                                itemY += 40;
                            }
                            System.out.println(" Boss 被击毁！掉落 3 个道具");
                            bossSpawned = false;
                            // 提高下一次生成的分数阈值，避免立即重新生成
                            bossSpawnScore += 700;
                        }
                        // 精英敌机被击毁时，以一定概率生成道具
                        else if (enemyAircraft instanceof EliteEnemy) {
                            if (Math.random() < 0.5) {
                                spawnItem(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                            }
                        }
                        // 精锐敌机被击毁时，掉落 4 种道具（不含冰冻）
                        else if (enemyAircraft instanceof EliteplusEnemy) {
                            if (Math.random() < 0.5) {
                                spawnElitePlusItem(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                            }
                        }
                        // 王牌敌机被击毁时，掉落全部 5 种道具
                        else if (enemyAircraft instanceof EliteproEnemy) {
                            if (Math.random() < 0.5) {
                                spawnEliteProItem(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                            }
                        }
                    }
                }
                // 英雄机与敌机相撞，双方均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(50);  // 英雄机立即死亡
                }
            }
        }

        // 英雄机获得道具，道具生效
        for (BaseItem item : items) {
            if (item.notValid()) {
                continue;
            }
            if (heroAircraft.crash(item)) {
                item.active(heroAircraft);
                item.vanish();
            }
        }

    }

    /**
     * 后处理：清理游戏中无效的对象
     * 包括：
     * 1. 删除飞出屏幕或已失效的子弹
     * 2. 删除被击毁的敌机
     * 3. 删除已使用或失效的道具
     */
    private void postProcessAction() {
        // 移除无效的敌方子弹
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        // 移除无效的英雄子弹
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        // 移除无效的敌机
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        // 删除无效道具
        items.removeIf(AbstractFlyingObject::notValid);
    }

    /**
     * 生成道具（使用简单工厂模式）
     * 精英敌机被击毁时调用，仅生成加血、火力、超级火力三种道具
     * @param x 道具生成的 X 坐标
     * @param y 道具生成的 Y 坐标
     */
    private void spawnItem(int x, int y) {
        BaseItem item = BaseItem.createEliteDropItem(x, y);
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * 生成精锐敌机掉落道具（4种：加血、火力、超级火力、炸弹）
     * @param x 道具生成的 X 坐标
     * @param y 道具生成的 Y 坐标
     */
    private void spawnElitePlusItem(int x, int y) {
        BaseItem item = BaseItem.createElitePlusDropItem(x, y);
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * 生成王牌敌机掉落道具（5种：加血、火力、超级火力、炸弹、冰冻）
     * @param x 道具生成的 X 坐标
     * @param y 道具生成的 Y 坐标
     */
    private void spawnEliteProItem(int x, int y) {
        BaseItem item = BaseItem.createEliteProDropItem(x, y);
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * 生成随机道具（5种类型无限制）
     * @param x 道具生成的 X 坐标
     * @param y 道具生成的 Y 坐标
     */
    private void spawnRandomItem(int x, int y) {
        BaseItem item = BaseItem.createRandomItem(x, y);
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * 检查游戏是否结束
     * 若英雄机生命值归零，则游戏结束，关闭定时器
     */
    private void checkResultAction() {
        // 游戏结束检查：判断英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            timer.cancel();  // 取消定时器并终止所有调度任务
            gameOverFlag = true;
            System.out.println("Game Over!");
            
            // 记录游戏分数
            saveGameScore();
        }
    }

    /**
     * 保存游戏分数到排行榜
     */
    private void saveGameScore() {
        // 计算游戏用时
        long gameDuration = System.currentTimeMillis() - gameStartTime;
        
        // 创建分数记录
        ScoreRecord record = new ScoreRecord(
            "玩家",
            score,
            gameDuration,
            difficulty
        );
        
        // 保存记录
        scoreDAO.saveScore(record);
        
        // 显示当前难度的排行榜
        displayLeaderboard();
    }

    /**
     * 在控制台打印排行榜
     */
    private void displayLeaderboard() {
        System.out.println("\n========== 排行榜 ==========");
        System.out.println("难度: " + difficulty);
        System.out.println("========================================");
        System.out.println("排名    | 玩家名      | 难度        | 分数    | 用时");
        System.out.println("========================================");
        
        List<ScoreRecord> records = scoreDAO.getScoresByDifficulty(difficulty);
        
        // 按分数降序排序
        Collections.sort(records);
        
        if (records.isEmpty()) {
            System.out.println("暂无记录");
        } else {
            int rank = 1;
            for (ScoreRecord record : records) {
                System.out.printf("%-6d | %s%n", rank, record.toString());
                rank++;
            }
        }
        
        System.out.println("========================================\n");
    }

    //***********************
    //      Paint 各部分 - 画面绘制
    //***********************
    
    /**
     * 重写 paint 方法，实现游戏画面的绘制
     * 通过定时调用 repaint() 方法，实现游戏动画效果
     * @param g 图形上下文对象
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景，实现图片滚动效果
        // 绘制两张背景图，形成无缝滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;  // 背景向下滚动
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;  // 重置背景位置，实现循环
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层，避免遮挡
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        // 绘制道具
        paintImageWithPositionRevised(g, items);

        // 绘制英雄机
        g.drawImage(ImageManager.HERO_IMAGE, 
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, 
                null);

        // 绘制得分和生命值信息
        paintScoreAndLife(g);
    }

    /**
     * 根据飞行对象的位置绘制其图像
     * 自动将图像中心对准对象的坐标位置
     * @param g 图形上下文
     * @param objects 要绘制的飞行对象列表
     */
    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            // 移除 assert，改为安全判空
            if (image != null) {
                g.drawImage(image, 
                        object.getLocationX() - image.getWidth() / 2,
                        object.getLocationY() - image.getHeight() / 2, 
                        null);
            }
        }
    }

    /**
     * 在屏幕左上角绘制玩家得分和英雄机生命值
     * @param g 图形上下文
     */
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
