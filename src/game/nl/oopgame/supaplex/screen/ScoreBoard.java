/**
 * Deze class maakt en tekent het dashboard wat tijdens het spel getoond wordt
 */

package nl.oopgame.supaplex.screen;

import nl.han.ica.oopg.dashboard.Dashboard;
import nl.oopgame.supaplex.SupaplexApp;
import nl.oopgame.supaplex.helper.StopWatch;
import processing.core.PGraphics;
import processing.core.PImage;

public class ScoreBoard extends Dashboard{

    private PImage infotrons;
    private int currentScore;
    public StopWatch stopWatch;

    public ScoreBoard(SupaplexApp app) {
        super(app.getWorldWidth() - 100,0,app.getWorldWidth(),app.getWorldHeight());
        this.infotrons = app.loadImage("src/assets/sprites/sprite_infotron.png", "png");
        this.currentScore = 0;
        this.stopWatch = new StopWatch();
        stopWatch.start();
    }

    /**
     * Verhoog de score in het huidige level
     */
    public void updateScore(){
        this.currentScore++;
    }

    /**
     * Werk elk frame het scorebord bij
     * @param g PGraphics object will be given by the GameEngine.
     */
    @Override
    public void draw(PGraphics g){
        g.image(infotrons, (this.width - 390), 50, 50, 50);
        g.textAlign(CENTER, CENTER);
        g.fill(255,255,255);

        g.noFill();
        g.strokeWeight(5);
        g.textSize(26);
        g.text(currentScore+"/"+SupaplexApp.getSelectedLevel().getInfotronsNeeded(), width - 300, 75);
        g.text(stopWatch.getElapsedTime(), width - 200, 75);
        g.rect((this.width - 400), 50, 350, 55, 7);
    }

}
