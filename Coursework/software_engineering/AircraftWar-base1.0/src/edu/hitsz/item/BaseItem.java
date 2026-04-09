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

    /** 道具类型常量 */
    public static final int TYPE_BLOOD = 0;         // 加血道具
    public static final int TYPE_BULLET = 1;        // 火力道具
    public static final int TYPE_SUPER_BULLET = 2;  // 超级火力道具
    public static final int TYPE_BOMB = 3;          // 炸弹道具
    public static final int TYPE_FREEZE = 4;        // 冰冻道具

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

    /**
     * 简单工厂方法：根据类型创建道具
     * @param type 道具类型
     * @param locationX X 坐标
     * @param locationY Y 坐标
     * @return 创建的道具对象，若类型无效则返回 null
     */
    public static BaseItem createItem(int type, int locationX, int locationY) {
        switch (type) {
            case TYPE_BLOOD:
                return new BloodItem(locationX, locationY, 0, 5, 30);
            case TYPE_BULLET:
                return new BulletItem(locationX, locationY, 0, 5, 1);
            case TYPE_SUPER_BULLET:
                return new SuperBulletItem(locationX, locationY, 0, 5, 2, 20);
            case TYPE_BOMB:
                return new BombItem(locationX, locationY, 0, 5, 100);
            case TYPE_FREEZE:
                return new FreezeItem(locationX, locationY, 0, 5, 5000);
            default:
                return null;
        }
    }

    /**
     * 简单工厂方法：随机创建道具
     * 等概率生成五种类型的道具
     * @param locationX X 坐标
     * @param locationY Y 坐标
     * @return 随机创建的道具对象
     */
    public static BaseItem createRandomItem(int locationX, int locationY) {
        int type = (int) (Math.random() * 5);
        return createItem(type, locationX, locationY);
    }

    /**
     * 简单工厂方法：创建精英敌机掉落道具
     * 等概率生成三种类型的道具：加血、火力、超级火力
     * @param locationX X 坐标
     * @param locationY Y 坐标
     * @return 随机创建的道具对象
     */
    public static BaseItem createEliteDropItem(int locationX, int locationY) {
        int type = (int) (Math.random() * 3);
        return createItem(type, locationX, locationY);
    }

}
