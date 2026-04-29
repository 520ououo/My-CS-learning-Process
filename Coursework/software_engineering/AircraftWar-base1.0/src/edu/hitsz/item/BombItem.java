package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

import java.util.ArrayList;
import java.util.List;

public class BombItem extends BaseItem {

    private int bombDamage;
    
    private List<ItemObserver> observers = new ArrayList<>();

    public BombItem(int locationX, int locationY, int speedX, int speedY, int bombDamage) {
        super(locationX, locationY, speedX, speedY);
        this.bombDamage = bombDamage;
    }

    public void addObserver(ItemObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (ItemObserver observer : observers) {
            observer.onBombEffect();
        }
    }

    public List<ItemObserver> getObservers() {
        return observers;
    }

    @Override
    public void active(HeroAircraft hero) {
        notifyObservers();
        System.out.println("Bomb active!");
    }

}
