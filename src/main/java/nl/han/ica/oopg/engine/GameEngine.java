package nl.han.ica.oopg.engine;

import ddf.minim.Minim;
import nl.han.ica.oopg.collision.CollisionSide;
import nl.han.ica.oopg.dashboard.Dashboard;
import nl.han.ica.oopg.collision.CollidedTile;
import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.collision.ICollidableWithTiles;
import nl.han.ica.oopg.dashboard.FPSCounter;
import nl.han.ica.oopg.exceptions.GameEngineRuntimeException;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.tile.Tile;
import nl.han.ica.oopg.userinput.IKeyInput;
import nl.han.ica.oopg.userinput.IMouseInput;
import nl.han.ica.oopg.view.View;
import nl.han.ica.oopg.tile.TileMap;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.awt.*;
import java.util.Vector;

/**
 * GameEngine is the core of the game. Extending this class is required to make use of the GameEngine.
 * <p>
 * This engine is created by: Bram Heijmink, Jeffrey Haen, Joost Elshof, Kenny Ligthart, Mark Vaesen and Nico Smolders.
 */
public abstract class GameEngine extends PApplet {

    /**
     * A vectorlist that holds all GameObjects.
     */
    private Vector<GameObject> gameObjects = new Vector<>();

    /**
     * A vectorlist that holds all Dashboards, which are of type nl.oopgame.supaplex.GameObject.
     */
    private Vector<Dashboard> dashboards = new Vector<>();

    /**
     * Instance of TileMap.
     */
    protected TileMap tileMap;

    /**
     * Thread is used to keep the update method running.
     */
    private GameThread gameThread = new GameThread(this);

    /**
     * Creates an instance of minim that helps with loading music in the sound class.
     */
    public Minim soundLibrary = new Minim(this);

    /**
     * The view is the main canvas that is been drawn by the GameEngine.
     */
    private View view;

    /**
     * The FPSCounter shows the Frames Per Second on the screen.
     */
    private FPSCounter fpsCounter;

    private static GameEngine engine;

    /**
     * Creates a new GameEngine object, use a static main method and implement the following:
     * PApplet.main(new String[]{"{YOUR.PACKAGENAME}.{YOUR.CLASSNAME}"});
     */
    public GameEngine() {
        GameEngine.engine = this;
    }

    /**
     * The actual setup of the game-engine.
     * Used to make sure the gameThread is started after setup has finished.
     * This method is invoked by processing.
     */
    public void setup() {
        setupGame();
        gameThread.start();
    }

    /**
     * Implement this method to setup your game.
     */
    public abstract void setupGame();

    /**
     * Sets the view which will be drawn by the GameEngine.
     *
     * @param view The view
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Gets the view which is drawn by the GameEngine.
     *
     * @return The view
     */
    public View getView() {
        return view;
    }

    /**
     * Add a nl.oopgame.supaplex.GameObject to the GameEngine.
     *
     * @param gameObject The nl.oopgame.supaplex.GameObject that will be added to the game. Should have either
     *                   nl.oopgame.supaplex.GameObject or MovableGameObject as it's parent.
     */
    public void addGameObject(GameObject gameObject) {
        if (!gameObjects.contains(gameObject)) {
        	gameObjects.add(gameObject);
        } else {
        	throw new GameEngineRuntimeException("The same object is added twice. This is not allowed for performance reasons. Please fix your code (do NOT catch this exception).");
        }
    }

    /**
     * Add a dashboard to the GameEngine.
     *
     * @param dashboard The dashboard that should be added
     */
    public void addDashboard(Dashboard dashboard) {
        dashboards.add(dashboard);
    }

    /**
     * Add a nl.oopgame.supaplex.GameObject to the GameEngine.
     *
     * @param gameObject The gameObject that should be added
     * @param x          The x-coordinate on which the gameobject should be added
     * @param y          The y-coordinate on which the gameobject should be added
     */
    public void addGameObject(GameObject gameObject, float x, float y) {
        addGameObject(gameObject);

        gameObject.setX(x);
        gameObject.setY(y);
    }

    /**
     * Add a dashboard to te GameEngine.
     *
     * @param dashboard The dashboard that should be added
     * @param x         The x-coordinate on which the dashboard should be added
     * @param y         The y-coordinate on which the dashboard should be added
     */
    public void addDashboard(Dashboard dashboard, float x, float y) {
        addDashboard(dashboard);

        dashboard.setX(x);
        dashboard.setY(y);
    }

