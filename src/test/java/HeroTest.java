import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.teresaam7.hero.elements.GenericTextGraphics;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.teresaam7.hero.elements.Hero;
import com.teresaam7.hero.elements.Position;

public class HeroTest {
    private TextGraphics graphics;
    @Test
    public void canHeroMoveTest(){
        Hero hero = new Hero(10, 10, 6);

        hero.moveUp();
        assertEquals(9, hero.getPosition().getY());

        hero.moveDown();
        assertEquals(10, hero.getPosition().getY());

        hero.moveRight();
        assertEquals(11, hero.getPosition().getX());

        hero.moveLeft();
        assertEquals(10, hero.getPosition().getX());
    }

    @Test
    public void EnergyDecreaseTest() {
        Hero hero = new Hero(10, 10, 6);
        hero.energyDecrease(1);
        assertEquals(5, hero.getEnergy().getEnergy());
    }

    @Test
    public void testHeroDraw() {
        GenericTextGraphics graphics = mock(GenericTextGraphics.class);
        Hero hero = new Hero(10, 10, 6);

        doNothing().when(graphics).setForegroundColor(any());
        doNothing().when(graphics).enableModifiers(SGR.BOLD);
        doNothing().when(graphics).putString(any(), any());

        hero.draw(graphics);

        verify(graphics).setForegroundColor(TextColor.Factory.fromString("#ff6392"));
        verify(graphics).enableModifiers(SGR.BOLD);
        verify(graphics).putString(new TerminalPosition(hero.getPosition().getX(), hero.getPosition().getY()), "H");
    }

    @Test
    public void testChangeEnergyColor() {
        Hero hero = new Hero(0, 0, 0);

        TextColor color1 = hero.changeEnergyColor(6);
        TextColor color2 = hero.changeEnergyColor(5);
        TextColor color3 = hero.changeEnergyColor(4);
        TextColor color4 = hero.changeEnergyColor(3);

        assertEquals(TextColor.Factory.fromString("#affc41"), color1);
        assertEquals(TextColor.Factory.fromString("#FFFF00"), color2);
        assertEquals(TextColor.Factory.fromString("#FFA500"), color3);
        assertEquals(TextColor.Factory.fromString("#FF0000"), color4);
    }
}
