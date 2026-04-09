package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.CircleShootStrategy;

/**
 * BOSS敌机类
 * 特性：
 * 1. 悬浮于界面上方，仅执行左右移动操作，不向下移动
 * 2. 采用环射弹道，单次同时发射 20 颗子弹
 * 3. 被击毁时，随机掉落 3 个道具（道具类型无限制）
 */
public class Boss extends EnemyAircraft {

    private static final int DEFAULT_SCORE = 150;

    public Boss(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, DEFAULT_SCORE);
        this.shootStrategy = new CircleShootStrategy();
    }

    /**
     * 实现敌机的移动逻辑
     * Boss 仅执行左右移动，悬浮于界面上方，不向下移动
     */
    @Override
    public void forward() {
        locationX += speedX;
        // 触碰左右边界时反向飞行
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            speedX = -speedX;
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
        // Boss 悬浮于界面上方，Y 坐标固定
        int y = 100;
        // 随机初始水平移动方向
        int speedX = Math.random() < 0.5 ? -3 : 3;
        return new Boss(x, y, speedX, 0, 300);
    }

}
