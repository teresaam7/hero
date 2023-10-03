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
        graphics.setForegroundColor(TextColor.Factory.fromString("#DB7093"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(getPosition().getX(), getPosition().getY()), "H");
    }
}
