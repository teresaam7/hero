package com.teresaam7.hero.elements;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Coin extends Element {
    public Coin(int x, int y){
        super(x, y);
    }
    public void draw(GenericTextGraphics graphics){
        graphics.setForegroundColor(TextColor.Factory.fromString("#ffdf00"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(getPosition().getX(), getPosition().getY()), "$");
    }
}
