/**
 * Class om de stenen in het spel te maken
 * Hier worden de bewegingen van de steen gecontroleerd en afgehandeld
 */

package nl.oopgame.supaplex.object;

import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.oopgame.supaplex.Constants;
import nl.oopgame.supaplex.SupaplexApp;
import nl.oopgame.supaplex.enemy.Scissors;

import java.util.List;

public class Stone extends Objects implements ICollidableWithGameObjects {

    /**
     * Create a new SpriteObject with a Sprite object.
     *
     * @param sprite The sprite
     */
    public Stone(Sprite sprite, SupaplexApp world) {
        super(sprite, world);
    }

    /**
     * Update loop om het verloop gedurende de game te controleren
     */
    @Override
    public void update() {

        // Controleer of de steen kan vallen
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

    /**
     * Afhandeling wanneer de steen met game objecten in aanraking komt
     * @param collidedGameObjects The GameObjects with which a collision should be detected
     */
    @Override
    public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
        for (GameObject go : collidedGameObjects) {

            if (go instanceof Player) {
                if(go.getY() > getY() && isFalling) {
                    go.setSpeed(0);
                    world.gameOver(false);
                    break;
                }

                if (go.getY() == getY()) {
                    pushStone(go);
                }

                // Voorkom dat de speler op de plek van de steen gaat staan als de speler boven of onder de steen staat
                if (go.getY() <= this.getY() + Constants.TILESIZE && go.getY() > this.getY() ||
                        (go.getY() >= this.getY() - Constants.TILESIZE && go.getY() < this.getY())) {
                    go.setY(go.getY() - Constants.SPEED);
                    go.setSpeed(0);
                }
            }

            if (go instanceof Scissors) {
                if (go.getY() > getY()) {
                    world.deleteGameObject(go);
                }
            }

            if (go instanceof Stone) {
                if (go.getY() > getY()) {
                    setY(getY() - getSpeed());
                    go.setSpeed(0);
                }
            }
        }
    }

    /**
     * Probeer een steen weg te duwen
     * @param go    Gameobject van de player
     */
    private void pushStone(GameObject go) {

        // Steen moet op dezelfde rij zijn om te kunnen duwen
        if (getY() == go.getY()) {
            int stoneIndexX = getIndex(getX());
            int stoneIndexY = getIndex(getY());
            int newIndexX = stoneIndexX;

            // Check of de steen geduwd kan worden
            switch((int)go.getDirection()) {
                case 90:
                    // Duw naar rechts
                    newIndexX = pushRight(stoneIndexX, stoneIndexY);
                    break;
                case 270:
                    // Check of positie rechts van de steen vrij is
                    newIndexX = pushLeft(stoneIndexX, stoneIndexY);
                    break;
            }

            if (newIndexX == stoneIndexX) {
                if (go.getX() < getX()) {
                    go.setX(go.getX() - go.getSpeed());
                } else if(go.getX() > getX()) {
                    go.setX(go.getX() + go.getSpeed());
                }
            }

            // Stel nieuwe locatie in
            setX(newIndexX * Constants.TILESIZE);
            setSpeed(0);
        }
    }

    /**
     * Duw steen naar rechts als er plaats is
     * @param stoneIndexX   De huidige X index van de steen
     * @param stoneIndexY   De huidige Y index van de steen
     * @return              De nieuwe index waar de steen terecht komt
     */
    private int pushRight(int stoneIndexX, int stoneIndexY) {
        int checkX = stoneIndexX + 1;
        if (emptyTile(checkX, stoneIndexY)) {

            stoneIndexX++;
            return stoneIndexX;
        }
        return stoneIndexX;
    }

    /**
     * Duw steen naar links als er plaats is
     * @param stoneIndexX   De huidige X index van de steen
     * @param stoneIndexY   De huidige Y index van de steen
     * @return              De nieuwe index waar de steen terecht komt
     */
    private int pushLeft(int stoneIndexX, int stoneIndexY) {
        int checkX = stoneIndexX - 1;
        if (emptyTile(checkX, stoneIndexY)) {

            stoneIndexX--;
           return stoneIndexX;
        }
        return stoneIndexX;
    }
}