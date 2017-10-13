package com.example.tomi.namecardnfcapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;

public class CardViewActivity extends AppCompatActivity {
    private Context context = CardViewActivity.this;
    private NameCardParameters card;
    private ArrayList<NameCardListElementResource> cardResource;
    private Button backButton;
    private Button sendButton;
    private Intent intent;
    private String fileAsString;
    final private CardActionsHandler cardHandler = new CardActionsHandler(CardViewActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        intent = getIntent();
        backButton = (Button) findViewById(R.id.back);
        sendButton = (Button) findViewById(R.id.sendCard);
        File[] allFiles = CardViewActivity.this.getFilesDir().listFiles();
        for(File file : allFiles){
            if( intent.getStringExtra("cardNameParam").equals(file.getName())){
                card = cardHandler.loadCard(cardHandler.readCardToString(file));
                fileAsString = intent.getStringExtra("cardNameParam") + "/" + cardHandler.readCardToString(file);
            }
        }

        RelativeLayout editor = (RelativeLayout) findViewById(R.id.cardField);
        editor = cardHandler.generateCardView(editor, card, null);
        OnClick();
    }

    private void OnClick(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nfcActivity = new Intent(context.getApplicationContext(), SendCardActivity.class);
                nfcActivity.putExtra("cardParam", fileAsString);
                context.startActivity(nfcActivity);
            }
        });
    }
}
