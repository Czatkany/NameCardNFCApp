package com.example.tomi.namecardnfcapp;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tomi on 2017. 05. 01..
 */

public class CardActionsHandler {
    Context context;
    public CardActionsHandler(Context context){
        this.context = context;
    }

    public void writeCardToFile(String savedNamecardName, String cardToSave){

            FileOutputStream outputStream;

            try {
                outputStream = context.openFileOutput(savedNamecardName, Context.MODE_PRIVATE);
                outputStream.write(cardToSave.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(context,context.getFilesDir().getAbsolutePath(),Toast.LENGTH_LONG).show();
    }

    public String readCardToString(File file){
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
    }

    public NameCardListElementResource generateResourcefromFileString(String card, String cardName){
        String[] cardTexts = card.split(";");
        return new NameCardListElementResource(Integer.parseInt(cardTexts[0]), cardName);
    }
    public NameCardParameters loadCards(String card, String cardName){
        String[] cardTexts = card.split(";");
        NameCardParameters actualCard= new NameCardParameters(context.getDrawable(Integer.parseInt(cardTexts[0])));
        for(int i = 1; i<cardTexts.length;i++){
            String[] textParams = cardTexts[i].split(",");
            NameCardTextValue actualText = new NameCardTextValue(textParams[0],Float.parseFloat(textParams[1]),Float.parseFloat(textParams[2]),textParams[3],Integer.parseInt(textParams[4]));
            actualCard.Add(actualText);
        }
        return actualCard;
    }
}
