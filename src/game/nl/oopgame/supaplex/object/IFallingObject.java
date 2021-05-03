/**
 * Interface dat gebruikt moet worden voor objecten die aan de zwaartekracht onderheven zijn
 */

package nl.oopgame.supaplex.object;

public interface IFallingObject {

    boolean canFall();

    boolean isObjectUnderObject();

    boolean isPlayerMovingUnderObject();
}
