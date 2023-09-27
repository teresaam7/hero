import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class Arena {
    private int width;
    private int height;
    private Hero hero;
    public Arena(int width, int height){
        this.width = width;
        this.height = height;
        // Starting position in the middle
        this.hero = new Hero(width / 2, height / 2);
    }
    public boolean canHeroMove(Position position){
        int x = position.getX();
        int y = position.getY();
        return (x < width && y < height && x >= 0 && y >= 0);
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
    public void draw(Screen screen) throws IOException {
        screen.clear();
        hero.draw(screen);
        screen.refresh();
    }
}
