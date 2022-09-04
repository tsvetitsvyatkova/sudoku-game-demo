package com.example.sudokugamedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;

public class SavedGamesActivity extends AppCompatActivity {

    ListView savedGamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);
        savedGamesList = findViewById(R.id.savedGamesList);
        File directory = new File(String.valueOf(getFilesDir()));
        File[] files = directory.listFiles();
        ArrayList arrayList = new ArrayList<>();
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
            arrayList.add(files[i].getName());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        savedGamesList.setAdapter(arrayAdapter);

        savedGamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = "";
                File directory = new File(String.valueOf(getFilesDir()));
                File[] files = directory.listFiles();
                assert files != null;
                startSavedGame(files[i].getName());
            }
        });
    }

    public void startSavedGame(String gameName) {
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("key",gameName);
        startActivity(intent);
    }
}