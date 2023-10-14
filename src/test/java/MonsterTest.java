import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.teresaam7.hero.elements.GenericTextGraphics;
import com.teresaam7.hero.elements.Hero;
import com.teresaam7.hero.elements.Monster;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class MonsterTest {
    private TextGraphics graphics;
    @Test
    public void testMonsterDraw() {
        GenericTextGraphics graphics = mock(GenericTextGraphics.class);
        Monster monster = new Monster(10, 10);

        doNothing().when(graphics).setForegroundColor(any());
        doNothing().when(graphics).enableModifiers(SGR.BOLD);
        doNothing().when(graphics).putString(any(), any());

        monster.draw(graphics);

        verify(graphics).setForegroundColor(TextColor.Factory.fromString("#5e7ce2"));
        verify(graphics).enableModifiers(SGR.BOLD);
        verify(graphics).putString(new TerminalPosition(monster.getPosition().getX(), monster.getPosition().getY()), "M");
    }
}
