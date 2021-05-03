/**
 * Interface voor objecten die kunnen omvallen
 */

package nl.oopgame.supaplex.object;

public interface ITippingObject {

    void checkTipOver();

    boolean checkTipOverTiles(int targetX, int thisY, int targetY);

    boolean checkTipOverObjects(int targetX, int thisY, int targetY);

    void tipOver();

    void resetTipOver();
}