package com.teresaam7.hero;

import org.junit.Test;
import static org.junit.Assert.*;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.teresaam7.hero.elements.Hero;
import com.teresaam7.hero.elements.Position;

public class HeroTest {
    private TextGraphics graphics;
    @Test
    public void canHeroMoveTest(){
        Hero hero = new Hero(10, 10, 6);

        Position position = hero.moveUp();
        assertEquals(10, hero.getPosition().getX());
        assertEquals(9, hero.getPosition().getY());

        position = hero.moveDown();
        assertEquals(10, hero.getPosition().getX());
        assertEquals(11, hero.getPosition().getY());

        position = hero.moveRight();
        assertEquals(11, hero.getPosition().getX());
        assertEquals(10, hero.getPosition().getY());

        position = hero.moveLeft();
        assertEquals(9, hero.getPosition().getX());
        assertEquals(10, hero.getPosition().getY());
    }

    @Test
    public void EnergyDecreaseTest() {
        Hero hero = new Hero(10, 10, 6);
        hero.energyDecrease(1);
        assertEquals(5, hero.getEnergy());
    }

    @Test
    public void testHeroCannotMoveOutOfBounds() {
        Hero hero = new Hero(10, 10, mock(TextGraphicsFunctions.class));
        assertFalse(hero.canMove(new Position(12, 12)));
    }
}
