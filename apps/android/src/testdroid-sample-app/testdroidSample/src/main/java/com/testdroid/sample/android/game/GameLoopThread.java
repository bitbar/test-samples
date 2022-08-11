package com.testdroid.sample.android.game;

import android.graphics.Canvas;

import utils.Constants;

public class GameLoopThread extends Thread {

    private final GameView gameView;
    private boolean running = false;

    public GameLoopThread(GameView gameView) {
        this.gameView = gameView;
    }

    public void setRunning(boolean isRun) {
        this.running = isRun;
    }

    @Override
    public void run() {

        int FPS = Constants.GAME_FPS;
        long ticksPerSec = 1000 / FPS; // ticks is the minimum time each iteration should take
        long startTime;
        long sleepTime;

        while (running) {

            Canvas canvas = null;

            startTime = System.currentTimeMillis();

            try {
                canvas = gameView.getHolder().lockCanvas();
                synchronized (gameView.getHolder()) {
                    gameView.draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    gameView.getHolder().unlockCanvasAndPost(canvas);
                }
            }

            sleepTime = ticksPerSec - (System.currentTimeMillis() - startTime);
            try {

                if (sleepTime > 0) {
                    sleep(sleepTime);
                } else {
                    sleep(10);
                }

            } catch (Exception ignore) {
            }

        }

    }
}
