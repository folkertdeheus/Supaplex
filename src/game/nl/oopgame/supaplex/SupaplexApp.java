/**
 * Hoofdclass van de SUPAPLEX APP
 *
 * @author Mustafa Idiz & Folkert de Heus
 * @date 02-05-2021
 */

package nl.oopgame.supaplex;

//<editor-fold desc="imports">
import nl.han.ica.oopg.engine.GameEngine;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.tile.Tile;
import nl.han.ica.oopg.tile.TileMap;
import nl.han.ica.oopg.tile.TileType;
import nl.han.ica.oopg.view.CenterFollowingViewport;
import nl.han.ica.oopg.view.EdgeFollowingViewport;
import nl.han.ica.oopg.view.View;

import nl.oopgame.supaplex.enemy.Scissors;
import nl.oopgame.supaplex.object.Infotron;
import nl.oopgame.supaplex.object.Player;
import nl.oopgame.supaplex.object.Stone;
import nl.oopgame.supaplex.screen.GameEndScreen;
import nl.oopgame.supaplex.screen.MenuScreen;
import nl.oopgame.supaplex.screen.ScoreBoard;
import nl.oopgame.supaplex.tile.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.io.FileNotFoundException;
import java.util.ArrayList;
//</editor-fold>

public class SupaplexApp extends GameEngine {

    private final String MEDIAPATH = "src/assets/";             // Pad naar alle media files

    private GameState gameState;                                // Initialiseer gamestate
    private ArrayList<Level> levels = new ArrayList<Level>();   // Arraylist met alle beschikbare levels
    private MenuScreen menu;                                    // Initialiseer het menu
    private GameEndScreen endScreen;
    private static ScoreBoard scoreBoard;                       // Initialiseer het score bord
    private static Level selectedLevel;                         // Het geselecteerde level om te spelen
    private static int worldWidth = 1204;                       // De breedte van het scherm
    private static int worldHeight = 903;                       // De hoogte van het scherm
    private boolean gameStarted = false;
    private boolean menuInitialized = false;
    private boolean endScreenInitialized = false;
    private Player player;

    //<editor-fold desc="getters & setters">
    public static int getWorldWidth() {
        return worldWidth;
    }

    public static int getWorldHeight() {
        return worldHeight;
    }

    public static ScoreBoard getScoreBoard() { return scoreBoard;}

    public static Level getSelectedLevel() {
        return selectedLevel;
    }

