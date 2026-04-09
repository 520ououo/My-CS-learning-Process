package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

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

    /**
     * 构造函数：初始化精锐敌机
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度（左右移动）
     * @param speedY Y 方向速度（向下飞行）
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
            vanish();
        }
    }

    /**
     * 实现射击方法：精锐敌机向下直射双排子弹
     * @return 包含两发子弹的列表
     */
    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        
        int x = this.getLocationX();
        int y = this.getLocationY() + this.getHeight() / 2;
        
        int speedX = 0;
        int speedY = 10;
        int power = 10;
        
        // 双排子弹：左右各偏移 15 像素
        BaseBullet bullet1 = new EnemyBullet(x - 15, y, speedX, speedY, power);
        BaseBullet bullet2 = new EnemyBullet(x + 15, y, speedX, speedY, power);
        
        res.add(bullet1);
        res.add(bullet2);
        
        return res;
    }

    /**
     * 工厂方法：创建精锐敌机实例
     * @param locationX X 坐标（未使用）
     * @param locationY Y 坐标（未使用）
     * @return 新创建的精锐敌机对象
     */
    @Override
    public EnemyAircraft createInstance(int locationX, int locationY) {
        int width = ImageManager.ELITEPLUS_ENEMY_IMAGE.getWidth();
        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - width));
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        // 随机初始水平移动方向：50% 概率向左（-6），50% 概率向右（6）
        int speedX = Math.random() < 0.5 ? -6 : 6;
        return new EliteplusEnemy(x, y, speedX, 5, 80);
    }

}
