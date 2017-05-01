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
    final private CardActionsHandler cardHandler = new CardActionsHandler(CardViewActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        Intent myIntent = getIntent();
        backButton = (Button) findViewById(R.id.back);
        sendButton = (Button) findViewById(R.id.sendCard);


        File[] allFiles = CardViewActivity.this.getFilesDir().listFiles();
        for(File file : allFiles){
            String kakimakika = file.getName();
            if( myIntent.getStringExtra("cardNameParam").equals(file.getName())){
                card = cardHandler.loadCards(cardHandler.readCardToString(file), file.getName());
            }
        }
        RelativeLayout editor = (RelativeLayout) findViewById(R.id.cardField);
        generateCardView(editor, card);
        OnClick();
    }

    private void generateCardView(RelativeLayout editField, NameCardParameters card){
        LayoutInflater vi = LayoutInflater.from(getBaseContext());
        editField.setBackground(card.getBackground());


        for(NameCardTextValue textField : card.getTexts()){

            RelativeLayout rl = (RelativeLayout) vi.inflate(R.layout.text_input, null);
            RelativeLayout holder = (RelativeLayout) rl.findViewById(R.id.textInput);
            holder.setX(textField.getxCoord());
            holder.setY(textField.getyCoord());
            EditText editTextToAdd = (EditText) holder.findViewById(R.id.textView);
            editTextToAdd.setText(textField.getText());
            editTextToAdd.setTextColor(textField.getColor());
            editTextToAdd.setTextIsSelectable(false);
            editTextToAdd.setEnabled(false);
            editField.addView(rl);
            //ViewGroup insertPoint = (ViewGroup) findViewById(R.id.cardField);
            //insertPoint.addView(editField, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }
        //return editField;
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
                Intent nfcActivity = new Intent(context.getApplicationContext(), NFCActivity.class);
                //nfcActivity.putExtra("cardNameParam", p.text);
                context.startActivity(nfcActivity);
            }
        });
    }

    /*private String fileReader(File file){
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }*/

    /*private void splitFiles(String card, String cardName){
        String[] cardTexts = card.split(";");
        NameCardParameters actualCard= new NameCardParameters(getDrawable(Integer.parseInt(cardTexts[0])));
        for(int i = 1; i<cardTexts.length;i++){
            String[] textParams = cardTexts[1].split(",");
            NameCardTextValue actualText = new NameCardTextValue(textParams[0],Float.parseFloat(textParams[1]),Float.parseFloat(textParams[2]),textParams[3],Integer.parseInt(textParams[4]));
            actualCard.Add(actualText);
        }
        cards.add(actualCard);
    }*/
}
