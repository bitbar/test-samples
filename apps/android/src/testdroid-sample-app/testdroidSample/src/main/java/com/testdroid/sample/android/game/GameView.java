package com.testdroid.sample.android.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.testdroid.sample.android.R;

import utils.Constants;
import utils.Helpers;

public class GameView extends SurfaceView {

    private static final String TAG = GameView.class.getName();

    private SurfaceHolder surfaceHolder;
    private GameLoopThread gameLoopThread;

    private int[] idsImagesBugs = {R.drawable.game_bug1, R.drawable.game_bug2, R.drawable.game_bug3, R.drawable.game_bug4, R.drawable.game_bug5};
    private int[] idsImagesGuns = {R.drawable.game_gun1, R.drawable.game_gun2, R.drawable.game_gun3, R.drawable.game_gun4};
    private int[] idsImagesGunsFired = {R.drawable.game_gun1_fired, R.drawable.game_gun2_fired, R.drawable.game_gun3_fired, R.drawable.game_gun4_fired};
    private int[] idsImagesBullets = {R.drawable.game_bullet1, R.drawable.game_bullet2, R.drawable.game_bullet3, R.drawable.game_bullet4};
    private int[] idsSoundsGuns = {R.raw.gun1, R.raw.gun2, R.raw.gun3, R.raw.gun4};

    private final int[] SPEED_X_BUGS = {2, 5, 8, 10, 15, 25};

    private Bitmap bitmapBullet;
    private Bitmap[] bitmapsBugs;
    private Bitmap[] bitmapsGuns;
    private Bitmap[] bitmapsBullets;

    private int[][] positionsGuns = null;

    private static final int MARGIN_HORIZONTAL = 50;
    private static final int MARGIN_VERTICAL = 50;
    private static final int BULLET_SPEED = 25;


    private int bugType = -1;
    private int xBug = 0;
    private int yBug = 0;
    private int xSpeed = 0;

    private int xBullet = 0;
    private int yBullet = 0;

    private int firingGun = -1;

    private long lastTouchEvent = 0;

    private SoundPool soundPool;
    private int[] soundsGuns;
    private int soundHit;
    private int soundBackground;

