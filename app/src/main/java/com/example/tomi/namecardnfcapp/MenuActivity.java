package com.example.tomi.namecardnfcapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MenuActivity extends AppCompatActivity {

    private Button savedCardsButton;
    private Button cardButton;
    private Button quitButton;
    private Button clearDataButton;
    private Button getCardButton;
    private Context context = MenuActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        savedCardsButton = (Button)findViewById(R.id.savedCards);
        cardButton = (Button)findViewById(R.id.cardEditor);
        clearDataButton = (Button) findViewById(R.id.clearData);
        quitButton = (Button)findViewById(R.id.quit);
        getCardButton = (Button) findViewById(R.id.getCard);
        Onclick();
    }

    private void Onclick(){
        savedCardsButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent listScreen = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(listScreen);
            }
        });
        cardButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent editorScreen = new Intent(getApplicationContext(), EditorActivity.class);
                startActivity(editorScreen);
            }
        });
        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File[] allFiles = MenuActivity.this.getFilesDir().listFiles();
                for(File file : allFiles){
                    file.delete();
                }
            }
        });
        getCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getCardScreen = new Intent(getApplicationContext(), GetCardActivity.class);
                startActivity(getCardScreen);
            }
        });
        quitButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
               finish();
            }
        });

    }

}
