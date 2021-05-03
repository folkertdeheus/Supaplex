/**
 * Deze class bouwt het eindscherm op en tekent deze
 * Afhankelijk van of een level is gehaald of niet wordt er een scherm getekend
 */

package nl.oopgame.supaplex.screen;

import nl.han.ica.oopg.objects.TextObject;
import nl.oopgame.supaplex.GameState;
import nl.oopgame.supaplex.SupaplexApp;
import nl.oopgame.supaplex.button.Button;
import processing.core.PGraphics;

import java.util.ArrayList;

public class GameEndScreen extends Screens {

    private boolean gameCompleted;
    private ArrayList<Button> buttons = new ArrayList<>();
    private SupaplexApp app;
    private int titleFontSize = 100;    // Stel font size in voor de titel
    private int textFontSize = 30;      // Stel font size in voor de titel
    private int buttonWidth = 200;      // De breedte van de knoppen
    private int endButtonX = SupaplexApp.getWorldWidth() / 2 - 100;
    private int endButtonY = SupaplexApp.getWorldHeight() / 2 - 50;
    private int endButtonHeight = 50;

    public GameEndScreen(SupaplexApp app,boolean gameCompleted) {
        super(0,0, SupaplexApp.getWorldWidth(),SupaplexApp.getWorldHeight());
        this.gameCompleted = gameCompleted;
        this.app = app;
    }

    /**
     * Teken het eindscherm
     */
    public void drawScreen() {
        drawGameFinishedScreen();
        if(gameCompleted) {
            drawVictory();
        }else{
            drawDefeat();
        }
    }

    /**
     * Maak de knoppen voor het eindscherm
     */
    private void generateEndButtons() {
        buttons.add(new Button(app, this.endButtonX,this.endButtonY,this.endButtonHeight,this.buttonWidth,200, "Play Again", GameState.GAME));
        buttons.add(new Button(app, this.endButtonX,this.endButtonY +100,this.endButtonHeight,this.buttonWidth,200, "Back To Menu",GameState.MENU));
    }

    /**
     * Teken de knoppen elke gameloop opnieuw
     * @param g PGraphics object will be given by the GameEngine.
     */
    @Override
    public void draw(PGraphics g){
        for (Button btn: buttons) {
            btn.draw(app.g);
        }
    }

    /**
     * Teken de titel van het gameover scherm
     */
    private void drawTitle() {
        TextObject title = new TextObject("Game Over", this.titleFontSize);
        title.setForeColor(255, 37, 13, 255);
        app.addGameObject(title, SupaplexApp.getWorldWidth() / 2 - 260, 20);
    }

    /**
     * Teken de victory tekst
     */
    private void drawVictory(){
        TextObject title = new TextObject("You have finished the game thanks for playing", textFontSize);
        title.setForeColor(255, 37, 13, 255);
        app.addGameObject(title, SupaplexApp.getWorldWidth() / 2 - 330, 140);
    }

    /**
     * Teken de defeat tekst
     */
    private void drawDefeat(){
        TextObject title = new TextObject("You have died", textFontSize);
        title.setForeColor(255, 37, 13, 255);
        app.addGameObject(title, SupaplexApp.getWorldWidth() / 2 - 130, 140);
    }

    /**
     * Teken de eind tijd
     */
    private void drawFinalTime() {
        TextObject title = new TextObject("You were playing this lvl for: "+SupaplexApp.getScoreBoard().stopWatch.getElapsedTime(), textFontSize);
        title.setForeColor(255, 37, 13, 255);
        app.addGameObject(title, SupaplexApp.getWorldWidth() / 2 - 260, 180);
    }

    /**
     * Teken de eind score
     */
    private void drawCollectedInfotrons() {
        TextObject title = new TextObject("You have collected "+SupaplexApp.getSelectedLevel().getCollectedInfotrons()+" Infotrons", textFontSize);
        title.setForeColor(255, 37, 13, 255);
        app.addGameObject(title, SupaplexApp.getWorldWidth() / 2 - 200, 230);
    }

    /**
     * Teken het "level gehaald" scherm
     */
    private void drawGameFinishedScreen() {
        drawTitle();
        drawFinalTime();
        drawCollectedInfotrons();
        generateEndButtons();
    }

    /**
     * Controleer of er op de startknop geklikt is
     * @return boolean      True als er op de startknop geklikt is
     */
    public GameState goBackToMenu() {
        for(Button btn : buttons){
            if(app.mousePressed && btn.mouseOverButton(app.mouseX, app.mouseY)){
                return btn.getGameState();
            }
        }
        return GameState.END;
    }
}
