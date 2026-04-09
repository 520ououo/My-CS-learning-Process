package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

public class CircleShootStrategy implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft owner) {
        List<BaseBullet> bullets = new LinkedList<>();
        int x = owner.getLocationX();
        int y = owner.getLocationY();
        int speed = 8;
        int power = 15;

        if (owner instanceof HeroAircraft) {
            // 英雄机：向上方 180 度环射（避免打到自己）
            for (int i = 0; i < 20; i++) {
                double angle = Math.toRadians(180 + i * 9);
                int sx = (int) (speed * Math.cos(angle));
                int sy = (int) (speed * Math.sin(angle));
                bullets.add(new HeroBullet(x, y - 20, sx, sy - speed, power));
            }
        } else {
            // 敌机：360 度全向环射
            for (int i = 0; i < 20; i++) {
                double angle = Math.toRadians(i * 18);
                int sx = (int) (speed * Math.cos(angle));
                int sy = (int) (speed * Math.sin(angle));
                bullets.add(new EnemyBullet(x, y + owner.getHeight() / 2, sx, sy, power));
            }
        }
        return bullets;
    }
}

