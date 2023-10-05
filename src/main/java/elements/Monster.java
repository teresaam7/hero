package elements;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import elements.Element;

import java.util.Random;

public class Monster extends Element {
    private long spawnTime;
    private boolean despawned;
    public Monster(int x, int y){
        super(x, y);
        this.spawnTime = System.currentTimeMillis();
        this.despawned = false;
    }

    public long getSpawnTime() {
        return spawnTime;
    }
    public boolean isDespawned() {
        return despawned;
    }

    public void setDespawned(boolean despawned) {
        this.despawned = despawned;
    }

    public Position move(){
        Random random = new Random();
        int dx = getPosition().getX();
        int dy = getPosition().getY();
        int direction = random.nextInt(4);
        switch (direction) {
            case 0:
                dy--;
                break;
            case 1:
                dy++;
                break;
            case 2:
                dx--;
                break;
            case 3:
                dx++;
                break;
            default:
                break;
        }
        return new Position(dx, dy);
    }
    public void draw(TextGraphics graphics){
        graphics.setForegroundColor(TextColor.Factory.fromString("#5e7ce2"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(getPosition().getX(), getPosition().getY()), "M");
    }
}
