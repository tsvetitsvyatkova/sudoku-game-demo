package com.example.sudokugamedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GamePlayActivity extends AppCompatActivity {

    class Cell {
        int value;
        boolean fixed;
        Button bt;

        public Cell(int initValue, Context THIS) {
            value = initValue;
            if(value!=0) fixed = true;
            else fixed=false;
            bt = new Button(THIS);
            if(fixed) bt.setText(String.valueOf(value));
            else bt.setTextColor(Color.RED);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fixed) return;
                    value++;
                    if(value>9) value=1;
                    bt.setText(String.valueOf(value));
                }
            });
        }
    }

    boolean completed() {
        for(int i=0; i<9; i++)
            for(int j=0; j<9; j++)
                if(table[i][j].value==0)
                    return false;
        return true;
    }

    boolean correct(int i1, int j1, int i2, int j2) {
        boolean[] seen = new boolean[10];
        for(int i=1; i<9; i++) seen[i]=false;
        for(int i=i1; i<i2; i++) {
            for(int j=j1; j<j2; j++) {
                int value = table[i][j].value;
                if(value!=0) {
                    if(seen[value]) return false;
                    seen[value]=true;
                }
            }
        }
        return true;
    }
    boolean correct() {
        for(int i=0;i<9;i++)
            if(!correct(i, 0, i+1, 9)) return false;
        for(int j=0; j<9; j++)
            if(!correct(0, j,9, j+1)) return false;
        for(int i=0;i<3; i++)
            for(int j=0; j<3; j++)
                if(!correct(3*i, 3*j, 3*i+3, 3*j+3))
                    return false;
        return true;
    }

    Cell [] [] table;
    String input;
    TableLayout tl;
    TextView tv;
    LinearLayout linearLayout;
    Button saveBtn;
    Button checkBtn;

    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
            String text = "";
            File file = new File(getFilesDir(), value);
            try {
                BufferedReader reader = new BufferedReader(
                        new FileReader(file));
                String line;
                while((line=reader.readLine()) != null) {
                    text += line + "\n";
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] array = text.split("[ \n]+");
            for (int i=0; i<9; i++) {
                for (int j=0; j<9; j++) {
                    Cell cell = table[i] [j];
                    cell.value = Integer.parseInt(array[i*9+j]);
                    if(cell.value==0) {
                        cell.bt.setText("");
                    }
                    else {
                        cell.bt.setText(String.valueOf(cell.value));
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        input = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    getAssets().open("sudoku.txt")));
            String line;
            while ((line=reader.readLine()) != null) {
                input += line + " ";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] split = input.split("[ ]+");
        table = new Cell [9] [9];
        tl = new TableLayout(this);
        for(int i=0;i<9;i++){
            TableRow tr = new TableRow(this);
            for(int j=0;j<9;j++){
                String s = split[i*9+j];
                Character c = s.charAt(0);
                table[i] [j] = new Cell(c=='?'?0:c-'0',this);
                tr.addView(table[i] [j].bt);
            }
            tl.addView(tr);
        }
        checkBtn = new Button(this);
        checkBtn.setText("Check game");
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(completed() && correct()) {
                    tv.setText("Well done!");
                }
                else if (completed() && !correct()){
                    tv.setText("There is a repeated digit!");
                }
                else {
                    tv.setText("The game is not completed!");
                }
            }
        });

        saveBtn = new Button(this);
        saveBtn.setText("Save Game");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "";
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        Cell cell = table[i][j];
                        if (j > 0) text += " ";
                        text += String.valueOf(cell.value);
                    }
                    text += "\n";
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                Date now = new Date();
                String fileName = "Game " + formatter.format(now);
                File file = new File(getFilesDir(), fileName);
                FileWriter writer;
                try {
                    writer = new FileWriter(file);
                    writer.append(text);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        tl.setShrinkAllColumns(true);
        tl.setStretchAllColumns(true);
        tv=new TextView(this);
        linearLayout=new LinearLayout(this);
        linearLayout.addView(tl);
        linearLayout.addView(saveBtn);
        linearLayout.addView(checkBtn);
        linearLayout.addView(tv);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);
    }
}