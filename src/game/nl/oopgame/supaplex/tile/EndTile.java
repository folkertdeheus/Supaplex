/**
 * Class om de End Tile te maken
 */

package nl.oopgame.supaplex.tile;

import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.tile.Tile;

import java.util.List;

public class EndTile extends Tile {
    /**
     * @param sprite The image which will be drawn whenever the draw method of the tile is called.
     */
    public EndTile(Sprite sprite) {
        super(sprite);
    }
}
