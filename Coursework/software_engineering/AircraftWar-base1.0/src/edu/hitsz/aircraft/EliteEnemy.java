package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.SingleShootStrategy;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机类，代表游戏中最基础的敌方单位
 * 继承自 EnemyAircraft，具有以下特性：
 * 1. 只能向下飞行，不能射击
 * 2. 被击毁后不会掉落道具
 * 3. 飞出屏幕底部时自动销毁
 * @author hitsz
 */
public class EliteEnemy extends EnemyAircraft {

    private static final int DEFAULT_SCORE = 15;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, DEFAULT_SCORE);
        this.shootStrategy = new SingleShootStrategy();
    }

    /**
     * 实现敌机的移动逻辑
     * 调用父类的 forward() 方法更新位置
     * 检测是否飞出屏幕底部边界，若出界则标记为无效并销毁
     */
    @Override
    public void forward() {
        super.forward();
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    @Override
    public EnemyAircraft createInstance(int locationX, int locationY) {
        int width = ImageManager.ELITE_ENEMY_IMAGE.getWidth();
        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - width));
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        return new EliteEnemy(x, y, 0, 8, 60);
    }

}
