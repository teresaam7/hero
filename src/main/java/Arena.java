import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.*;

public class Arena {
    private int width;
    private int height;
    private Hero hero;
    private List<Wall> walls;
    private List<Coin> coins;
    public Arena(int width, int height){
        this.width = width;
        this.height = height;
        // Starting position in the middle
        this.hero = new Hero(width / 2, height / 2);
        this.walls = createWalls();
        this.coins = createCoins();
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
        if (canHeroMove(position)){
            hero.setPosition(position);
            retrieveCoins();
        }
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
    private List<Coin> createCoins() {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        // Creation of a list to track existing positions
        List<Position> candidatePositions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            // Generation of a random position for the coin
            Position coinPosition = getRandomPosition(random);
            // Verify if the coin is colliding with the hero and with other coins
            while (candidatePositions.contains(coinPosition) || coinPosition.equals(hero.getPosition())) {
                coinPosition = getRandomPosition(random);
            }
            coins.add(new Coin(coinPosition.getX(), coinPosition.getY()));
            candidatePositions.add(coinPosition);
        }
        return coins;
    }
    private Position getRandomPosition(Random random) {
        int cX = random.nextInt(width - 2) + 1;
        int cY = random.nextInt(height - 2) + 1;
        return new Position(cX, cY);
    }
    private boolean collisionCoins(int x, int y){
        for (Coin coin: coins){
            if (coin.getPosition().getX() == x && coin.getPosition().getY() == y){
                return true;
            }
        }
        return false;
    }
    private boolean collisionHero(int x, int y){
        return (hero.getPosition().getX() == x && hero.getPosition().getY() == y);
    }
    public void retrieveCoins(){
        Iterator<Coin> iterator = coins.iterator();
        while (iterator.hasNext()) {
            Coin coin = iterator.next();
            if (coin.getPosition().equals(hero.getPosition())) {
                iterator.remove();
                break;
            }
        }
    }
    public void draw(TextGraphics graphics) throws IOException {
        // Drawing the arena
        graphics.setBackgroundColor(TextColor.Factory.fromString("#87ceeb"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        // Drawing the walls
        for (Wall wall : walls) {
            wall.draw(graphics);
        }
        // Drawing the coins
        for (Coin coin : coins){
            coin.draw(graphics);
        }
        // Drawing the hero
        hero.draw(graphics);
    }
}
