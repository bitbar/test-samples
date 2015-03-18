package utils;

/**
 * Created by saad on 22/08/14.
 */
public class Constants {

    public static final int MAX_GAME_SCORE = 10;

    public static final int PING_COUNT_MIN = 1;
    public static final int PING_COUNT_MAX = 50;
    public static final int PING_COUNT_DEFAULT = 5;

    public static final long LOCATION_UPDATE_MIN_TIME = 0; // update as frequently as possible, in milliseconds
    public static final float LOCATION_UPDATE_MIN_DISTANCE = 1; // update a change of 5 meters

    public static final long[] VIBRATE_NOTIFICATION = {100, 100, 100, 100};

    public static final String KV_IS_ANSWER_CORRECT = "isAnswerCorrect";
    public static final String KV_NAME = "name";
    public static final String KV_SCORE = "score";

    public static final int GAME_FPS = 40;
    public static final int GAME_MIN_TOUCH_TIME_GAP = 1000;
    public static final long[] GAME_VIBRATE_FIRE = {0, 50};
    public static final long[] GAME_VIBRATE_HIT = {50, 100};


}
