/**
 * MenuScreen tekent het menuscherm van waaruit het spel gestart kan worden
 */

package nl.oopgame.supaplex.screen;

import nl.han.ica.oopg.objects.TextObject;
import nl.oopgame.supaplex.*;
import nl.oopgame.supaplex.button.Button;
import processing.core.PGraphics;

import java.util.ArrayList;

public class MenuScreen extends Screens {

    private ArrayList<Level> levels;
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Button> levelButtons = new ArrayList<>();
    private SupaplexApp app;
    private boolean generateMenuItems = false;
    private int titleFontSize = 100;    // Stel font size in voor de titel
    private int levelFontSize = 20;     // Stel font size in voor de level keuze
    private int levelStartWidth = 100;  // De X coordinaat om de levels weer te geven
    private int levelStartHeight = 200; // De Y coordinaat om de levels weer te geven
    private int buttonWidth = 200;      // De breedte van de levels knoppen
    private int startButtonX = SupaplexApp.getWorldWidth() / 2 - 100;
    private int startButtonY = SupaplexApp.getWorldHeight() / 2 - 50;
    private int startButtonHeight = 50;

    public MenuScreen(SupaplexApp app,ArrayList<Level> levels) {
        super(0,0,SupaplexApp.getWorldWidth(),SupaplexApp.getWorldHeight());
        this.app = app;
        this.levels = levels;
    }

    /**
     * Teken het menuscherm
     */
    public void drawScreen() {

        if(!generateMenuItems) {
            // Maak de knoppen aan
            this.generateStartButtons();

            this.generateLevelButtons();

            // Teken de titel
            this.drawTitle();
            generateMenuItems = true;
        }
    }

    /**
     * Tekent een knop
     * @param x             De X coordinaat van de knop
     * @param y             De Y coordinaat van de knop
     * @param buttonWidth   De breedte van de knop
     * @param buttonHeight  De hoogte van de knop
     * @param buttonText    De tekst in de knop
     * @param buttonColor   De kleur van de knop
     */
    public void drawButton(int x, int y, int buttonWidth, int buttonHeight, String buttonText, int buttonColor) {
        app.fill(buttonColor);
        app.rect(x, y, buttonWidth, buttonHeight);
        app.fill(123,123,125);
        app.textAlign(app.CENTER, app.CENTER);
        app.textSize(11);
        app.text(buttonText, x + buttonWidth / 2, y + buttonHeight / 2);
    }


    /**
     * Genereer de menu knoppen
     */
    private void generateStartButtons() {
        buttons.add(new Button(app, this.startButtonX,this.startButtonY,this.startButtonHeight,this.buttonWidth,200, "Start Game", GameState.GAME));
        buttons.add(new Button(app, this.startButtonX,this.startButtonY+100,this.startButtonHeight,this.buttonWidth,200, "Exit Game",GameState.EXIT));
    }

    @Override
    public void draw(PGraphics g){
        for (Button btn: buttons) {
            btn.draw(app.g);
        }
        generateLevelButtons();
    }

    /**
     * Teken de titel van het spel in het scherm
     */
    private void drawTitle() {
        TextObject title = new TextObject("SUPAPLEX", this.titleFontSize);
        title.setForeColor(255, 37, 13, 255);
        app.addGameObject(title, SupaplexApp.getWorldWidth() / 2 - 220, 20);
    }

    /**
     * Teken de levels in het scherm
     */
    private void generateLevelButtons() {
        // TODO maak hiervan twee functies een voor genereren en andere voor het tekenen
        // TODO genereer de knoppen voor de levels via de buttons class
        int buttonHeight = this.levelStartHeight;

        if (levels.size() > 0) {

            TextObject levelHeader = new TextObject("Select level: ", this.levelFontSize);
            levelHeader.setForeColor(255, 37, 13, 255);
            app.addGameObject(levelHeader, this.levelStartWidth, buttonHeight - this.levelFontSize);

            for (Level l : levels) {
                // Stel een prefix in voor het level, als het geselecteerd is
                Boolean levelIsSelected = SupaplexApp.getSelectedLevel().getLevelName() == l.getLevelName();
                String levelPrefix = levelIsSelected ? " > " : "";
                int buttonColor = levelIsSelected ? app.color(255, 37, 13) : app.color(22, 22, 22);

                // Druk level af
                this.drawButton(this.levelStartWidth, buttonHeight, this.buttonWidth, this.levelFontSize, levelPrefix + l.getLevelName(), buttonColor);
                // Druk het volgende level op een volgende regel af
                buttonHeight += this.levelFontSize;
            }
        }
    }

    /**
     * Controleert of de muis over een knop gaat
     * @param x                 De X coordinaat van de knop
     * @param y                 De Y coordinaat van de knop
     * @param buttonWidth       De breedte van de knop
     * @param buttonHeight      De hoogte van de knop
     * @return
     */
    public boolean mouseOverButton(int x, int y, int buttonWidth, int buttonHeight) {
        return app.mouseX >= x && app.mouseX <= x + buttonWidth &&
                app.mouseY >= y && app.mouseY <= y + buttonHeight;
    }

    /**
     * Controleer of er op een level knop gedrukt is. Zo ja, return het nieuwe geselecteerde level
     * @return Level    Het geselecteerde level
     */
    public Level checkClickAction(Level selectedLevel) {
        //TODO zet dit om naar buttons class mouseover functie
        int buttonHeight = this.levelStartHeight;

        for (Level l : levels) {
            if (app.mousePressed && mouseOverButton(this.levelStartWidth, buttonHeight, this.buttonWidth, this.levelFontSize)) {
                return l;
            }

            buttonHeight += this.levelFontSize;
        }

        return selectedLevel;
    }

    /**
     * Controleer of er op de startknop geklikt is
     * @return boolean      True als er op de startknop geklikt is
     */
    public GameState startLevel() {
        for(Button btn : buttons){
            if(app.mousePressed && btn.mouseOverButton(app.mouseX, app.mouseY)){
                System.out.println("HIT MENUSCREEN WITH GAMESTATE OF: "+btn.getGameState());
                return btn.getGameState();
            }
        }
        return GameState.MENU;
    }
}