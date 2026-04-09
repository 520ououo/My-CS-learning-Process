package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.DoubleShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 精锐敌机类
 * 继承自 EnemyAircraft，具有以下特性：
 * 1. 向下飞行，且触碰左右边界时自动反弹
 * 2. 按周期向下直射双排子弹
 * 3. 被击毁时按概率掉落道具（4种，不含冰冻）
 * @author hitsz
 */
public class EliteplusEnemy extends EnemyAircraft {

    private static final int DEFAULT_SCORE = 20;

    public EliteplusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, DEFAULT_SCORE);
        this.shootStrategy = new DoubleShootStrategy();
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
        int width = ImageManager.ELITEPLUS_ENEMY_IMAGE.getWidth();
        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - width));
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        int speedX = Math.random() < 0.5 ? -6 : 6;
        return new EliteplusEnemy(x, y, speedX, 5, 80);
    }

}
