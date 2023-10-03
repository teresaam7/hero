import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;
import java.util.*;

public class Arena {
    private int width;
    private int height;
    private Hero hero;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;
    private long lastMonsterSpawnTime;
    private long monsterSpawnInterval = 5000;

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
        this.hero = new Hero(width / 2, height / 2, 6);
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
        for (int i = 0; i < 5; i++)
            coins.add(new Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
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
    public void initializeMonsters() {
        monsters.clear();
        lastMonsterSpawnTime = System.currentTimeMillis();
        for (int i = 0; i < 7; i++) {
            spawnMonster();
        }
    }
    // Methods for update the monsters' positions
    private List<Monster> createMonsters(){
        Random random = new Random();
        List<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            monsters.add(new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
        }
        return monsters;
    }
    public void spawnMonster() {
        Random random = new Random();
        Monster newMonster = new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);
        monsters.add(newMonster);
    }
    public void updateMonsters() {
        long currentTime = System.currentTimeMillis();
        // Check if it's time to spawn a new monster
        if (currentTime - lastMonsterSpawnTime >= monsterSpawnInterval) {
            createCoins();
            lastMonsterSpawnTime = currentTime;
        }
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
    public void moveMonsters() {
        Iterator<Monster> iterator = monsters.iterator();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            Position newPosition = monster.move();
            // Check if the new position collides with a wall
            if (!canMonsterMove(newPosition)) {
                iterator.remove();
                continue;
            }
            monster.setPosition(newPosition);
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
    // Draw method for the scenario and all the components of the game
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
        graphics.putString(new TerminalPosition(2, 1), "Energy: " + hero.getEnergy().getEnergy());
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
    // Method that loads the map of the game from the map.txt file
    public void loadMapFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Random random = new Random();
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    char symbol = line.charAt(col);
                    // Adjustment of the screen's size
                    int screenX = col + (width - line.length()) / 2;
                    int screenY = row + (height - 1 - row);
                    if (symbol == 'W') {
                        walls.add(new Wall(screenX, screenY));
                    } else if (symbol == 'H') {
                        hero.setPosition(new Position(screenX, screenY/2));
                    } else if (symbol == '$') {
                        coins.add(new Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
                    } else if (symbol == 'M') {
                        monsters.add(new Monster(screenX, screenY));
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
