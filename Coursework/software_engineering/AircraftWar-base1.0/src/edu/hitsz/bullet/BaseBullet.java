package edu.hitsz.bullet;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 子弹的抽象基类，继承自 AbstractFlyingObject
 * 定义了所有子弹的通用属性和行为：
 * 1. 子弹的威力值（对敌机造成的伤害）
 * 2. 自动检测飞行边界并销毁
 * 3. 根据速度方向判断出界条件
 * @author hitsz
 */
public abstract class BaseBullet extends AbstractFlyingObject {

    /** 
     * 子弹的威力值，决定击中目标时造成的伤害量
     * 例如：power=30 表示每次击中造成 30 点伤害
     */
    private int power = 0;

    /**
     * 构造函数：初始化子弹对象
     * @param locationX 初始 X 坐标（发射位置）
     * @param locationY 初始 Y 坐标（发射位置）
     * @param speedX X 方向速度（通常为 0，垂直飞行）
     * @param speedY Y 方向速度（正数向下，负数向上）
     * @param power 子弹威力值
     */
    public BaseBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    /**
     * 实现子弹的移动和边界检测逻辑
     * 调用父类的 forward() 方法更新位置
     * 检测子弹是否飞出游戏窗口边界，若出界则标记为无效并销毁
     */
    @Override
    public void forward() {
        super.forward();

        // 判定 X 轴出界：当子弹水平飞出左右边界时
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();  // 标记为无效，等待清理
        }

        // 判定 Y 轴出界：根据飞行方向分别判断
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT) {
            // 向下飞行的子弹（敌机子弹）：超出窗口底部时销毁
            vanish();
        } else if (locationY <= 0) {
            // 向上飞行的子弹（英雄机子弹）：超出窗口顶部时销毁
            vanish();
        }
    }

    /**
     * 获取子弹的威力值
     * @return 子弹威力，用于计算对敌机的伤害
     */
    public int getPower() {
        return power;
    }
}
