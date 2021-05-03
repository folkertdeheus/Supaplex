/**
 * De Infotron class maakt infotrons en houdt bij of deze kunnen (om)vallen
 */

package nl.oopgame.supaplex.object;

import nl.han.ica.oopg.objects.Sprite;
import nl.oopgame.supaplex.Constants;
import nl.oopgame.supaplex.SupaplexApp;

import java.util.List;

public class Infotron extends Objects{

    /**
     * Create a new SpriteObject with a Sprite object.
     *
     * @param sprite The sprite
     */
    public Infotron(Sprite sprite, SupaplexApp world) {
        super(sprite, world);
    }

    @Override
    public void update() {
        if (canFall()) {
            setDirection(180);
            setSpeed(Constants.SPEED);
            isFalling = true;
        } else {
            setSpeed(0);
            isFalling = false;
        }

        // Controleer of de steen kan omvallen
        if (tipDirection == 0) {
            checkTipOver();
        }

        tipOver();
        resetTipOver();
    }
}