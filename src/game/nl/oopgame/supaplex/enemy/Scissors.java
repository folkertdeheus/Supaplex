/**
 * Class die het Scissors Object maakt
 * De bewegingen en tile collisions worden hier bijgehouden
 * Collisions met het Player object en Stone object worden in die classes bijgehouden
 */

package nl.oopgame.supaplex.enemy;

import nl.han.ica.oopg.collision.CollidedTile;
import nl.han.ica.oopg.collision.CollisionSide;
import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.collision.ICollidableWithTiles;
import nl.han.ica.oopg.exceptions.TileNotFoundException;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.tile.Tile;
import nl.han.ica.oopg.tile.TileMap;
import nl.oopgame.supaplex.Constants;
import nl.oopgame.supaplex.Directions;
import nl.oopgame.supaplex.tile.MicrochipsTile;
import nl.oopgame.supaplex.object.Objects;
import nl.oopgame.supaplex.SupaplexApp;
import nl.oopgame.supaplex.tile.BoardsTile;
import nl.oopgame.supaplex.tile.CircuitsTile;
import processing.core.PVector;

import java.util.List;

public class Scissors extends Objects implements ICollidableWithTiles {

    private int intialDirection = Directions.RIGHT;   // De eerste richting van de schaar

    /**
     * Create a new SpriteObject with a Sprite object.
     * @param sprite The sprite
     */
    public Scissors(Sprite sprite, SupaplexApp world) {
        super(sprite, world);
        setSpeed(Constants.SPEED);
        setDirection(this.intialDirection);
    }

    @Override
    public void update() {
        this.tryToTurnLeft();
    }

    /**
     * Probeer om linksaf te slaan. Doe dit aan de hand van de huidige positie en de locatie in de tilemap
     */
    private void tryToTurnLeft() {
        TileMap tileMap = this.world.getTileMap();
        int[][] map = tileMap.getTileMap();

        int x = getIndex(getX());
        int y = getIndex(getY());

        if (canTurnLeft(map, x, y) && getX() % Constants.TILESIZE == 0 && getY() % Constants.TILESIZE == 0) {
            setDirection(this.turnLeft(getDirection()));
        }
    }

    /**
     * Controleer aan de hand van de huidige positie en richting of de positie links van de schaar vrij is
     * @param map   De tilemap in multidimensionale array
     * @param x     De huidige X positie (index) in de tilemap
     * @param y     De huidige Y positie (index) in de tilemap
     * @return      True of False, afhankelijk van of de positie links vrij is
     */
    private boolean canTurnLeft(int[][] map, int x, int y) {
        switch((int)getDirection()) {
            case 0:
                return map[y][x - 1] == -1;
            case 90:
                return map[y - 1][x] == -1;
            case 180:
                return map[y][x + 1] == -1;
            case 270:
                return map[y + 1][x] == -1;
            default:
                return false;
        }
    }

    @Override
    public void tileCollisionOccurred(List<CollidedTile> collidedTiles) {
        PVector vector;

        for (CollidedTile ct : collidedTiles) {
            Tile tile = ct.getTile();

            if (tile instanceof BoardsTile || tile instanceof CircuitsTile || tile instanceof MicrochipsTile) {

                if (CollisionSide.TOP.equals(ct.getCollisionSide())) {
                    try {
                        vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                        setY(vector.y - getHeight());
                    } catch (TileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (CollisionSide.BOTTOM.equals(ct.getCollisionSide())) {
                    try {
                        vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                        setY(getHeight() + vector.y);
                    } catch (TileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (CollisionSide.RIGHT.equals(ct.getCollisionSide())) {
                    try {
                        vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                        setX(getWidth() + vector.x);
                    } catch (TileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (CollisionSide.LEFT.equals(ct.getCollisionSide())) {
                    try {
                        vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                        setX(vector.x - getWidth());
                    } catch (TileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                // Verander de richting van de schaar als het volgende veld niet vrij is
                setDirection(this.turnRight(getDirection()));
            }
        }
    }

    /**
     * Laat de schaar rechts af slaan
     * @param currentDirection  De huidige richting van de schaar
     * @return                  De nieuwe richting van de schaar
     */
    private float turnRight(float currentDirection) {
        return currentDirection == Directions.LEFT ? Directions.UP : currentDirection + 90;
    }

    /**
     * Laat de schaar links af slaan
     * @param currentDirection  De huidige richting van de schaar
     * @return                  De nieuwe richting van de schaar
     */
    private float turnLeft(float currentDirection) {
        return currentDirection == Directions.UP ? Directions.LEFT : currentDirection - 90;
    }
}