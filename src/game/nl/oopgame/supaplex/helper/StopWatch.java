/**
 * Class die bijhoudt hoeveel tijd er in een level is verstreken
 */

package nl.oopgame.supaplex.helper;

import java.util.concurrent.TimeUnit;

public class StopWatch {
    private long startTime;

    /**
     * Start de timer
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Geeft de verstreken tijd terug
     * @return
     */
    public String getElapsedTime() {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours((System.currentTimeMillis() - startTime)),
                TimeUnit.MILLISECONDS.toMinutes((System.currentTimeMillis() - startTime)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((System.currentTimeMillis() - startTime))),
                TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - startTime)) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((System.currentTimeMillis() - startTime))));

    }
}