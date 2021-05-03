/**
 * Abstract class om te forceren dat elke class zijn eigen scherm tekent
 * Deze class zorgt via de constructor voor de plaatsing van de schermen
 */

package nl.oopgame.supaplex.screen;

import nl.han.ica.oopg.dashboard.Dashboard;

abstract public class Screens extends Dashboard {

    public Screens(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public abstract void drawScreen();
}
