package com.example.chess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.chess.service.BackgroundMusicService;

public class GameSettings extends AppCompatActivity {

    private View whiteTile, blackTile;
    private Intent serviceIntent;
    private SwitchCompat switchCompat;
    private SeekBar seekBar;
    private int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        whiteTile = findViewById(R.id.whiteTile);
        blackTile = findViewById(R.id.blackTile);
        serviceIntent = new Intent(this, BackgroundMusicService.class);
        switchCompat = (SwitchCompat) findViewById(R.id.switch1);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        current = seekBar.getProgress();
        SharedPreferences sharedPreferences = getSharedPreferences("User_Data", 0);
        whiteTile.setBackgroundColor(Color.rgb(sharedPreferences.getInt("whiteR", 0), sharedPreferences.getInt("whiteG", 0), sharedPreferences.getInt("whiteB", 0)));
        blackTile.setBackgroundColor(Color.rgb(sharedPreferences.getInt("blackR", 0), sharedPreferences.getInt("blackG", 0), sharedPreferences.getInt("blackB", 0)));
        switchCompat.setChecked(sharedPreferences.getBoolean("playing", false));
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int j, boolean b) {
                int progress = seekBar.getProgress();
                int times = current - progress; // > 0 -> (-) /  < 0 -> (+)
                if (times > 0) {
                    for (int i = 0; i < Math.abs(times); i++) {
                        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                    }
                } else {
                    for (int i = 0; i < Math.abs(times); i++) {
                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                    }
                }
                current = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (b) {
                    playAudio();
                    editor.putBoolean("playing", true);
                } else {
                    stopAudio();
                    editor.putBoolean("playing", false);
                }
                editor.apply();
            }
        });

        whiteTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        blackTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void saveToSharedPreferences(int[] color_W, int[] color_B) {
        SharedPreferences sharedPreferences = getSharedPreferences("User_Data", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("whiteR", color_W[0]);
        editor.putInt("whiteG", color_W[1]);
        editor.putInt("whiteB", color_W[2]);

        editor.putInt("blackR", color_B[0]);
        editor.putInt("blackG", color_B[1]);
        editor.putInt("blackB", color_B[2]);

        editor.apply();
    }

    private void playAudio() {
        try {
            startService(serviceIntent);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAudio() {
        try {
            stopService(serviceIntent);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setOne(View view) {
        saveToSharedPreferences(new int[]{239, 239, 239}, new int[]{136, 119, 183});
        setViewsColor(new int[]{239, 239, 239}, new int[]{136, 119, 183});
    }

    public void setTwo(View view) {
        saveToSharedPreferences(new int[]{232, 231, 208}, new int[]{75, 115, 153});
        setViewsColor(new int[]{232, 231, 208}, new int[]{75, 115, 153});
    }

    public void setThree(View view) {
        saveToSharedPreferences(new int[]{238, 238, 210}, new int[]{118, 150, 86});
        setViewsColor(new int[]{238, 238, 210}, new int[]{118, 150, 86});
    }

    public void setFour(View view) {
        saveToSharedPreferences(new int[]{240, 217, 181}, new int[]{181, 136, 99});
        setViewsColor(new int[]{240, 217, 181}, new int[]{181, 136, 99});
    }

    public void setViewsColor(int[] color_W, int[] color_B) {
        whiteTile.setBackgroundColor(Color.rgb(color_W[0], color_W[1], color_W[2]));
        blackTile.setBackgroundColor(Color.rgb(color_B[0], color_B[1], color_B[2]));
    }

    public void goBack(View view) {
        startActivity(new Intent(GameSettings.this, GameOptions.class));
        finish();
    }
}