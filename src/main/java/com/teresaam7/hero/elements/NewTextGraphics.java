package com.teresaam7.hero.elements;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class NewTextGraphics implements GenericTextGraphics{
    private TextGraphics graphics;
    private Screen screen;
    public NewTextGraphics(TextGraphics graphics) {
        this.graphics = graphics;
    }
    @Override
    public void setForegroundColor(TextColor color){
        graphics.setForegroundColor(color);
    }
    @Override
    public void fillRectangle(TerminalPosition position, TerminalSize size, char character){
        graphics.fillRectangle(position, size, character);
    }
    @Override
    public void enableModifiers(SGR... modifiers){
        graphics.enableModifiers(modifiers);
    }
    @Override
    public void putString(TerminalPosition position, String str){
        graphics.putString(position, str);
    }
    @Override
    public void setBackgroundColor(TextColor color){
        graphics.setBackgroundColor(color);
    }
    @Override
    public TextGraphics newTextGraphics() {
        return screen.newTextGraphics();
    }
}
