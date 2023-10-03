import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        try {
            Game game = new Game(150, 42);
            game.run();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
