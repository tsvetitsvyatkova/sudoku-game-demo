package com.example.sudokugamedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button startGameBtn;
    Button restoreGameBtn;
    Button takePhotoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGameBtn = findViewById(R.id.startGameBtn);
        restoreGameBtn = findViewById(R.id.restoreGameBtn);
        takePhotoBtn = findViewById(R.id.takePhotoBtn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });
        restoreGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSavedGames();
            }
        });
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
    }

    public void startNewGame(){
        Intent intent = new Intent(this, GamePlayActivity.class);
        startActivity(intent);
    }

    public void openSavedGames(){
        Intent intent = new Intent(this, SavedGamesActivity.class);
        startActivity(intent);
    }

    public void takePhoto(){
        Intent intent = new Intent(this, PhotoGame.class);
        startActivity(intent);
    }
}