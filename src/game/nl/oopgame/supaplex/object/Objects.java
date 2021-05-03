/**
 * Parent class voor objecten
 * Hier worden de methoden gemaakt voor de gemeenschappelijke gedragingen van de verschillende objecten
 * Zoals vallen en omvallen
 */

package nl.oopgame.supaplex.object;

import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;
import nl.han.ica.oopg.tile.TileMap;
import nl.oopgame.supaplex.*;
import processing.core.PVector;

import javax.activation.CommandObject;
import java.util.Vector;
import java.util.stream.IntStream;

public abstract class Objects extends SpriteObject implements IFallingObject, ITippingObject {

    protected SupaplexApp world;
    public boolean isFalling;
    protected int tipTargetX;
    protected int tipDirection;
    protected TileMap tileMap;
    protected int[][] map;

    /**
     * Create a new SpriteObject with a Sprite object.
     *
     * @param sprite The sprite
     */
    public Objects(Sprite sprite, SupaplexApp world) {
        super(sprite);
        this.world = world;
        setDirection(Directions.DOWN);
        this.isFalling = false;
        this.tipDirection = Directions.UP;
        this.tileMap = this.world.getTileMap();
        this.map = tileMap.getTileMap();
    }

    @Override
    public abstract void update();

    /**
     * Krijg de index waarde uit de huidige positie
     * @param currentPosition   de huidige positie van de schaar
     * @return                  de bijbehorende index in de tilemap
     */
    protected int getIndex(float currentPosition) {
        return ((int)currentPosition + Constants.SPEED) / Constants.TILESIZE;
    }

    /**
     * Controleer of de steen kan vallen
     * @return      True als de steen kan vallen
     */
    @Override
    public boolean canFall() {
        int x = getIndex(getX());
        int y = getIndex(getY() - Constants.SPEED);

        if (x > map[0].length || y == map.length) {
            return false;
        }
        return emptyTile(x, y + 1) && !isPlayerMovingUnderObject() && !isObjectUnderObject();
    }

