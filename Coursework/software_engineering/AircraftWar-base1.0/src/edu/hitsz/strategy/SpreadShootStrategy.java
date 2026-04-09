package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

public class SpreadShootStrategy implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft owner) {
        List<BaseBullet> bullets = new LinkedList<>();
        int x = owner.getLocationX();
        int y = owner.getLocationY();

        if (owner instanceof HeroAircraft) {
            // 英雄机：向上散射
            bullets.add(new HeroBullet(x - 20, y - 20, -3, -10, 20));
            bullets.add(new HeroBullet(x, y - 20, 0, -10, 20));
            bullets.add(new HeroBullet(x + 20, y - 20, 3, -10, 20));
        } else {
            // 敌机：向下散射
            bullets.add(new EnemyBullet(x - 20, y + owner.getHeight() / 2, -3, 10, 12));
            bullets.add(new EnemyBullet(x, y + owner.getHeight() / 2, 0, 10, 12));
            bullets.add(new EnemyBullet(x + 20, y + owner.getHeight() / 2, 3, 10, 12));
        }
        return bullets;
    }
}

