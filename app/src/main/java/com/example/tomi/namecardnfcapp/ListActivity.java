package com.example.tomi.namecardnfcapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private Context context = ListActivity.this;
    private ArrayList<NameCardListElementResource> cardResource;
    private ListAdapter adapter;
    private ListView cardList;
    private Button backButton;
    final private CardActionsHandler cardHandler = new CardActionsHandler(ListActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        cardResource = new ArrayList<NameCardListElementResource>();
        File[] allFiles = ListActivity.this.getFilesDir().listFiles();
        for(File file : allFiles){
            cardResource.add(cardHandler.generateResourcefromFileString(cardHandler.readCardToString(file), file.getName()));
        }
        backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cardList = (ListView) findViewById(R.id.cardListHolder);
        adapter = new ListAdapter(context, R.layout.list_resource, cardResource);
        cardList.setAdapter(adapter);
    }
}
