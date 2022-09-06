package com.testdroid.sample.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import utils.Animations;
import utils.Constants;
import utils.Helpers;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class NA_Native extends Activity implements View.OnClickListener {

    private InputMethodManager inputMethodManager;

    private static final String STATE_FREE = "free";
    private static final String STATE_PRESSED = "pressed";
    private static final String STATE_HIGHLIGHTED = "highlighted";

    private static ImageButton[][] tiles;
    private static int[][] tileTops;
    private static int[][] tileTopsGame;
    private static int[] swapTile = {-1, -1};
    private int score = Constants.MAX_GAME_SCORE;

    // UI Widgets
    private static LinearLayout ll_game;
    private static LinearLayout ll_question;
    private static RadioGroup rg_options;
    private static EditText et_name;
    private static Button b_answer;
    private static TextView tv_score;
    private static ImageButton ib_tile11;
    private static ImageButton ib_tile12;
    private static ImageButton ib_tile13;
    private static ImageButton ib_tile21;
    private static ImageButton ib_tile22;
    private static ImageButton ib_tile23;
    private static ImageButton ib_tile31;
    private static ImageButton ib_tile32;
    private static ImageButton ib_tile33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.na_layout);

        inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        ll_game = (LinearLayout) findViewById(R.id.na_ll_game);
        ll_question = (LinearLayout) findViewById(R.id.na_ll_question);
        rg_options = (RadioGroup) findViewById(R.id.na_rg_options);
        et_name = (EditText) findViewById(R.id.na_et_name);
        b_answer = (Button) findViewById(R.id.na_b_answer);
        tv_score = (TextView) findViewById(R.id.na_tv_score);
        ib_tile11 = (ImageButton) findViewById(R.id.na_ib_tile1);
        ib_tile12 = (ImageButton) findViewById(R.id.na_ib_tile2);
        ib_tile13 = (ImageButton) findViewById(R.id.na_ib_tile3);
        ib_tile21 = (ImageButton) findViewById(R.id.na_ib_tile4);
        ib_tile22 = (ImageButton) findViewById(R.id.na_ib_tile5);
        ib_tile23 = (ImageButton) findViewById(R.id.na_ib_tile6);
        ib_tile31 = (ImageButton) findViewById(R.id.na_ib_tile7);
        ib_tile32 = (ImageButton) findViewById(R.id.na_ib_tile8);
        ib_tile33 = (ImageButton) findViewById(R.id.na_ib_tile9);

        tiles = new ImageButton[][]{{ib_tile11, ib_tile12, ib_tile13}, {ib_tile21, ib_tile22, ib_tile23}, {ib_tile31, ib_tile32, ib_tile33}};
        tileTops = new int[][]{{R.drawable.logo_t, R.drawable.logo_e, R.drawable.logo_s}, {R.drawable.logo_t, R.drawable.logo_d, R.drawable.logo_r}, {R.drawable.logo_o, R.drawable.logo_i, R.drawable.logo_d}};

        // un-shuffled
        tileTopsGame = tileTops;

        tv_score.setOnClickListener(this);
        ib_tile11.setOnClickListener(this);
        ib_tile12.setOnClickListener(this);
        ib_tile13.setOnClickListener(this);
        ib_tile21.setOnClickListener(this);
        ib_tile22.setOnClickListener(this);
        ib_tile23.setOnClickListener(this);
        ib_tile31.setOnClickListener(this);
        ib_tile32.setOnClickListener(this);
        ib_tile33.setOnClickListener(this);
        b_answer.setOnClickListener(this);

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int nameLength = et_name.getText().toString().replace(" ", "").length();
                if (nameLength > 0) {
                    enableAnswerButton();
                } else {
                    disableAnswerButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        configureTileLayout();

        // setting presets
        tv_score.setText("" + score);
        shuffleTiles(false);
        updateAllTiles();

        rg_options.check(R.id.na_rb_option1);
        disableAnswerButton();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.na_tv_score:
                switchQuestion();
                break;
            case R.id.na_ib_tile1:
                tileClick(0, 0);
                break;
            case R.id.na_ib_tile2:
                tileClick(0, 1);
                break;
            case R.id.na_ib_tile3:
                tileClick(0, 2);
                break;
            case R.id.na_ib_tile4:
                tileClick(1, 0);
                break;
            case R.id.na_ib_tile5:
                tileClick(1, 1);
                break;
            case R.id.na_ib_tile6:
                tileClick(1, 2);
                break;
            case R.id.na_ib_tile7:
                tileClick(2, 0);
                break;
            case R.id.na_ib_tile8:
                tileClick(2, 1);
                break;
            case R.id.na_ib_tile9:
                tileClick(2, 2);
                break;
            case R.id.na_b_answer:
                answer();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void answer() {
        inputMethodManager.hideSoftInputFromWindow(et_name.getWindowToken(), 0);

        String name = et_name.getText().toString();
        goto_AN(rg_options.getCheckedRadioButtonId() == R.id.na_rb_option2, name);
    }

    private void enableAnswerButton() {
        b_answer.setEnabled(true);
        b_answer.setAlpha(1.0f);
    }

    private void disableAnswerButton() {
        b_answer.setEnabled(false);
        b_answer.setAlpha(0.5f);
    }

    private void switchQuestion() {

        if (ll_game.getVisibility() == View.VISIBLE) {
            ll_game.startAnimation(Animations.slideOutLeft());
            ll_question.startAnimation(Animations.slideInRight());
            ll_game.setVisibility(View.GONE);
            ll_question.setVisibility(View.VISIBLE);
            tv_score.setBackgroundResource(R.drawable.button_score_back);
        } else {
            ll_game.startAnimation(Animations.slideInLeft());
            ll_question.startAnimation(Animations.slideOutRight());
            ll_game.setVisibility(View.VISIBLE);
            ll_question.setVisibility(View.GONE);
            tv_score.setBackgroundResource(R.drawable.button_score);
        }
    }

    private void tileClick(int x, int y) {
        ImageButton tile = tiles[x][y];

        if (tile.getTag() == STATE_HIGHLIGHTED) {
            swap(swapTile[0], swapTile[1], x, y);
            swapTile = new int[]{-1, -1};
            incrementRound();
        } else {
            swapTile = new int[]{x, y};
            tile.setTag(STATE_PRESSED);
            tile.setEnabled(false);
            tile.setBackgroundResource(R.drawable.button_game_pressed);
            highlightNeighbours(x, y);
        }
    }

    private void highlightNeighbours(int x, int y) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if ((i - 1 == x && j == y) || (i + 1 == x && j == y) || (i == x && j - 1 == y) || (i == x && j + 1 == y)) {
                    tiles[i][j].setTag(STATE_HIGHLIGHTED);
                    tiles[i][j].setBackgroundResource(R.drawable.button_game_highlight);
                } else if (!(i == x && j == y)) {
                    tiles[i][j].setTag(STATE_FREE);
                    tiles[i][j].setBackgroundResource(R.drawable.set_button_game_background);
                    tiles[i][j].setEnabled(true);
                }
            }
        }
    }

    private void swap(int x1, int y1, int x2, int y2) {
        int imageA = tileTopsGame[x1][y1];
        int imageB = tileTopsGame[x2][y2];
        tileTopsGame[x1][y1] = imageB;
        tileTopsGame[x2][y2] = imageA;

        if (x1 == x2) {
            if (y1 > y2) {
                tiles[x1][y1].startAnimation(Animations.slideInLeft());
                tiles[x2][y2].startAnimation(Animations.slideInRight());
            } else {
                tiles[x1][y1].startAnimation(Animations.slideInRight());
                tiles[x2][y2].startAnimation(Animations.slideInLeft());
            }
        } else {
            if (x1 > x2) {
                tiles[x1][y1].startAnimation(Animations.slideInTop());
                tiles[x2][y2].startAnimation(Animations.slideInBottom());
            } else {
                tiles[x1][y1].startAnimation(Animations.slideInBottom());
                tiles[x2][y2].startAnimation(Animations.slideInTop());
            }
        }

        tiles[x1][y1].setImageResource(imageB);
        tiles[x2][y2].setImageResource(imageA);
    }

    private void incrementRound() {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                tiles[i][j].setBackgroundResource(R.drawable.set_button_game_background);
                tiles[i][j].setTag(STATE_FREE);
                tiles[i][j].setEnabled(true);
            }
        }

        score--;
        tv_score.setText("" + score);
        if (score > 0) {
            tv_score.startAnimation(Animations.rotate());
            checkWin();
        } else {
            // goto_AN(false, null);
            switchQuestion();
            tv_score.setEnabled(false);
            tv_score.setAlpha(0.5f);
        }
    }

    private void updateAllTiles() {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                tiles[i][j].setImageResource(tileTopsGame[i][j]);
            }
        }
    }

    private void shuffleTiles(boolean isRandomShuffle) {
        if (!isRandomShuffle) {
            tileTopsGame = new int[][]{{R.drawable.logo_t, R.drawable.logo_d, R.drawable.logo_e}, {R.drawable.logo_t, R.drawable.logo_s, R.drawable.logo_r}, {R.drawable.logo_o, R.drawable.logo_i, R.drawable.logo_d}};
            return;
        } else {
            // TODO - shuffle tileTopsGame here
        }
    }

    private void checkWin() {

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (tileTopsGame[i][j] != tileTops[i][j]) {
                    return;
                }
            }
        }

        goto_AN(true, null);
    }

    private void configureTileLayout() {
        int margin = Helpers.getDimenDp(getApplicationContext(), R.dimen.margin_activity_horizontal);
        Pair<Integer, Integer> windowDimensions = Helpers.getWindowDimensionsWithoutMargin(getApplicationContext(), getWindowManager(), margin + 6, margin + 6);

        int tileWidth = (int) windowDimensions.first / 3;

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                tiles[i][j].getLayoutParams().height = tileWidth;
                tiles[i][j].getLayoutParams().width = tileWidth;
            }
        }

    }

    private void goto_AN(final boolean isAnswerCorrect, String name) {

        final Intent intent = new Intent(NA_Native.this, AN_Answer.class);
        intent.putExtra(Constants.KV_IS_ANSWER_CORRECT, isAnswerCorrect);
        intent.putExtra(Constants.KV_NAME, name);
        intent.putExtra(Constants.KV_SCORE, score);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);

                if (isAnswerCorrect) {
                    finish();
                }

            }
        }, 500);

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                tiles[i][j].startAnimation(Animations.rotate());
            }
        }

    }
}
