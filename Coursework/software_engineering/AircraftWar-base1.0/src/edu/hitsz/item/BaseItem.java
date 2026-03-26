package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 所有道具的抽象基类，继承自 AbstractFlyingObject
 * 定义了游戏中道具的通用属性和行为：
 * 1. 具有位置坐标和移动速度（向下掉落）
 * 2. 被英雄机获得时产生特定效果
 * 3. 飞出屏幕底部时自动销毁
 * @author hitsz
 */
public abstract class BaseItem extends AbstractFlyingObject {

    /**
     * 构造函数：初始化道具的位置和状态
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向的速度（通常为 0，垂直下落）
     * @param speedY Y 方向的速度（向下掉落，正值）
     */
    public BaseItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * 道具生效方法（抽象方法）
     * 当英雄机获得该道具时调用，对英雄机产生特定效果
     * @param hero 获得道具的英雄机对象
     */
    public abstract void active(HeroAircraft hero);

    /**
     * 实现道具的移动逻辑
     * 调用父类的 forward() 方法更新位置
     * 检测是否飞出屏幕底部边界，若出界则标记为无效并销毁
     */
    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界：当道具到达或超过窗口底部时
        if (locationY >= edu.hitsz.application.Main.WINDOW_HEIGHT) {
            vanish();  // 标记为无效，等待游戏循环清理
        }
    }

}