    /**
     * Add a nl.oopgame.supaplex.GameObject to the GameEngine.
     *
     * @param gameObject    The gameObject that should be added
     * @param x             The x-coordinate on which the gameobject should be added
     * @param y             The y-coordinate on which the gameobject should be added
     * @param layerposition The layerposition on which the gameobject should be added
     */
    public void addGameObject(GameObject gameObject, float x, float y, float layerposition) {
        addGameObject(gameObject);

        gameObject.setX(x);
        gameObject.setY(y);
        gameObject.setZ(layerposition);
    }

    /**
     * Add a dashboard to the GameEngine.
     *
     * @param dashboard     The dashboard that should be added
     * @param x             The x-coordinate on which the dashboard should be added
     * @param y             The y-coordinate on which the dashboard should be added
     * @param layerposition The layerposition on which the dashboard should be added
     */
    public void addDashboard(Dashboard dashboard, float x, float y, float layerposition) {
        addDashboard(dashboard);

        dashboard.setX(x);
        dashboard.setY(y);
        dashboard.setZ(layerposition);
    }

    /**
     * Add a nl.oopgame.supaplex.GameObject to the GameEngine.
     *
     * @param gameObject    The gameObject that should be added
     * @param layerposition The layerposition on which the gameobject should be added
     */
    public void addGameObject(GameObject gameObject, float layerposition) {
        addGameObject(gameObject);

        gameObject.setZ(layerposition);
    }

    /**
     * Add a dashboard to the GameEngine.
     *
     * @param dashboard     The dashboard that should be added
     * @param layerposition The layerposition on which the dashboard should be added
     */
    public void addDashboard(Dashboard dashboard, float layerposition) {
        addDashboard(dashboard);

        dashboard.setZ(layerposition);
    }

    /**
     * Get a list of all the GameObjects inside the GameEngine.
     *
     * @return All GAmeObjects
     */
    public Vector<GameObject> getGameObjectItems() {
        return gameObjects;
    }

    /**
     * Get a list of all the Dashboards inside the GameEngine.
     *
     * @return All Dashboards
     */
    public Vector<Dashboard> getDashboards() {
        return dashboards;
    }

    /**
     * Delete a nl.oopgame.supaplex.GameObject from the GameEngine.
     *
     * @param gameObject The gameobject that hould be deleted
     */
    public void deleteGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    /**
     * Delete a dashboard from the GameEngine.
     *
     * @param dashboard The dashboard that should be deleted
     */
    public void deleteDashboard(Dashboard dashboard) {
        dashboards.remove(dashboard);
    }

    /**
     * Delete all GameObjects from the GameEngine.
     */
    public void deleteAllGameOBjects() {
        gameObjects.removeAllElements();
    }

    /**
     * Delete all Dashboards from the GameEngine.
     */
    public void deleteAllDashboards() {
        dashboards.removeAllElements();
    }

    /**
     * Delete all GameObjects of a given type from the GameEngine.
     * <p>
     * Example paramater: nl.oopgame.supaplex.Player.class
     *
     * @param type The type of the gameobjects
     * @param <T>  Generic type, should extend nl.oopgame.supaplex.GameObject
     */
    public <T extends GameObject> void deleteAllGameObjectsOfType(Class<T> type) {
        gameObjects.removeIf(p -> type.equals(p.getClass()));
    }

    /**
     * Updates every nl.oopgame.supaplex.GameObject inside the GameEngine.
     * Calls move method and checks for collision.
     * <p>
     * This method is called by the GameThread.
     */
    public void updateGame() {

        updateGameObjects();
        updateDashboards();

        Vector<GameObject> tempCollision = (Vector<GameObject>) gameObjects.clone();
        for (int i = 0; i < tempCollision.size(); i++) {

            if (tempCollision.get(i) instanceof ICollidableWithGameObjects) {
                Vector collidedGameobjects = CollidingHelper.calculateGameObjectCollisions(tempCollision.get(i), tempCollision);
                if (!collidedGameobjects.isEmpty())
                    ((ICollidableWithGameObjects) tempCollision.get(i)).gameObjectCollisionOccurred(collidedGameobjects);
            }
            if (tempCollision.get(i) instanceof ICollidableWithTiles) {
                Vector collidedTiles = CollidingHelper.calculateTileCollision(tempCollision.get(i), tileMap);
                if (!collidedTiles.isEmpty())
                    ((ICollidableWithTiles) tempCollision.get(i)).tileCollisionOccurred(collidedTiles);

            }
        }

        gameObjects.sort((obj1, obj2) -> Float.compare(obj1.getZ(), obj2.getZ())); // Sort the list by Z index...
    }

