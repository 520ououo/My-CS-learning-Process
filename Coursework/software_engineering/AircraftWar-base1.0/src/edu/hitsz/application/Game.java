package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.*;
import edu.hitsz.basic.AbstractFlyingObject;

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

    //屏幕中出现的敌机最大数量
    private final int enemyMaxNumber = 5;

    /** 敌机生成周期（帧数），每隔这么多帧生成一架敌机 */
    protected double enemySpawnCycle = 20;
    /** 敌机生成计数器 */
    private int enemySpawnCounter = 0;

    /** 射击周期（帧数），控制英雄机和敌机的射击频率 */
    protected double shootCycle = 20;
    /** 射击计数器 */
    private int shootCounter = 0;

    /** 当前玩家得分 */
    private int score = 0;

    /** 游戏结束标志，true 表示游戏已结束 */
    private boolean gameOverFlag = false;

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

        // 启动英雄机的鼠标控制器，用于响应玩家操作
        new HeroController(this, heroAircraft);

        // 创建定时器，用于驱动游戏循环
        this.timer = new Timer("game-action-timer", true);
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

                // 敌机生成逻辑
                enemySpawnCounter++;
                if (enemySpawnCounter >= enemySpawnCycle) {
                    enemySpawnCounter = 0;
                    // 产生敌机
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        double spawnType = Math.random();
                        if (spawnType < 0.2) {
                            // 20% 概率生成精英敌机
                            enemyAircrafts.add(new EliteEnemy(
                                    (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                                    (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                                    0,
                                    8,
                                    60
                            ));
                        } else {
                            // 80% 概率生成普通敌机
                            enemyAircrafts.add(new MobEnemy(
                                    (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                                    (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                                    0,
                                    10,
                                    30
                            ));
                        }
                    }
                }

                // 飞机发射子弹
                shootAction();
                // 子弹移动
                bulletsMoveAction();
                // 飞机移动
                aircraftsMoveAction();
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
                        // TODO 获得分数，产生道具补给
                        score +=  ((EnemyAircraft) enemyAircraft).getScore();
                    }
                }
                // 英雄机与敌机相撞，双方均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);  // 英雄机立即死亡
                }
            }
        }

        // Todo: 我方获得道具，道具生效

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
        // Todo: 删除无效道具
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
        }
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

        // Todo: 绘制道具

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
            assert image != null : objects.getClass().getName() + " has no image! ";
            // 将图像中心对准对象坐标进行绘制
            g.drawImage(image, 
                    object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, 
                    null);
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
