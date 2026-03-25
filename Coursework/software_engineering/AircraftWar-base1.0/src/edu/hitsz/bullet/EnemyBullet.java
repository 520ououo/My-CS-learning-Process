package edu.hitsz.bullet;

/**
 * 敌机子弹类，代表敌机发射的子弹
 * 继承自 BaseBullet，具有以下特性：
 * 1. 向下飞行（speedY 为正数）
 * 2. 击中英雄机会造成伤害
 * 3. 出界检测逻辑由父类自动处理
 * @author hitsz
 */
public class EnemyBullet extends BaseBullet {

    /**
     * 构造函数：初始化敌机子弹
     * @param locationX 初始 X 坐标（发射位置）
     * @param locationY 初始 Y 坐标（发射位置，通常在敌机下方）
     * @param speedX X 方向速度（通常为 0，垂直下落）
     * @param speedY Y 方向速度（正数，表示向下飞行）
     * @param power 子弹威力值，决定击中英雄机时造成的伤害
     */
    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

}
