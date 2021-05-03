/**
 * Class die knoppen tekent en controleert of er acties op nodig zijn
 */

package nl.oopgame.supaplex.button;

import nl.han.ica.oopg.objects.GameObject;
import nl.oopgame.supaplex.GameState;
import nl.oopgame.supaplex.SupaplexApp;
import processing.core.PGraphics;

public class Button extends GameObject {

    private String description;
    private int color;
    private GameState gameState;

    //<editor-fold desc="getters & setters">
    public GameState getGameState(){
        return gameState;
    }
    //</editor-fold>

    /**
     * Maak knop
     */
    public Button(SupaplexApp world, float x, float y, int height, int width, int color, String text, GameState gameState) {
        setHeight(height);
        setWidth(width);
        setX(x);
        setY(y);
        this.color = color;
        this.description = text;
        this.gameState = gameState;
    }

    /**
     * GameObject vereist deze methode, afhandeling gebeurt echter in draw()
     * Dit voorkomt knipperingen in het scherm
     */
    @Override
    public void update() { }

    /**
     * Teken de knop elk frame opnieuw
     * @param g PGraphics object will be given by the GameEngine.
     */
    @Override
    public void draw(PGraphics g) {
        g.rectMode(g.CORNER);
        g.stroke(color);
        g.fill(color);
        g.rect(this.x, this.y, this.width, this.height);
        g.fill(0xFFFFFFFF);
        g.textAlign(g.CENTER, g.CENTER);
        g.text(description, this.x+this.width/2, this.y+this.height/2);
    }

    /**
     * Controleert of de muis over een knop gaat
     * @param x                 De X coordinaat van de knop
     * @param y                 De Y coordinaat van de knop
     * @return
     */
    public boolean mouseOverButton(int x, int y) {
        return x >= this.x && x <= (this.x + this.width) && y >= this.y && y <= (this.y + this.height);
    }
}
