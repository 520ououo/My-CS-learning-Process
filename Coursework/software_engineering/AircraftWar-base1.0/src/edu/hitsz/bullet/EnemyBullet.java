package edu.hitsz.bullet;

import edu.hitsz.item.ItemObserver;

public class EnemyBullet extends BaseBullet implements ItemObserver {

    private boolean isFrozen = false;
    private long freezeEndTime = 0;
    private int originalSpeedY;

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
        this.originalSpeedY = speedY;
    }

    @Override
    public int onBombEffect() {
        vanish();
        return 1;
    }

    @Override
    public int onFreezeEffect() {
        this.isFrozen = true;
        this.freezeEndTime = System.currentTimeMillis() + 5000;
        this.speedX = 0;
        this.speedY = 0;
        return 5000;
    }

    public void updateFreezeState() {
        if (isFrozen && System.currentTimeMillis() >= freezeEndTime) {
            isFrozen = false;
            this.speedY = originalSpeedY;
            this.speedX = 0;
        }
    }

    @Override
    public void forward() {
        if (isFrozen) {
            return;
        }
        super.forward();
    }

}
