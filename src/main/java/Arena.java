import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;
import java.util.*;

public class Arena {
    private int width;
    private int height;
    private Hero hero;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Arena(int width, int height){
        this.width = width;
        this.height = height;
        // Starting position in the middle
        this.hero = new Hero(width / 2, height / 2, 20);
        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();
    }
    // Methods related with the Hero's movement
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
    // Method related with the creation of walls
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
    // Methods related with the Coins that are collected by the Hero
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
    // Methods related to the monsters that are disturbing our Hero
    private List<Monster> createMonsters(){
        Random random = new Random();
        List<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            monsters.add(new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
        }
        return monsters;
    }
    public boolean canMonsterMove(Position position){
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
    public void moveMonsters(){
        for (Monster monster: monsters){
            if(canMonsterMove(monster.move())){
                monster.setPosition(monster.move());
            }
        }
    }
    public boolean verifyMonsterCollisions(){
        for (Monster monster: monsters){
            if(monster.getPosition().equals(hero.getPosition())){
                hero.energyDecrease(1);
                if (hero.getEnergy().emptyEnergy())
                    return true;
            }
        }
        return false;
    }
    // Draw method for the scenario and all the componenets of the game
    public void draw(TextGraphics graphics) throws IOException {
        // Drawing the arena
        graphics.setBackgroundColor(TextColor.Factory.fromString("#87CEEB"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        // Drawing the walls
        for (Wall wall : walls) {
            wall.draw(graphics);
        }
        // Drawing the coins
        for (Coin coin : coins){
            coin.draw(graphics);
        }
        // Drawing the monsters
        for (Monster monster : monsters) {
            monster.draw(graphics);
        }
        // Drawing the hero
        hero.draw(graphics);
        // Drawing the energy level of the hero
        TextColor energyColor = changeEnergyColor(hero.getEnergy().getEnergy());
        graphics.setForegroundColor(energyColor);
        graphics.putString(new TerminalPosition(1, 1), "Energy: " + hero.getEnergy().getEnergy());
        graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
    }
    // Method to change the color of the energy
    private TextColor changeEnergyColor(int energyLevel) {
        if (energyLevel > 20) {
            return TextColor.Factory.fromString("#00FF00"); // Green
        } else if (energyLevel > 15) {
            return TextColor.Factory.fromString("#FFFF00"); // Yellow
        } else if (energyLevel > 10) {
            return TextColor.Factory.fromString("#FFA500"); // Orange
        } else {
            return TextColor.Factory.fromString("#FF0000"); // Red
        }
    }
}