    public GameView(Context context) {
        super(context);

        gameLoopThread = new GameLoopThread(this);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                soundPool.release();
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException ignore) {
                    }
                }
            }
        });

        initializeBitmaps();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (System.currentTimeMillis() - lastTouchEvent < Constants.GAME_MIN_TOUCH_TIME_GAP) {
            return true;
        }

        if (firingGun != -1) {
            return true;
        }

        lastTouchEvent = System.currentTimeMillis();

        Log.d(TAG, String.format("onTouchEvent - xBug: %s, y: %s", event.getX(), event.getY()));

        for (int i = 0; i < positionsGuns.length; i++) {
            if (event.getX() > positionsGuns[i][0] && event.getX() < positionsGuns[i][0] + bitmapsGuns[i].getWidth() && event.getY() > positionsGuns[i][1]) {
                Log.d(TAG, String.format("gunClicked: %s", i));
                firingGun = i;
                bitmapsGuns[i] = BitmapFactory.decodeResource(getResources(), idsImagesGunsFired[firingGun]);
                soundPool.play(soundsGuns[firingGun], 1.0f, 1.0f, 0, 0, 1.5f);
                Helpers.vibrate(getContext(), Constants.GAME_VIBRATE_FIRE);
                break;
            }
        }

        return true;
    }

    public void draw(Canvas canvas) {

        super.draw(canvas);
        if (canvas == null) {
            return;
        }

        canvas.drawColor(getResources().getColor(R.color.bitbar_grey_dark));

        drawGuns(canvas);

        drawBug(canvas);

        if (firingGun != -1) {
            drawBullet(canvas);
        }

        checkHit();

    }

    private void drawGuns(Canvas canvas) {

        if (positionsGuns == null) {
            positionsGuns = calculateGunPositions(bitmapsGuns, getWidth(), getHeight());
        }

        for (int i = 0; i < bitmapsGuns.length; i++) {
            canvas.drawBitmap(bitmapsGuns[i], positionsGuns[i][0], positionsGuns[i][1], null);
        }

    }

    private void drawBug(Canvas canvas) {

        if (bugType == -1) {
            bugType = Helpers.randomInt(0, idsImagesBugs.length - 1);
            xBug = Helpers.randomInt(MARGIN_HORIZONTAL, getWidth() - bitmapsBugs[bugType].getWidth() - MARGIN_HORIZONTAL);
            yBug = Helpers.randomInt(MARGIN_VERTICAL, getHeight() - bitmapsGuns[0].getHeight() - MARGIN_VERTICAL - bitmapsBugs[bugType].getHeight());
            xSpeed = SPEED_X_BUGS[bugType];
        }

        if (xBug >= getWidth() - (bitmapsBugs[bugType].getWidth() + MARGIN_HORIZONTAL)) {
            xSpeed = SPEED_X_BUGS[bugType] * -1;
        } else if (xBug <= MARGIN_HORIZONTAL) {
            xSpeed = SPEED_X_BUGS[bugType];
        }

        xBug += xSpeed;

        canvas.drawBitmap(bitmapsBugs[bugType], xBug, yBug, null);

    }

    private void drawBullet(Canvas canvas) {

        if (xBullet == 0 && yBullet == 0) {
            bitmapBullet = bitmapsBullets[firingGun];
            xBullet = positionsGuns[firingGun][0] + (bitmapsGuns[firingGun].getWidth() / 2) - (bitmapBullet.getWidth() / 2);
            yBullet = positionsGuns[firingGun][1] - bitmapBullet.getHeight();
        } else if (yBullet - BULLET_SPEED > MARGIN_VERTICAL) {
            yBullet -= BULLET_SPEED;
        } else {
            bitmapsGuns[firingGun] = BitmapFactory.decodeResource(getResources(), idsImagesGuns[firingGun]);
            firingGun = -1;
            xBullet = 0;
            yBullet = 0;
            return;
        }

        canvas.drawBitmap(bitmapBullet, xBullet, yBullet, null);

    }

    private void checkHit() {
        if (xBullet > xBug - (bitmapBullet.getWidth() / 2) && xBullet < xBug + bitmapsBugs[bugType].getWidth() + (bitmapBullet.getWidth() + 2) && yBullet < yBug + bitmapsBugs[bugType].getHeight() && yBullet > yBug + (bitmapsBugs[bugType].getHeight() / 2)) {
            bitmapsGuns[firingGun] = BitmapFactory.decodeResource(getResources(), idsImagesGuns[firingGun]);
            firingGun = -1;
            xBullet = 0;
            yBullet = 0;
            bugType = -1;
            Helpers.vibrate(getContext(), Constants.GAME_VIBRATE_HIT);
            soundPool.play(soundHit, 0.8f, 0.8f, 0, 0, 1.5f);
        }
    }

    private static int[][] calculateGunPositions(Bitmap[] bitmapGuns, int width, int height) {

        int totalGuns = bitmapGuns.length;

        int[][] gunPositions = new int[totalGuns][2];

        int widestGun = 0;
        int highestGun = 0;

        for (Bitmap bitmapGun : bitmapGuns) {
            if (bitmapGun.getWidth() > widestGun) {
                widestGun = bitmapGun.getWidth();
            }
            if (bitmapGun.getHeight() > highestGun) {
                highestGun = bitmapGun.getHeight();
            }
        }

        int horizontalSpacing = (width - (totalGuns * widestGun)) / (totalGuns + 1);

        int y = height - (highestGun + MARGIN_VERTICAL);

        for (int i = 0; i < totalGuns; i++) {
            gunPositions[i][0] = (horizontalSpacing * (i + 1)) + (widestGun * i);
            gunPositions[i][1] = y;
        }

        return gunPositions;
    }

    private void initializeBitmaps() {

        bitmapsGuns = new Bitmap[idsImagesGuns.length];
        for (int i = 0; i < idsImagesGuns.length; i++) {
            bitmapsGuns[i] = BitmapFactory.decodeResource(getResources(), idsImagesGuns[i]);
        }

        bitmapsBullets = new Bitmap[idsImagesBullets.length];
        for (int i = 0; i < idsImagesBullets.length; i++) {
            bitmapsBullets[i] = BitmapFactory.decodeResource(getResources(), idsImagesBullets[i]);
        }

        bitmapsBugs = new Bitmap[idsImagesBugs.length];
        for (int i = 0; i < idsImagesBugs.length; i++) {
            bitmapsBugs[i] = BitmapFactory.decodeResource(getResources(), idsImagesBugs[i]);
        }

        bitmapBullet = BitmapFactory.decodeResource(getResources(), R.drawable.game_bullet1);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        soundsGuns = new int[idsSoundsGuns.length];
        for (int i = 0; i < idsSoundsGuns.length; i++) {
            soundsGuns[i] = soundPool.load(getContext(), idsSoundsGuns[i], 1);
        }
        soundHit = soundPool.load(getContext(), R.raw.hit, 1);
        soundBackground = soundPool.load(getContext(), R.raw.bg, 1);

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (sampleId == soundBackground) {
                soundPool.play(soundBackground, 1.0f, 1.0f, 1, -1, 1.0f);
            }
        });

    }
}
