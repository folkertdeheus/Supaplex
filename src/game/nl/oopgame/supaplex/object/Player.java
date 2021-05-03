/**
 * Class van de speler
 * Hier worden alle bewegingen van de speler afgehandeld
 */

package nl.oopgame.supaplex.object;

import nl.han.ica.oopg.collision.*;
import nl.han.ica.oopg.objects.*;
import nl.han.ica.oopg.collision.CollidedTile;
import nl.han.ica.oopg.exceptions.TileNotFoundException;
import nl.han.ica.oopg.objects.Sprite;
import nl.oopgame.supaplex.*;
import nl.oopgame.supaplex.enemy.Scissors;
import nl.oopgame.supaplex.helper.KeyTracker;
import nl.oopgame.supaplex.tile.*;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.awt.event.KeyEvent.*;

public class Player extends Objects implements ICollidableWithTiles, ICollidableWithGameObjects {

    private ArrayList<KeyTracker> keyTracker = new ArrayList<>();

    public Player(Sprite sprite, SupaplexApp world) {
        super(sprite, world);
        keyTracker.add(new KeyTracker(VK_SPACE));
        keyTracker.add(new KeyTracker(VK_UP));
        keyTracker.add(new KeyTracker(VK_DOWN));
        keyTracker.add(new KeyTracker(VK_LEFT));
        keyTracker.add(new KeyTracker(VK_RIGHT));
    }

    /**
     * Werk de speler elk frame van het spel bij
     */
    @Override
    public void update() {

        // Zorg dat de speler in de wereld blijft
        constrainInWorld();

        // Controleer of een move klaar is
        moveFinished();

        // Beweeg het player object
        movePlayer();
    }

    /**
     * Override op de GameObjects keyPressed methode
     * Voeg de toetsaanslag toe aan de ingedrukte toetsen
     * @param keyCode
     * @param key
     */
    @Override
    public void keyPressed(int keyCode, char key) {

        // Doe pas een volgende zet als de vorige is afgemaakt
        if (getX() % Constants.TILESIZE == 0 && getY() % Constants.TILESIZE == 0) {

            for(KeyTracker keys : keyTracker) {
                if (keys.getKey() == keyCode) {
                    keys.setPressed(true);
                }
            }
        }
    }

    /**
     * Verwijder de toetsaanslag bij de ingedrukte toetsen
     * @param keyCode
     * @param key
     */
    public void keyReleased(int keyCode, char key) {
        for(KeyTracker keys : keyTracker) {
            if (keys.getKey() == keyCode) {
                keys.setPressed(false);
            }
        }
    }

