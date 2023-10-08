package com.teresaam7.hero.elements;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Energy{
    private int energy;
    public Energy(int energy){
        this.energy = energy;
    }
    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public void energyLoss(int amount){
        this.energy -= amount;
    }
    public boolean emptyEnergy() {
        return energy <= 0;
    }
}
