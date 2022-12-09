package com.example.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tictactoe.databinding.ActivityMainBinding;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FloatingActionButton btnPlay, btnSetting, btnQuit;
    private ImageView titleImage;
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog dialog;

    // Settings Buttons
    private Chip opponentChip,difficultyChip,chip3;
    private FloatingActionButton settingsDoneBtn;
    boolean enabled, difficulty;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initSettings();
        initViews();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity GameScreen = new GameScreen();
                switchActivities(GameScreen);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewSettingsDialog();
            }
        });
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

    }

    private void initSettings() {
        //initital settings
        enabled = true;
        difficulty = false;
    }

    private void initViews() {
        btnPlay = findViewById(R.id.btnPlay);
        btnSetting = findViewById(R.id.btnSettings);
        btnQuit = findViewById(R.id.btnQuit);
        titleImage = findViewById(R.id.titleImage);
        String titleImageUrl = "https://static.vecteezy.com/system/resources/previews/004/532/221/original/tic-tac-toe-seamless-background-on-dark-blue-illustration-vector.jpg";
        Glide.with(this)
                .asBitmap()
                .load(titleImageUrl)
                .into(titleImage);
    }

    private void switchActivities(Activity activity) {
        Intent switchActivity = new Intent(this, activity.getClass());
        Bundle bundle = new Bundle();
        bundle.putBoolean("enabled",enabled);
        bundle.putBoolean("difficulty",difficulty);
        switchActivity.putExtras(bundle);
        startActivity(switchActivity);
    }

    public void createNewSettingsDialog(){
        dialogBuilder = new MaterialAlertDialogBuilder(this);
        final View settingsPopupView = getLayoutInflater().inflate(R.layout.settings_fragment_layout,null);
        opponentChip = settingsPopupView.findViewById(R.id.opponentChip);
        difficultyChip = settingsPopupView.findViewById(R.id.difficultyChip);
        chip3 = settingsPopupView.findViewById(R.id.chip3);
        settingsDoneBtn = settingsPopupView.findViewById(R.id.settingsDoneBtn);


        dialogBuilder.setView(settingsPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        settingsDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Define finished button here


                determineSettings();
                dialog.dismiss();
            }
        });

        difficultyChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                difficultyChip.setChecked(false);
            }
        });
    }

    private void bindButtons(){
        //binding.btnPlay.setOnClickListener();
    }

    private void determineSettings() {
        enabled = opponentChip.isChecked();
        difficulty = difficultyChip.isChecked();
    }

}