    public static void setSelectedLevel(Level level) {
        selectedLevel = level;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Player getPlayer() {
        return player;
    }
    //</editor-fold>

    public static void main(String[] args) {
        String[] arguments = {"nl.oopgame.supaplex.SupaplexApp"};
        SupaplexApp app = new SupaplexApp();
        PApplet.runSketch(arguments, app);
    }

    /**
     * Stel het spel in
     */
    @Override
    public void setupGame() {
        createViewWithoutViewport(worldWidth, worldHeight);

        // Stel de levels in
        Level levelZero = new Level(1, "level zero");
        this.levels.add(levelZero);

        Level levelOne = new Level(5, "testLevel");
        this.levels.add(levelOne);

        Level levelTwo = new Level(2, "new test level");
        this.levels.add(levelTwo);

        // Stel het gekozen level in. Standaard is dit het eerste level
        this.selectedLevel = levels.get(0);

        // Bij het starten van het spel begint het bij het laten zien van het titelscherm
        this.gameState = GameState.TITLE;

        setGameSpeed(Constants.GAMESPEED);
    }

    /**
     * Werk de app elk frame bij
     */
    @Override
    public void update() {
        // Switch voor het bepalen in welke state de game is en wat er getoond moet worden
        switchGameState();
        switch(gameState) {
            case TITLE:
                // Geef titelscherm weer
                this.displayTitleScreen();
                break;

            case MENU:
                this.menu.drawScreen();
                // Controleer menuclicks
                selectedLevel = this.menu.checkClickAction(selectedLevel);
                // Controleer klik op de startknop
                gameState = this.menu.startLevel();
                break;

            case GAME:
                break;

            case END:
                gameState = this.endScreen.goBackToMenu();
                break;
            case EXIT:
                System.exit(0);
                break;
        }
    }

    /**
     * Hou de gamestate bij, verander deze wanneer nodig
     */
    public void switchGameState(){

        switch(gameState) {
            case TITLE:
                break;

            case MENU:
                if(menuInitialized == false){
                    initializeMenu();
                }

                break;

            case GAME:
                if(!gameStarted){
                    initializeGame();
                }
                break;

            case END:
                if(!endScreenInitialized){
                    cleanGameResidue();
                    initializeEndScreen(false);
                }
                break;
            case EXIT:
                break;
        }
    }

    /**
     * Plaats de speler in het veld
     * @param x     De X locatie van de speler
     * @param y     De Y locatie van de speler
     */
    private void initializePlayer(int x, int y) {
        Sprite playerSprite = new Sprite("src/assets/sprites/sprite_player.png");
        playerSprite.resize(Constants.TILESIZE, Constants.TILESIZE);
        player = new Player(playerSprite, this);
        addGameObject(player, x, y);
    }

    /**
     * Start het scorebord
     */
    private void initializeMenu() {
        cleanGameResidue();
        menu = new MenuScreen(this,levels);
        this.addDashboard(menu);
        gameStarted = false;
        endScreenInitialized = false;
        menuInitialized = true;
    }

    /**
     * Maak het scherm schoon
     */
    private void cleanGameResidue(){
        this.clearView();
        this.deleteAllDashboards();
        this.getView().setBackground(null);
        this.deleteAllGameOBjects();
        this.clear();
    }

    /**
     * Start het scorebord
     */
    private void initializeEndScreen(boolean gameCompleted) {
        cleanGameResidue();
        createViewWithoutViewport(getWorldWidth(),getWorldHeight());
        endScreen = new GameEndScreen(this,gameCompleted);
        endScreen.drawScreen();
        this.addDashboard(endScreen);
        menuInitialized = false;
        endScreenInitialized = true;
        gameStarted = false;
    }

    /**
     * Stel een nieuw spel in
     */
    private void initializeGame(){
        cleanGameResidue();
        initializeTileMap();
        initializeScoreBoard();
        initializePlayer(Constants.STARTX, Constants.STARTY);
        createViewWithViewport(worldWidth,worldHeight,worldWidth,worldHeight,1.5f);
        gameStarted = true;
        endScreenInitialized = false;
        menuInitialized = false;
    }

    /**
     * Start het scorebord
     */
    private void initializeScoreBoard() {
        scoreBoard = new ScoreBoard(this);
        this.addDashboard(scoreBoard);
    }

    /**
     * Creeërt de view zonder viewport
     *
     * @param screenWidth  Breedte van het scherm
     * @param screenHeight Hoogte van het scherm
     */
    private void createViewWithoutViewport(int screenWidth, int screenHeight) {
        View view = new View(screenWidth, screenHeight);

        setView(view);
        size(screenWidth, screenHeight);
    }

    /**
     * Creeërt de view met viewport
     *
     * @param worldWidth   Totale breedte van de wereld
     * @param worldHeight  Totale hoogte van de wereld
     * @param screenWidth  Breedte van het scherm
     * @param screenHeight Hoogte van het scherm
     * @param zoomFactor   Factor waarmee wordt ingezoomd
     */
    private void createViewWithViewport(int worldWidth, int worldHeight, int screenWidth, int screenHeight, float zoomFactor) {
        EdgeFollowingViewport viewPort = new EdgeFollowingViewport(player, (int) Math.ceil(screenWidth / zoomFactor), (int) Math.ceil(screenHeight / zoomFactor), 0, 0);
        CenterFollowingViewport viewPort2 = new CenterFollowingViewport(player, (int) Math.ceil(screenWidth / zoomFactor), (int) Math.ceil(screenHeight / zoomFactor), 0, 0);
        viewPort.setTolerance(Constants.VIEWPORTTOLERANCE, Constants.VIEWPORTTOLERANCE, Constants.VIEWPORTTOLERANCE, Constants.VIEWPORTTOLERANCE);
        View view = new View(viewPort2, worldWidth, worldHeight);
        setView(view);
        size(screenWidth, screenHeight);
    }

    /**
     * Verwijdert de huidige game objecten in de engine instance
     */
    private void clearView() {
        this.deleteAllGameOBjects();
    }

    /**
     * Spel game over, ga naar het eindscherm
     */
    public void gameOver(boolean gameCompleted){
        cleanGameResidue();
        int[][] newTileMap = {{}};
        tileMap.setTileMap(newTileMap);
        initializeEndScreen(gameCompleted);
        gameState = GameState.END;
    }

    /**
     * Level is gehaald, door naar het volgende level
     */
    public void nextLvl(){
        cleanGameResidue();

        int[][] newTileMap = {{}};
        tileMap.setTileMap(newTileMap);

        int lvls = levels.indexOf(selectedLevel);
        int nextlvl = ++lvls;
        if((this.levels.size()) == lvls){
            gameOver(true);
        }else{
            selectedLevel = this.levels.get(nextlvl);
        }

        gameStarted = false;
    }

    /**
     * Initialiseert geluid
     */
    private void initializeSound() {
        // TODO krijg dit werkend op alle operating systems (werkt alleen momenteel op windows)
        // backgroundSound = new Sound(this, MEDIAPATH+"music/supaplex_game.mp3");
        // backgroundSound.loop(-1);
    }

    /**
     * Methode om het titelscherm weer te geven
     */
    private void displayTitleScreen() {

        PImage testing = loadImage(MEDIAPATH+"img/supaplex-title-screen.jpg");
        this.getView().setBackground(testing);

        // Verander de gamestate na 5 seconden
        if (this.millis() > 5000) {
            this.gameState = gameState.MENU;
        }
    }

    /**
     * Initialiseert de tilemap
     */
    private void initializeTileMap() {
        /* TILES */
        Sprite boardsSprite = new Sprite(MEDIAPATH+"sprites/sprite_wall.png");
        Sprite circuitSprite = new Sprite(MEDIAPATH+"sprites/sprite_field.png");
        Sprite microchipSprite = new Sprite(MEDIAPATH+"sprites/sprite_microchip.png");
        Sprite exitSprite = new Sprite(MEDIAPATH+"sprites/sprite_end.png");

        /* OBJECTS */
        Sprite stoneSprite = new Sprite(MEDIAPATH + "sprites/sprite_stone.png");
        stoneSprite.resize(Constants.TILESIZE, Constants.TILESIZE);

        Sprite infotronSprite = new Sprite(MEDIAPATH+"sprites/sprite_infotron.png");
        infotronSprite.resize(Constants.TILESIZE, Constants.TILESIZE);

        Sprite scissorSprite = new Sprite(MEDIAPATH + "sprites/sprite_scissor.png");
        scissorSprite.resize(Constants.TILESIZE, Constants.TILESIZE);

        TileType<BoardsTile> boardTileType = new TileType<>(BoardsTile.class, boardsSprite);
        TileType<CircuitsTile> CircuitTileType = new TileType<>(CircuitsTile.class, circuitSprite);
        TileType<MicrochipsTile> MicrochipTileType = new TileType<>(MicrochipsTile.class, microchipSprite);
        TileType<EndTile> ExitTileType = new TileType<>(EndTile.class, exitSprite);

        TileType[] tileTypes = {boardTileType,CircuitTileType,MicrochipTileType,ExitTileType};

        try{
            int tilesMap[][] = selectedLevel.generateLvl(selectedLevel.getLevelName());
            tileMap = new TileMap(Constants.TILESIZE, tileTypes, tilesMap);
            for (int i = 0;i < tileMap.getTileMap().length-1;i++) {
                for (int j = 0;j < tileMap.getTileMap()[0].length-1;j++) {

                    if (tilesMap[i][j] >= 7) {
                        Tile tempTile = tileMap.getTileOnIndex(j, i);
                        PVector tileLocation = tileMap.getTilePixelLocation(tempTile);

                        switch (tilesMap[i][j]) {
                            case 7:
                                Stone stone = new Stone(stoneSprite, this);
                                addGameObject(stone, tileLocation.x, tileLocation.y);
                                break;
                            case 8:
                                Scissors scissor = new Scissors(scissorSprite, this);
                                addGameObject(scissor, tileLocation.x, tileLocation.y);
                                break;
                            case 9:
                                Infotron infotron = new Infotron(infotronSprite, this);
                                addGameObject(infotron, tileLocation.x, tileLocation.y);
                                break;
                        }
                    }
                }
            }
        }catch(FileNotFoundException e){ }
    }
}