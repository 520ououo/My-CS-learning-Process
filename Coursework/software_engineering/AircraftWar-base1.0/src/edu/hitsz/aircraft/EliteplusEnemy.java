package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;

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
public class EliteplusEnemy extends EnemyAircraft {

    private static final int DEFAULT_SCORE = 20;

    /**
     * 构造函数：初始化普通敌机
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度（通常为 0，直线下落）
     * @param speedY Y 方向速度（向下飞行，正值）
     * @param hp 初始生命值
     */
    public EliteplusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, DEFAULT_SCORE);
    }

    /**
     * 实现敌机的移动逻辑
     * 调用父类的 forward() 方法更新位置
     * 检测是否飞出屏幕底部边界，若出界则标记为无效并销毁
     */
    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界：当敌机到达或超过窗口底部时
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();  // 标记为无效，等待游戏循环清理
        }
    }

    /**
     * 实现射击方法
     * 由于普通敌机不具备射击能力，返回空列表
     * @return 空的子弹列表，表示无法射击
     */
    @Override
    public List<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    /**
     * 工厂方法：创建高级精英敌机实例
     * @param locationX X 坐标（未使用）
     * @param locationY Y 坐标（未使用）
     * @return 新创建的高级精英敌机对象
     */
    @Override
    public EnemyAircraft createInstance(int locationX, int locationY) {
        int width = ImageManager.ELITEPLUS_ENEMY_IMAGE.getWidth();
        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - width));
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        return new EliteplusEnemy(x, y, 0, 6, 80);
    }

}
