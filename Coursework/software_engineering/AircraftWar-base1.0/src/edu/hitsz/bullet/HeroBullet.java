package edu.hitsz.bullet;

/**
 * 英雄机子弹类，代表玩家战机发射的子弹
 * 继承自 BaseBullet，具有以下特性：
 * 1. 向上飞行（speedY 为负数）
 * 2. 击中敌机会造成伤害
 * 3. 出界检测逻辑由父类自动处理
 * @author hitsz
 */
public class HeroBullet extends BaseBullet {

    /**
     * 构造函数：初始化英雄机子弹
     * @param locationX 初始 X 坐标（发射位置，通常在英雄机上方）
     * @param locationY 初始 Y 坐标（发射位置）
     * @param speedX X 方向速度（通常为 0，垂直向上）
     * @param speedY Y 方向速度（负数，表示向上飞行）
     * @param power 子弹威力值，决定击中敌机时造成的伤害
     */
    public HeroBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

}
