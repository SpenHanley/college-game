package net.fullnerd.android.whack_a_trump;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView[] moles = new ImageView[3];
    private MediaPlayer mediaPlayer;
    private TextView score, lives;

    final Handler handler = new Handler();

    public int playerScore = 0, playerLifes = 10;
    private int translation = -270;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View view = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            int random = new Random().nextInt(3);
            moles[random].animate().translationY(translation).setDuration(500);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; i < moles.length; i++)
                    {
                        for (int j = 0; j < moles.length; j++)
                        {
                            final int id = j;
                            moles[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    moles[id].animate().translationY(0).setDuration(500);
                                    playerScore += 1;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            score.setText(getString(R.string.score) + playerScore);
                                        }
                                    });
                                    mediaPlayer.start();
                                }
                            });
                        }
                        if (moles[i].getTranslationY() == translation)
                        {
                            moles[i].animate().translationY(0).setDuration(500);
                            playerLifes--;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lives.setText(getString(R.string.lives) + playerLifes);
                                    if (playerLifes == 0)
                                    {
                                        // Execute as the person has died
                                        setContentView(R.layout.game_over);
                                    }
                                }
                            });
                        }
                    }
                }
            }, 1500);

            handler.postDelayed(runnableCode, 2000);
        }
    };

    public void changeToGame(View view) {
        setContentView(R.layout.activity_game);
        moles[0] = (ImageView) findViewById(R.id.moleOne);
        moles[1] = (ImageView) findViewById(R.id.moleTwo);
        moles[2] = (ImageView) findViewById(R.id.moleThree);
        score = (TextView) findViewById(R.id.score);
        lives = (TextView) findViewById(R.id.lives);
        playerLifes = 10;
        playerScore = 0;
        lives.setText(getString(R.string.lives) + playerLifes);
        score.setText(getString(R.string.score) + playerScore);

        handler.post(runnableCode);
    }

    public void restart(View view) {
        changeToGame(view);
    }

    public void quit(View view) {
        System.exit(0); // Exited OK
    }
}