    /**
     * Updates every dashboard inside the GameEngine.
     */
    private void updateDashboards() {

        for (int i = 0; i < dashboards.size(); i++) {
            dashboards.get(i).update();
        }
    }

    /**
     * Updates every nl.oopgame.supaplex.GameObject inside the GameEngine.
     */
    private void updateGameObjects() {

        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).setySpeed(gameObjects.get(i).getySpeed() + gameObjects.get(i).getGravity());
            gameObjects.get(i).move();
            gameObjects.get(i).update();
        }
    }

    /**
     * Implement this method to make constant updates in your game
     */
    public abstract void update();

    /**
     * Draws the view.
     * <p>
     * (non-Javadoc) This method is used by Processing to draw on the canvas.
     *
     * @see processing.core.PApplet#draw()
     */
    public void draw() {
        view.draw(g, tileMap, gameObjects, dashboards);
    }

    /**
     * Fires a keyPressed event to every nl.oopgame.supaplex.GameObject inside the GameEngine.
     * <p>
     * (non-Javadoc) This event is fired by Processing when registers key input.
     *
     * @see processing.core.PApplet#keyPressed()
     */
    public void keyPressed() {
        for (int i = 0; i < gameObjects.size(); i++) {

            if (gameObjects.get(i) instanceof IKeyInput) {
                ((IKeyInput) gameObjects.get(i)).keyPressed(keyCode, key);
            }
        }
    }

    /**
     * Fires a keyReleased event to every nl.oopgame.supaplex.GameObject inside the GameEngine.
     * <p>
     * (non-Javadoc) This event is fired by Processing when registers key release.
     *
     * @see processing.core.PApplet#keyReleased()
     */
    public void keyReleased() {
        for (int i = 0; i < gameObjects.size(); i++) {

            if (gameObjects.get(i) instanceof IKeyInput) {
                ((IKeyInput) gameObjects.get(i)).keyReleased(keyCode, key);
            }
        }
    }

    /**
     * Fires a mousePressed event to every nl.oopgame.supaplex.GameObject inside the GameEngine.
     * <p>
     * (non-Javadoc)
     *
     * @see processing.core.PApplet#mousePressed()
     */
    public void mousePressed() {

        PVector location = calculateRelativeMouseLocation(mouseX, mouseY);

        for (int i = 0; i < gameObjects.size(); i++) {

            if (gameObjects.get(i) instanceof IMouseInput) {
                ((IMouseInput) gameObjects.get(i)).mousePressed((int) location.x, (int) location.y, mouseButton);
            }
        }
    }

    /**
     * Fires a mouseReleased event to every nl.oopgame.supaplex.GameObject inside the GameEngine.
     * <p>
     * (non-Javadoc)
     *
     * @see processing.core.PApplet#mouseReleased()
     */
    public void mouseReleased() {

        PVector location = calculateRelativeMouseLocation(mouseX, mouseY);

        for (int i = 0; i < gameObjects.size(); i++) {

            if (gameObjects.get(i) instanceof IMouseInput) {
                ((IMouseInput) gameObjects.get(i)).mouseReleased((int) location.x, (int) location.y, mouseButton);
            }
        }
    }

    /**
     * Fires a mouseClicked event to every nl.oopgame.supaplex.GameObject inside the GameEngine.
     * <p>
     * (non-Javadoc)
     *
     * @see processing.core.PApplet#mouseClicked()
     */
    public void mouseClicked() {

        PVector location = calculateRelativeMouseLocation(mouseX, mouseY);

        for (int i = 0; i < gameObjects.size(); i++) {

            if (gameObjects.get(i) instanceof IMouseInput) {
                ((IMouseInput) gameObjects.get(i)).mouseClicked((int) location.x, (int) location.y, mouseButton);
            }
        }
    }

    /**
     * Fires a mouseMoved event to every nl.oopgame.supaplex.GameObject inside the GameEngine.
     * <p>
     * (non-Javadoc)
     *
     * @see processing.core.PApplet#mouseMoved()
     */
    public void mouseMoved() {

        PVector location = calculateRelativeMouseLocation(mouseX, mouseY);

        for (int i = 0; i < gameObjects.size(); i++) {

            if (gameObjects.get(i) instanceof IMouseInput) {
                ((IMouseInput) gameObjects.get(i)).mouseMoved((int) location.x, (int) location.y);
            }
        }
    }

    /**
     * Fires a mouseDragged event to every nl.oopgame.supaplex.GameObject inside the GameEngine.
     * <p>
     * (non-Javadoc)
     *
     * @see processing.core.PApplet#mouseDragged()
     */
    public void mouseDragged() {

        PVector location = calculateRelativeMouseLocation(mouseX, mouseY);

        for (int i = 0; i < gameObjects.size(); i++) {

            if (gameObjects.get(i) instanceof IMouseInput) {
                ((IMouseInput) gameObjects.get(i)).mouseDragged((int) location.x, (int) location.y, mouseButton);
            }
        }
    }

    /**
     * Fires a mouseWheel event to every nl.oopgame.supaplex.GameObject inside the GameEngine.
     * <p>
     * (non-Javadoc)
     *
     * @see processing.core.PApplet#mouseWheel(processing.event.MouseEvent)
     */
    public void mouseWheel(MouseEvent event) {

        for (int i = 0; i < gameObjects.size(); i++) {

            if (gameObjects.get(i) instanceof IMouseInput) {
                ((IMouseInput) gameObjects.get(i)).mouseWheel((int) mouseEvent.getAmount());
            }
        }
    }

    /**
     * Sets the amount of updates per second for the GameThread.
     *
     * @param updatesPerSecond The number of udates per second
     * @throws IllegalArgumentException if the updatesPerSecond is smaller than 1
     */
    public void setGameSpeed(int updatesPerSecond) throws IllegalArgumentException {

        if (updatesPerSecond < 1)
            throw new IllegalArgumentException("Updates per second must be equal or higher than 1");
        else
            gameThread.setGameSpeed(updatesPerSecond);
    }

    /**
     * Gets and calculates the relative mouse position with the location of world (TileMap).
     * A negative value means the mouse position is detected outside the world map.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return PVector
     */
    public PVector calculateRelativeMouseLocation(int x, int y) {

        x += view.getViewport().getX();
        y += view.getViewport().getY();

        return new PVector(x, y);
    }

    /**
     * Gets the amount of updates per second from the GameThread.
     *
     * @return int
     */
    public int getGameSpeed() {
        return (int) gameThread.getGameSpeed();
    }

    /**
     * Pauses the Game.
     */
    public void pauseGame() {
        gameThread.pauseGame();
    }

    /**
     * Resumes the Game.
     */
    public void resumeGame() {
        gameThread.resumeGame();
    }

    /**
     * Returns if the game thread is paused or not
     *
     * @return isGamePaused or !isGamePaused
     */
    public boolean getThreadState() {
        return gameThread.getThreadState();
    }

    /**
     * Sets the TileMap which will be drawn by the view.
     *
     * @param tileMap The tilemap to be set
     */
    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    /**
     * Gets the TileMap which is drawn by the view.
     *
     * @return TileMap  The tilemap
     */
    public TileMap getTileMap() {
        return tileMap;
    }

    /**
     * Creates a new PGraphics (canvas) object which can only made by the PApplet. (this method is used by the GameEngine)
     *
     * @param width  The width of the PGraphics
     * @param height The height of the PGraphics
     * @return PGraphics
     */
    public static PGraphics createPGraphics(int width, int height) {
        return engine.createGraphics(width, height);
    }

    private static class CollidingHelper {

        private static int topTileSide = 0;
        private static int rightTileSide = 1;
        private static int bottomTileSide = 2;
        private static int leftTileSide = 3;
        private static int insideTile = 4;

        private CollidingHelper() {

        }

        private static Vector<GameObject> calculateGameObjectCollisions(GameObject gameObject, Vector<GameObject> allGameObjects) {
            Vector<GameObject> collidedObjects = new Vector<>();

            Rectangle gameObjectRectangle = new Rectangle((int) gameObject.getX(), (int) gameObject.getY(),
                    (int) gameObject.getWidth(), (int) gameObject.getHeight());
            Rectangle otherGameObjectRectangle;

            for (int i = 0; i < allGameObjects.size(); i++) {
                if (!allGameObjects.get(i).equals(gameObject)) {
                    otherGameObjectRectangle = new Rectangle((int) allGameObjects.get(i).getX(), (int) allGameObjects.get(i).getY(),
                            (int) allGameObjects.get(i).getWidth(), (int) allGameObjects.get(i).getHeight());
                    collidedObjects.addAll(checkCollisionBetweenObjects(gameObject, allGameObjects.get(i), gameObjectRectangle, otherGameObjectRectangle));
                }
            }

            if (!collidedObjects.isEmpty()) {
                return collidedObjects;
            }

            return new Vector<GameObject>();
        }

        private static Vector<CollidedTile> calculateTileCollision(GameObject gameObject, TileMap tileMap) {
            Vector<CollidedTile> collidedTiles = new Vector<>();

            Rectangle gameObjectRectangle = new Rectangle((int) gameObject.getX(), (int) gameObject.getY(),
                    (int) gameObject.getWidth(), (int) gameObject.getHeight());
            Rectangle tileRectangle;

            for (int i = 0; i < tileMap.getTileMap().length; i++) {
                for (int j = 0; j < tileMap.getTileMap()[i].length; j++) {
                    if (!tilemapIndexHasEmptyTile(tileMap, i, j)) {
                        tileRectangle = new Rectangle(j * tileMap.getTileSize(), i * tileMap.getTileSize(),
                                tileMap.getTileSize(), tileMap.getTileSize());
                        collidedTiles.addAll(checkCollisionBetweenTileAndObject(gameObject, tileMap.getTileOnIndex(j, i), tileRectangle, gameObjectRectangle));
                    }
                }
            }

            if (!collidedTiles.isEmpty()) {
                return collidedTiles;
            }

            return new Vector<>();
        }

        private static boolean tilemapIndexHasEmptyTile(TileMap tileMap, int i, int j) {
            return (tileMap.findTileTypeIndex(tileMap.getTileOnIndex(j, i))) == -1;
        }

        private static boolean objectIsMoving(GameObject gameObject) {
            return (int) gameObject.getxSpeed() != 0 || (int) gameObject.getySpeed() != 0;
        }

        private static double lerp(float x1, float x2, double t) {
            return x1 + (x2 - x1) * t;
        }


        private static CollisionSide getCollidedTileSide(Rectangle gameObject, Rectangle tile) {
            int centerTileX = (int) tile.getX() + ((int) tile.getWidth() / 2);
            int centerTileY = (int) tile.getY() + ((int) tile.getHeight() / 2);

            int checkpointObjectX = getGameObjectXClosestToTileCenterX(gameObject, tile);
            int checkpointObjectY = getGameObjectYClosestToTileCenterY(gameObject, tile);

            float angleGameObjectToTile = getAngleBetweenPoints(checkpointObjectX, checkpointObjectY, centerTileX, centerTileY);

            return getCollidedTileSide(gameObject, centerTileX, centerTileY, angleGameObjectToTile);
        }

        private static CollisionSide getCollidedTileSide(Rectangle gameObject, int centerTileX, int centerTileY, float angleGameObjectToTile) {
            CollisionSide side = CollisionSide.INSIDE;

            if (!gameObject.contains(centerTileX, centerTileY)) {
                switch (calculateCollidedTileSide(Math.round(angleGameObjectToTile))) {
                    case 0:
                        side = CollisionSide.TOP;
                        break;
                    case 1:
                        side = CollisionSide.RIGHT;
                        break;
                    case 2:
                        side = CollisionSide.BOTTOM;
                        break;
                    case 3:
                        side = CollisionSide.LEFT;
                        break;
                }
            }
            return side;
        }


        private static int getGameObjectXClosestToTileCenterX(Rectangle gameObject, Rectangle tile) {
            int centerTileX = (int) tile.getX() + ((int) tile.getWidth() / 2);

            if (centerTileX >= gameObject.getX() + gameObject.getWidth())
                return (int) gameObject.getX() + ((int) gameObject.getWidth());
            else if ((centerTileX < gameObject.getX() + gameObject.getWidth()) && (gameObject.getX() <= centerTileX))
                return centerTileX;
            else if (gameObject.getX() > centerTileX)
                return (int) gameObject.getX();

            return (int) gameObject.getX() + ((int) gameObject.getWidth() / 2);
        }

        private static int getGameObjectYClosestToTileCenterY(Rectangle gameObject, Rectangle tile) {
            int centerTileY = (int) tile.getY() + ((int) tile.getHeight() / 2);

            if (centerTileY >= gameObject.getY() + gameObject.getHeight())
                return (int) gameObject.getY() + ((int) gameObject.getHeight());
            else if ((centerTileY < gameObject.getY() + gameObject.getHeight()) && (gameObject.getY() <= centerTileY))
                return centerTileY;
            else if (gameObject.getY() > centerTileY)
                return (int) gameObject.getY();

            return (int) gameObject.getY() + ((int) gameObject.getHeight() / 2);
        }

        private static int calculateCollidedTileSide(int angleGameObjectToTile) {

            if (angleGameObjectToTile >= 136 && angleGameObjectToTile <= 225)
                return topTileSide;

            else if (angleGameObjectToTile >= 226 && angleGameObjectToTile <= 315)
                return rightTileSide;

            else if ((angleGameObjectToTile >= 316 && angleGameObjectToTile <= 360) || angleGameObjectToTile >= 0 && angleGameObjectToTile <= 45)
                return bottomTileSide;

            else if (angleGameObjectToTile >= 46 && angleGameObjectToTile <= 135)
                return leftTileSide;

            else
                return -1;
        }

        private static float getAngleBetweenPoints(int point1X, int point1Y, int point2X, int point2Y) {
            float dx = point2X - point1X;
            float dy = point2Y - point1Y;

            if (dx >= 0 || dy >= 0)
                return (float) Math.toDegrees(Math.atan2(dy, dx)) + 90;
            else
                return (float) Math.toDegrees(Math.atan2(dy, dx)) + 450;
        }

        private static Vector checkCollisionBetweenTileAndObject(GameObject gameObject, Tile tile, Rectangle collidingTile, Rectangle collidingGameobject) {
            Vector objects = new Vector();

            if (objectIsMoving(gameObject)) {

                boolean collided = false;

                for (int t = 0; t <= gameObject.getSpeed() && !collided; t++) {
                    double xPos = lerp(gameObject.getPrevX(), gameObject.getX(), t / gameObject.getSpeed());
                    double yPos = lerp(gameObject.getPrevY(), gameObject.getY(), t / gameObject.getSpeed());
                    collidingGameobject.setLocation((int) xPos, (int) yPos);

                    if (collidingGameobject.intersects(collidingTile)) {

                        objects.add(new CollidedTile(tile, getCollidedTileSide(collidingGameobject, collidingTile)));
                        CollisionSide tileSide = getCollidedTileSide(collidingGameobject, collidingTile);
                        collided = true;
                    }
                }
            } else {
                if (collidingGameobject.intersects(collidingTile))
                    objects.add(new CollidedTile(tile, getCollidedTileSide(collidingGameobject, collidingTile)));
            }

            return objects;
        }

        private static Vector checkCollisionBetweenObjects(GameObject gameObject, GameObject otherGameObject, Rectangle gameObjectRectangle, Rectangle otherGameObjectRectangle) {
            Vector objects = new Vector();

            if (objectIsMoving(gameObject)) {

                boolean collided = false;

                for (int t = 0; t <= gameObject.getSpeed() && !collided; t++) {
                    double x = lerp(gameObject.getPrevX(), gameObject.getX(), t / gameObject.getSpeed());
                    double y = lerp(gameObject.getPrevY(), gameObject.getY(), t / gameObject.getSpeed());
                    gameObjectRectangle.setLocation((int) x, (int) y);
                    if (gameObjectRectangle.intersects(otherGameObjectRectangle)) {
                        objects.add(otherGameObject);
                        collided = true;
                    }
                }
            } else {
                if (gameObjectRectangle.intersects(otherGameObjectRectangle))
                    objects.add(otherGameObject);
            }

            return objects;
        }
    }

    /**
     * Sets whether the FPSCounter has to be shown or not.
     *
     * @param status True if the FPSCounter should be shown
     */
    public void setFPSCounter(boolean status) {
        if (status && fpsCounter == null) {
            fpsCounter = new FPSCounter(0, 12);
            addDashboard(fpsCounter);
        } else if (!status) {
            dashboards.remove(fpsCounter);
            fpsCounter = null;
        }
    }
}
