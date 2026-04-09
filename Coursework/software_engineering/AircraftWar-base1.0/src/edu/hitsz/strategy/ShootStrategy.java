package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import java.util.List;

/**
 * 射击策略接口
 * 定义不同机型的弹道发射算法族
 */
public interface ShootStrategy {
    /**
     * 执行射击逻辑
     * @param owner 射击者（英雄机或敌机）
     * @return 生成的子弹列表
     */
    List<BaseBullet> shoot(AbstractAircraft owner);
}
