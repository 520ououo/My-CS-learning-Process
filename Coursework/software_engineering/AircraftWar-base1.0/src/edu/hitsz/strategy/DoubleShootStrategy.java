package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import java.util.LinkedList;
import java.util.List;

public class DoubleShootStrategy implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft owner) {
        List<BaseBullet> bullets = new LinkedList<>();
        int x = owner.getLocationX();
        int y = owner.getLocationY() + owner.getHeight() / 2;
        int speedY = 10;
        int power = 15;

        bullets.add(new EnemyBullet(x - 15, y, 0, speedY, power));
        bullets.add(new EnemyBullet(x + 15, y, 0, speedY, power));
        return bullets;
    }
}

