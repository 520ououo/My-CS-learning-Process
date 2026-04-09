package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

public class SingleShootStrategy implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft owner) {
        List<BaseBullet> bullets = new LinkedList<>();
        int x = owner.getLocationX();
        int y = owner.getLocationY();

        if (owner instanceof HeroAircraft) {
            // 英雄机：向上发射
            bullets.add(new HeroBullet(x, y - 20, 0, -10, 30));
        } else {
            // 敌机：向下发射
            bullets.add(new EnemyBullet(x, y + owner.getHeight() / 2, 0, 15, 15));
        }
        return bullets;
    }
}