    /**
     * Checkt of er een object de val van dit object blokkeert
     * @return  True als er een object onder dit object staat
     */
    @Override
    public boolean isObjectUnderObject() {

        // Verzamel alle objecten
        Vector<GameObject> objects = this.world.getGameObjectItems();
        for(GameObject go : objects) {

            // Check of er een object in dezelfde kolom staat
            if ((go instanceof Stone || go instanceof Infotron) && go.getX() == this.getX()) {

                // Als het verschil tussen de steen in de loop en deze steen niet groter is dan een tile, dan is de steen geblokeerd
                if (go.getY() - this.getY() <= Constants.TILESIZE && this.getY() < go.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Controleer of de speler zich onder een steen gaat bewegen om het vallen tegen te houden
     * @param objectX    De X index coordinaat van de steen
     * @param objectY    De Y index coordinaat van de steen
     * @return          True als de speler de steen tegenhoudt
     */
    @Override
    public boolean isPlayerMovingUnderObject() {

        Player player = this.world.getPlayer();

        // Controleer of de speler naar de steen beweegt
        return playerLeftFromObject(player) || playerRightFromObject(player) || playerUnderObject(player) || playerDownToObject(player);
    }

    /**
     * Controleer of deze steen kan omvallen
     * @return      True als de steen kan omvallen
     */
    @Override
    public void checkTipOver() {
        // Verzamel alle objecten
        Vector<GameObject> objects = world.getGameObjectItems();
        for(GameObject go : objects) {

            // Check of er een steen in direct onder deze steen staat
            if ((go instanceof Stone || go instanceof Infotron) && go.getY() == this.getY() + Constants.TILESIZE && go.getX() == this.getX()) {

                // Stel target posities in

                // Tile target posities (indexes)
                int targetLeftX = this.getIndex(getX()) - 1;
                int targetRightX = this.getIndex(getX()) + 1;
                int thisY = this.getIndex(getY());
                int targetY = this.getIndex(getY()) + 1;

                // Object target posities
                int posXLeft = (int)this.getX() - Constants.TILESIZE;
                int posXRight = (int)this.getX() + Constants.TILESIZE;
                int posYThis = (int)this.getY();
                int posYDown = (int)this.getY() + Constants.TILESIZE;

                // Controleer of de steen kan omvallen
                if (checkTipOverTiles(targetLeftX, thisY, targetY) && checkTipOverObjects(posXLeft, posYThis, posYDown)) {
                    this.tipDirection = Directions.LEFT;
                    this.tipTargetX = (int)this.getX() - Constants.TILESIZE;
                } else if (checkTipOverTiles(targetRightX, thisY, targetY) && checkTipOverObjects(posXRight, posYThis, posYDown)) {
                    this.tipDirection = Directions.RIGHT;
                    this.tipTargetX = (int)this.getX() + Constants.TILESIZE;
                }
            }
        }
    }

    /**
     * Controleer of de ruimte links en linksonder de steen vrij zijn om te vallen
     * @return boolean      True als er geen tile staan
     */
    @Override
    public boolean checkTipOverTiles(int targetX, int thisY, int targetY) {
        return emptyTile(targetX, thisY) && emptyTile(targetX, targetY);
    }

    /**
     * Controleer of er ruimte is om de steen te laten omvallen
     * @param targetX   De kolom naast deze steen
     * @param thisY     De huidige rij van deze steen
     * @param targetY   De rij onder deze steen
     * @return          True als er ruimte is om om te vallen
     */
    @Override
    public boolean checkTipOverObjects(int targetX, int thisY, int targetY) {

        // Verzamel alle objecten
        Vector<GameObject> objects = world.getGameObjectItems();
        for(GameObject go : objects) {

            int playerDir = 0;
            if (go instanceof Player) {
                playerDir = (int)go.getDirection();
            }

            boolean inHorizontalBlockRange;
            if (targetX < go.getX()) {
                inHorizontalBlockRange = go.getX() > targetX - Constants.TILESIZE && go.getX() <= targetX && playerDir != Directions.RIGHT;
            } else {
                inHorizontalBlockRange = go.getX() < targetX + Constants.TILESIZE && go.getX() >= targetX && playerDir != Directions.LEFT;
            }
            boolean inVerticalBlockRange = go.getY() >= thisY && go.getY() <= targetY;

            if (inHorizontalBlockRange && inVerticalBlockRange) {
                return false;
            }
        }

        return true;
    }

    /**
     * Laat de steen omvallen
     */
    @Override
    public void tipOver() {
        if (this.tipDirection != 0 && this.getX() != this.tipTargetX) {
            this.setDirection(this.tipDirection);
            this.setSpeed(Constants.SPEED);
        }
    }

    /**
     * Reset wanneer de steen volledig is omgevallen
     */
    @Override
    public void resetTipOver() {
        if (this.tipDirection != 0 && this.getX() == this.tipTargetX) {
            this.tipDirection = 0;
            this.setSpeed(0);
        }
    }

    /**
     * Controleer of een veld niet bezet is. Filter de objecttiles hier uit
     * @param tileX     De X coordinaat van de tile om te controleren
     * @param tileY     De Y coordineet van de tile om te controleren
     * @return          True als er geen tile is
     */
    protected boolean emptyTile(int tileX, int tileY) {
        int[] a = {-1,7,8,9};
        int match = map[tileY][tileX];

        return IntStream.of(a).anyMatch(x -> x == match);
    }

    /**
     * Controleer of de speler links van het object staat en er naartoe beweegt
     * @param player        Het speler object
     * @return              True als de speler van links naar het object beweegt
     */
    private boolean playerLeftFromObject(Player player) {
        PVector playerVector = getPosFromObject(player);
        PVector objectVector = getPosFromObject(this);

        boolean inHorizontalRange = playerVector.x >= objectVector.x - Constants.TILESIZE && playerVector.x < objectVector.x;
        boolean inVerticalRange = playerVector.y >= objectVector.y + Constants.TILESIZE;
        boolean movingToStone = player.getSpeed() > 0;
        boolean movingDirection = player.getDirection() == Directions.RIGHT || player.getDirection() == Directions.LEFT;
        return inHorizontalRange && inVerticalRange && movingToStone && movingDirection;
    }

    /**
     * Controleer of de speler rechts van het object staat en er naartoe beweegt
     * @param player        Het speler object
     * @return              True als de speler van rechts naar het object beweegt
     */
    private boolean playerRightFromObject(Player player) {
        PVector playerVector = getPosFromObject(player);
        PVector objectVector = getPosFromObject(this);

        boolean inHorizontalRange = playerVector.x <= objectVector.x + Constants.TILESIZE && playerVector.x > objectVector.x;
        boolean inVerticalRange = playerVector.y <= objectVector.y + Constants.TILESIZE;
        boolean movingToStone = player.getSpeed() > 0;
        boolean movingDirection = player.getDirection() == Directions.RIGHT || player.getDirection() == Directions.LEFT || player.getDirection() == Directions.UP;
        return inHorizontalRange && inVerticalRange && movingToStone && movingDirection;
    }

    /**
     * Controleer of de speler direct onder het object staat
     * @param player        Het speler object
     * @return              True als de speler direct onder het object staat
     */
    private boolean playerUnderObject(Player player) {
        PVector playerVector = getPosFromObject(player);
        PVector objectVector = getPosFromObject(this);

        boolean inHorizontalRange = playerVector.x == objectVector.x;
        boolean inVerticalRange = playerVector.y == objectVector.y + Constants.TILESIZE;
        return inHorizontalRange && inVerticalRange;
    }

    /**
     * Controleer of de speler van onder het object naar het object toe beweegt
     * 2X tilesize is nodig omdat de eerste optelling van de tilesize met de onderkant van het huidige object correspondeert
     * @param player        Het speler object
     * @return              True als de speler van onder het object naar het object toe beweegt
     */
    private boolean playerDownToObject(Player player) {
        PVector playerVector = getPosFromObject(player);
        PVector objectVector = getPosFromObject(this);

        boolean inHorizontalRange = playerVector.x == objectVector.x;
        boolean inVerticalRange = playerVector.y <= objectVector.y + Constants.TILESIZE * 2 && playerVector.y > objectVector.y + Constants.TILESIZE;
        boolean movingToStone = player.getSpeed() > 0;
        boolean movingDirection = player.getDirection() == 0;
        return inHorizontalRange && inVerticalRange && movingToStone && movingDirection;
    }

    /**
     * Krijg de vectoren van een object
     * @param object    Het object om de positie van te krijgen
     * @return          De X en Y coordinaat van het object in een vector object
     */
    private PVector getPosFromObject(GameObject object) {
        PVector vector = new PVector();
        vector.set(object.getX(), object.getY());
        return vector;
    }

    /**
     * Zorg dat het player object binnen de wereld blijft
     */
    protected void constrainInWorld() {
        // TODO Voeg deze methode toe aan de overige objecten in de update()
        if (getX() <= 0) {
            setxSpeed(Constants.NOSPEED);
            setX(0);
        }
        if (getY() <= 0) {
            setySpeed(Constants.NOSPEED);
            setY(0);
        }
        if (getX() >= world.width - Constants.TILESIZE) {
            setxSpeed(Constants.NOSPEED);
            setX(world.width - Constants.TILESIZE);
        }
        if (getY() >= world.height - Constants.TILESIZE) {
            setySpeed(Constants.NOSPEED);
            setY(world.height - Constants.TILESIZE);
        }
    }
}
