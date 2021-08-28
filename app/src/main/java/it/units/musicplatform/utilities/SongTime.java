package it.units.musicplatform.utilities;

public class SongTime {

    private int minutes;
    private int seconds;

    public SongTime() {
        minutes = 0;
        seconds = 0;
    }

    public void addSecond() {
        if (seconds != 59) {
            seconds = seconds + 1;
        } else {
            minutes = minutes + 1;
            seconds = 0;
        }
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setSongTime(int seconds) {
        minutes = seconds / 60;
        this.seconds = seconds % 60;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String toString(long minutes, long seconds){
        return String.format("%02d:%02d", minutes, seconds);
    }
}
