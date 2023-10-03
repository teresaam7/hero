package elements;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class Hero extends Element {
    private Energy energy;
    public Hero(int x, int y, int startEnergy){
        super(x, y);
        this.energy = new Energy(startEnergy);
    }
    public void setEnergy(Energy energy) {
        this.energy = energy;
    }
    public Energy getEnergy() {
        return energy;
    }
    public void energyDecrease(int amount) {
        energy.energyLoss(amount);
    }

    public Position moveUp(){
        return new Position(getPosition().getX(), getPosition().getY() - 1);
    }
    public Position moveDown(){
        return new Position(getPosition().getX(), getPosition().getY() + 1);
    }
    public Position moveRight(){
        return new Position(getPosition().getX() + 1, getPosition().getY());
    }
    public Position moveLeft(){
        return new Position(getPosition().getX() - 1, getPosition().getY());
    }
    public void draw(TextGraphics graphics){
        // Drawing the character Hero
        graphics.setForegroundColor(TextColor.Factory.fromString("#DB7093"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(getPosition().getX(), getPosition().getY()), "H");

        // Drawing the energy of the Hero
        TextColor energyColor = changeEnergyColor(getEnergy().getEnergy());
        graphics.setForegroundColor(energyColor);
        graphics.putString(new TerminalPosition(2, 1), "Energy: " + getEnergy().getEnergy());
        graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
    }
    // Method to change the color of the energy
    private TextColor changeEnergyColor(int energyLevel) {
        if (energyLevel > 5) {
            return TextColor.Factory.fromString("#00FF00"); // Green
        } else if (energyLevel > 4) {
            return TextColor.Factory.fromString("#FFFF00"); // Yellow
        } else if (energyLevel > 3) {
            return TextColor.Factory.fromString("#FFA500"); // Orange
        } else {
            return TextColor.Factory.fromString("#FF0000"); // Red
        }
    }
}
