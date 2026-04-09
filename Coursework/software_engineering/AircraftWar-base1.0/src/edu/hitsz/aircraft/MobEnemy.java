package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.NoShootStrategy;

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
public class MobEnemy extends EnemyAircraft {

    private static final int DEFAULT_SCORE = 10;

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, DEFAULT_SCORE);
        this.shootStrategy = new NoShootStrategy();
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
        int width = ImageManager.MOB_ENEMY_IMAGE.getWidth();
        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - width));
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        return new MobEnemy(x, y, 0, 10, 30);
    }

}
