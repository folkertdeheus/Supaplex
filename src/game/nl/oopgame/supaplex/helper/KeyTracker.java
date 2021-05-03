/**
 * Deze class houdt bij welke knoppen er op dit moment ingedrukt zijn
 * Extra class is nodig omdat de game engine zelf niet bij kan houden wanneer er meerdere knoppten tegelijk worden ingedrukt
 */

package nl.oopgame.supaplex.helper;

public class KeyTracker {
    private int key;
    private boolean pressed;

    //<editor-fold desc="getters & setters">
    public int getKey() {
        return this.key;
    }

    public boolean getPressed() {
        return this.pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
    //</editor-fold>

    public KeyTracker(int key) {
        this.key = key;
        this.pressed = false;
    }
}