package nl.han.ica.oopg.collision;

import java.util.List;

/**
 * Make your nl.oopgame.supaplex.GameObject implement this interface if you want your nl.oopgame.supaplex.GameObject to
 * be collidable with Tiles.
 */
public interface ICollidableWithTiles {

    /**
     * This method will be triggered when a nl.oopgame.supaplex.GameObject has collided with Tiles.
     *
     * @param collidedTiles The tiles with which a collision should be detected
     */
    void tileCollisionOccurred(List<CollidedTile> collidedTiles);
}
