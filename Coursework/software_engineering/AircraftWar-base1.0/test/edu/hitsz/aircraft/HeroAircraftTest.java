package edu.hitsz.aircraft;

import static org.junit.jupiter.api.Assertions.*;
import edu.hitsz.bullet.BaseBullet;
import java.util.List;

class HeroAircraftTest {

    private HeroAircraft hero;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        hero = HeroAircraft.getInstance();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        hero.sethp(hero.getmaxHp());
        hero.setShootStrategy(new edu.hitsz.strategy.SingleShootStrategy());
    }

    @org.junit.jupiter.api.Test
    void forward() {
        int initialX = hero.getLocationX();
        int initialY = hero.getLocationY();
        
        hero.forward();
        
        assertEquals(initialX, hero.getLocationX(), "Hero should not move in X direction");
        assertEquals(initialY, hero.getLocationY(), "Hero should not move in Y direction");
    }

    @org.junit.jupiter.api.Test
    void sethp() {
        hero.sethp(500);
        assertEquals(500, hero.getHp(), "HP should be set to 500");
        
        hero.sethp(0);
        assertEquals(0, hero.getHp(), "HP can be set to 0");
    }

    @org.junit.jupiter.api.Test
    void activateSpreadFire() {
        hero.activateSpreadFire();
        List<BaseBullet> bullets = hero.shoot();
        assertEquals(3, bullets.size(), "Spread fire should produce 3 bullets");
    }
}