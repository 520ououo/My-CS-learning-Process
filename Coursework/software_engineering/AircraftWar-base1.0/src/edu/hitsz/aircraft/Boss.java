package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.CircleShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * BOSS敌机类
 * 继承自 EnemyAircraft，具有以下特性：
 * 1. 只能向下飞行，不能射击
 * 2. 被击毁后不会掉落道具
 * 3. 飞出屏幕底部时自动销毁
 * @author hitsz
 */
public class Boss extends EnemyAircraft {

    private static final int DEFAULT_SCORE = 150;

    public Boss(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, DEFAULT_SCORE);
        this.shootStrategy = new CircleShootStrategy();
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

    /**
     * 工厂方法：创建Boss敌机实例
     * @param locationX X 坐标（未使用）
     * @param locationY Y 坐标（未使用）
     * @return 新创建的Boss敌机对象
     */
    @Override
    public EnemyAircraft createInstance(int locationX, int locationY) {
        int width = ImageManager.BOSS_ENEMY_IMAGE.getWidth();
        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - width));
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        return new Boss(x, y, 0, 5, 200);
    }

}
