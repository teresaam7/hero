import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Arena {
    private int width;
    private int height;
    private Hero hero;
    private List<Wall> walls;
    public Arena(int width, int height){
        this.width = width;
        this.height = height;
        // Starting position in the middle
        this.hero = new Hero(width / 2, height / 2);
        this.walls = createWalls();
    }
    public boolean canHeroMove(Position position){
        int x = position.getX();
        int y = position.getY();
        if (x >= width || y >= height || x < 0 || y < 0){
            return false;
        }
        // Check if the position collides with a wall
        for (Wall wall: walls) {
            if(wall.getPosition().equals(position)){
                return false;
            }
        }
        return true;
    }
    public void moveHero(Position position) {
        if (canHeroMove(position))
            hero.setPosition(position);
    }
    public void processKey(KeyStroke key) {
        switch (key.getKeyType()){
            case ArrowRight:
                moveHero(hero.moveRight());
                break;
            case ArrowLeft:
                moveHero(hero.moveLeft());
                break;
            case ArrowDown:
                moveHero(hero.moveDown());
                break;
            case ArrowUp:
                moveHero(hero.moveUp());
                break;
        }
    }
    private List<Wall> createWalls() {
        List<Wall> walls = new ArrayList<>();
        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }
        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }
        return walls;
    }
    public void draw(TextGraphics graphics) throws IOException {
        // Drawing the arena
        graphics.setBackgroundColor(TextColor.Factory.fromString("#57cef5"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        // Drawing the walls
        for (Wall wall : walls) {
            wall.draw(graphics);
        }
        // Drawing the hero
        hero.draw(graphics);
    }
}
