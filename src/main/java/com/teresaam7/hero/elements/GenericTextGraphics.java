package com.teresaam7.hero.elements;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
public interface GenericTextGraphics {
    void setForegroundColor(TextColor color);
    void fillRectangle(TerminalPosition position, TerminalSize size, char character);
    void enableModifiers(SGR... modifiers);
    void putString(TerminalPosition position, String str);
    void setBackgroundColor(TextColor color);
}
