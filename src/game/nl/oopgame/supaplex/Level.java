/**
 * Levels class om een level op te bouwen vanuit een json file
 */

package nl.oopgame.supaplex;

import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Level {

    private int infotronsNeeded;
    private int infotronsCollected = 0;
    private String levelName;

    //<editor-fold desc="getters & setters">
    public String getLevelName() {
        return levelName;
    }

    public int getCollectedInfotrons(){
        return this.infotronsCollected;
    }

    public int getInfotronsNeeded(){
        return this.infotronsNeeded;
    }
    //</editor-fold>

    public Level(int infotronsNeeded, String levelName) {
        this.infotronsNeeded = infotronsNeeded;
        this.levelName = levelName;
    }

    /**
     * Verhoog de score
     */
    public void increaseCollectedInfotrons(){
        this.infotronsCollected++;
    }

    /**
     * Bouw het level op vanuit de json file
     * @param levelName                 De naam van het level om te bouwen
     * @return                          De tilemap van het level
     * @throws FileNotFoundException
     */
    public int[][] generateLvl(String levelName) throws FileNotFoundException {

        try{
            FileReader file = new FileReader("src/assets/levels/levels.json");
            JSONObject obj = new JSONObject(file);
            JSONArray arr = obj.getJSONArray(levelName);
            int[][] tilesMap = new int[arr.size()][];
            for (int i = 0; i < arr.size(); i++) {
                int[] internaljsonArray = arr.getJSONArray(i).getIntArray();
                tilesMap[i] = internaljsonArray;
            }
            return tilesMap;
        }catch(FileNotFoundException e){
            throw e;
        }
    }
}