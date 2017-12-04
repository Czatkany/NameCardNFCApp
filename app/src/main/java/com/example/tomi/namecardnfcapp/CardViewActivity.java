package com.example.tomi.namecardnfcapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import java.io.File;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class CardViewActivity extends AppCompatActivity {
    private Context context = CardViewActivity.this;
    private NameCardParameters card;
    private Intent intent;
    private String fileAsString;
    private String cardName;
    private io.github.yavski.fabspeeddial.FabSpeedDial floatMenuButton;
    final private CardActionsHandler cardHandler = new CardActionsHandler(CardViewActivity.this);
    private RelativeLayout editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        intent = getIntent();
        File[] allFiles = CardViewActivity.this.getFilesDir().listFiles();
        for(File file : allFiles){
            if( intent.getStringExtra("cardNameParam").equals(file.getName())){
                cardName = file.getName().toString();
                card = cardHandler.loadCard(cardHandler.readCardToStringFromFile(file));
                fileAsString = intent.getStringExtra("cardNameParam") + "/" + cardHandler.readCardToStringFromFile(file);
            }
        }
        editor = cardHandler.generateCardView((RelativeLayout) findViewById(R.id.cardField), card);
        floatMenuButton = (io.github.yavski.fabspeeddial.FabSpeedDial) findViewById(R.id.position_bottom_end);
        floatMenuButton.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch(menuItem.getTitle().toString()){
                    case "Edit":
                        Intent cardEdit = new Intent(context.getApplicationContext(), EditorActivity.class);
                        cardEdit.putExtra("cardNameParam", intent.getStringExtra("cardNameParam"));
                        context.startActivity(cardEdit);
                        break;
                    case "Send":
                        Intent sendCard = new Intent(context.getApplicationContext(), NFCActivity.class);
                        sendCard.putExtra("cardParam", intent.getStringExtra("cardNameParam") + "/" + cardHandler.readCardToStringFromString(intent.getStringExtra("cardNameParam")));
                        context.startActivity(sendCard);
                        break;
                    case "Back":
                        finish();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    //Reload card after editing
    @Override
    protected void onResume() {
        super.onResume();
        card = cardHandler.loadCard(cardHandler.readCardToStringFromString(cardName));
        editor.removeAllViews();
        editor = cardHandler.generateCardView((RelativeLayout) findViewById(R.id.cardField), card);
    }
}