    /**
     * Afhandeling van alle botsingen met tiles
     * @param collidedTiles The tiles with which a collision should be detected
     */
    @Override
    public void tileCollisionOccurred(List<CollidedTile> collidedTiles) {
        PVector vector;

        for (CollidedTile ct : collidedTiles) {
            if (ct.getTile() instanceof BoardsTile || ct.getTile() instanceof MicrochipsTile) {

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
                        setX(getWidth() + (vector.x));
                    } catch (TileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (CollisionSide.LEFT.equals(ct.getCollisionSide())) {
                        try {
                            vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                            setX((vector.x) - getWidth());
                        } catch (TileNotFoundException e) {
                            e.printStackTrace();
                        }
                }
            }

            if (ct.getTile() instanceof CircuitsTile) {
                try {
                    vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                    world.getTileMap().setTile((int) vector.x / 50, (int) vector.y / 50, -1);
                } catch (TileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (ct.getTile() instanceof EndTile) {
                try {
                  if(SupaplexApp.getSelectedLevel().getCollectedInfotrons() >= SupaplexApp.getSelectedLevel().getInfotronsNeeded()){
                       world.nextLvl();
                  }
                } catch (TileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Houdt de collisions met de game objecten bij
     * @param collidedGameObjects The GameObjects with which a collision should be detected
     */
    @Override
    public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
        for (GameObject go : collidedGameObjects) {
            if(go instanceof Scissors){
                world.gameOver(false);
            }

            if (go instanceof Infotron) {
                if (((Infotron) go).isFalling) {
                    world.gameOver(false);
                } else {
                    if (checkMovementPosition(go)) {
                        world.deleteGameObject(go);
                        world.getSelectedLevel().increaseCollectedInfotrons();
                        world.getScoreBoard().updateScore();
                    }
                }
            }
        }
    }

    /**
     * Controleert of de speler de goede positie en richting heeft om een infotron op te pakken
     * @param go        Game Object van de speler
     * @return          True als de speler een infotron kan oppakken
     */
    private boolean checkMovementPosition(GameObject go) {
        return (getY() == go.getY() && getX() < go.getX() && getDirection() == Directions.RIGHT) ||
                (getY() == go.getY() && getX() > go.getX() && getDirection() == Directions.LEFT) ||
                (getX() == go.getX() && getY() < go.getY() && getDirection() == Directions.DOWN) ||
                (getX() == go.getX() && getY() > go.getY() && getDirection() == Directions.UP);
    }

    /**
     * Controleer of er een hele move gedaan is
     * Zo ja, zet het player object stil
     */
    private void moveFinished() {
        if (getX() % Constants.TILESIZE == 0 && getY() % Constants.TILESIZE == 0) {
            setSpeed(Constants.NOSPEED);
        }
    }

    /**
     * Beweeg het player object
     */
    private void movePlayer() {

        for(KeyTracker keys : keyTracker) {

            if (keys.getKey() == VK_SPACE && keys.getPressed()) {
                setSpeed(Constants.NOSPEED);
                spaceMovePlayer();
                // Break is belangrijk hier, anders worden de andere ingedrukte toetsen alsnog afgehandeld
                break;

            } else if(keys.getKey() == VK_UP && keys.getPressed()) {
                setDirection(Directions.UP);
                setSpeed(Constants.SPEED);

            } else if(keys.getKey() == VK_DOWN && keys.getPressed()) {
                setDirection(Directions.DOWN);
                setSpeed(Constants.SPEED);

            } else if(keys.getKey() == VK_LEFT && keys.getPressed()) {
                setDirection(Directions.LEFT);
                setSpeed(Constants.SPEED);

            } else if(keys.getKey() == VK_RIGHT && keys.getPressed()) {
                setDirection(Directions.RIGHT);
                setSpeed(Constants.SPEED);
            }
        }
    }

    /**
     * Voer de move uit waarbij de spatie wordt gebruikt
     * Verwijderd een naastgelegen tile zonder te bewegen
     */
    private void spaceMovePlayer() {
        PVector vector = new PVector();
        vector = setSpaceDirection();

        // Verwijder vak indien mogelijk
        if (vector.x != -1 && vector.y != -1) {
            removeTile((int)vector.x, (int)vector.y);
            removeObject((int)vector.x, (int)vector.y);
        }
    }

    /**
     * Stel de target locatie in voor de spatie+ actie
     * @return      De X en Y locatie in een vector. -1 als er geen locatie is ingesteld
     */
    private PVector setSpaceDirection() {
        int targetX = -1;
        int targetY = -1;

        for(KeyTracker keys : keyTracker) {
            if(keys.getKey() == VK_UP && keys.getPressed()) {
                targetX = (int)getX();
                targetY = (int)getY() - Constants.TILESIZE;

            } else if (keys.getKey() == VK_DOWN && keys.getPressed()) {
                targetX = (int)getX();
                targetY = (int)getY() + Constants.TILESIZE;

            } else if(keys.getKey() == VK_LEFT && keys.getPressed()) {
                targetX = (int)getX() - Constants.TILESIZE;
                targetY = (int)getY();

            } else if(keys.getKey() == VK_RIGHT && keys.getPressed()) {
                targetX = (int)getX() + Constants.TILESIZE;
                targetY = (int)getY();
            }
        }

        PVector vector = new PVector();
        vector.set(targetX, targetY);
        return vector;
    }

    /**
     * Verwijder een circuits tile als deze op de gegeven locatie staat
     * @param posX      De X pixel locatie van de tile
     * @param posY      De Y pixel locatie van de tile
     */
    private void removeTile(int posX, int posY) {
        int indexX = getIndex(posX);
        int indexY = getIndex(posY);

        if (map[indexY][indexX] == 1) {
            world.getTileMap().setTile(indexX, indexY, -1);
        }
    }

    /**
     * Controleer of op de gegeven plek een verwijderbaar object staat
     * @param posX      De X pixel locatie van het object
     * @param posY      De Y pixel locatie van het object
     * @return          True als er een verwijderbaar object op de locatie staat
     */
    private void removeObject(int posX, int posY) {
        GameObject objectToDelete = null;

        for(GameObject go : world.getGameObjectItems()) {
            if (go.getX() == posX && go.getY() == posY && go instanceof Infotron) {
                objectToDelete = go;
            }
        }

        if (objectToDelete != null) {
            world.deleteGameObject(objectToDelete);
            world.getSelectedLevel().increaseCollectedInfotrons();
            world.getScoreBoard().updateScore();
        }
    }
}