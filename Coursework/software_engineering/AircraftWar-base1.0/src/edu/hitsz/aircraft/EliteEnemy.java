package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
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

    /**
     * 构造函数：初始化精英敌机
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度（通常为 0，直线下落）
     * @param speedY Y 方向速度（向下飞行，正值）
     * @param hp 初始生命值
     */
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
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
     * 实现射击方法，精英敌机向下发射子弹
     * @return 包含发射子弹的列表
     */
    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        
        int x = this.getLocationX();
        int y = this.getLocationY() + this.getHeight() / 2;
        
        int speedX = 0;
        int speedY = 15;
        int power = 10;
        
        BaseBullet bullet = new EnemyBullet(x, y, speedX, speedY, power);
        res.add(bullet);
        
        return res;
    }

}
