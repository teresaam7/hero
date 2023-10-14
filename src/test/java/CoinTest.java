import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.teresaam7.hero.elements.Coin;
import com.teresaam7.hero.elements.GenericTextGraphics;
import com.teresaam7.hero.elements.Monster;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class CoinTest {
    private TextGraphics graphics;
    @Test
    public void testCoinDraw() {
        GenericTextGraphics graphics = mock(GenericTextGraphics.class);
        Coin coin = new Coin(10, 10);

        doNothing().when(graphics).setForegroundColor(any());
        doNothing().when(graphics).enableModifiers(SGR.BOLD);
        doNothing().when(graphics).putString(any(), any());

        coin.draw(graphics);

        verify(graphics).setForegroundColor(TextColor.Factory.fromString("#ffdf00"));
        verify(graphics).enableModifiers(SGR.BOLD);
        verify(graphics).putString(new TerminalPosition(coin.getPosition().getX(), coin.getPosition().getY()), "$");
    }
}
