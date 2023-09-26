import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class Hero {
    private Position position;

    public Hero(int x, int y){
        this.position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position moveUp(){
        return new Position(position.getX(), position.getY() - 1);
    }
    public Position moveDown(){
        return new Position(position.getX(), position.getY() + 1);
    }
    public Position moveRight(){
        return new Position(position.getX() + 1, position.getY());
    }
    public Position moveLeft(){
        return new Position(position.getX() - 1, position.getY());
    }
    public void draw(Screen screen) throws IOException{
        int x = position.getX();
        int y = position.getY();
        screen.setCharacter(x, y, TextCharacter.fromCharacter('X')[0]);
    }
}
