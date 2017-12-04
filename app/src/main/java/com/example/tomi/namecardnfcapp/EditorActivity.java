package com.example.tomi.namecardnfcapp;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS;


public class EditorActivity extends AppCompatActivity {
    private final Context context = EditorActivity.this;
    private List<Integer> drawables;
    private List<Integer> thumbnails;
    private ArrayList<Integer> imageNames;
    private String savedNamecardName;
    private String savedCardString;
    private int selectedDrawableId;
    private Intent intent;
    private RelativeLayout editor;
    final private CardActionsHandler cardHandler = new CardActionsHandler(EditorActivity.this);
    private NameCardParameters card;
    private String editedCardName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_input);
        setContentView(R.layout.activity_editor);
        editor = (RelativeLayout) findViewById(R.id.editor);
        intent = getIntent();
        if(intent.getStringExtra("cardNameParam") == null){
            selectedDrawableId = R.drawable.background1;
        }else{
            for(File file : this.getFilesDir().listFiles()){
                if( intent.getStringExtra("cardNameParam").equals(file.getName())){
                    String cardToLoad = cardHandler.readCardToStringFromFile(file);
                    card = cardHandler.loadCard(cardToLoad);
                    selectedDrawableId = cardHandler.getCardBackgroundId(cardToLoad);
                    editedCardName = file.getName();
                    break;
                }
            }
            editor = cardHandler.generateCardView(editor, card);

        }
        final Spinner spinner = (Spinner) findViewById(R.id.editor_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.editor_items_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(parent.getItemAtPosition(position).toString()) {
                    case "Back":
                        finish();
                        break;
                    case "AddName":
                        addText(TYPE_CLASS_TEXT);
                        parent.setSelection(0);
                        break;
                    case "AddPhone":
                        addText(TYPE_CLASS_PHONE);
                        parent.setSelection(0);
                        break;
                    case "AddEmail":
                        addText(TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                        parent.setSelection(0);
                        break;
                    case "Save":
                        getNameCardName();
                        parent.setSelection(0);
                        break;
                    default:
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });
        addButtons();
        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.findFocus();
            }
        });
    }
    //Get the name of the card and save it
    private void getNameCardName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater vi = LayoutInflater.from(context);
        final View v = vi.inflate(R.layout.save_dialog, null);
        builder.setTitle(R.string.card_name);
        final EditText input = (EditText) v.findViewById(R.id.saved_card_name);
        final CheckBox isUserCard = (CheckBox) v.findViewById(R.id.saved_card_checkbox);
        if(editedCardName != null){
            input.setText(editedCardName);
        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!input.getText().toString().isEmpty() || input.getText().toString().length() <= 0){
                    if(isUserCard.isChecked()){
                        savedNamecardName = input.getText().toString();
                        collectCardData();
                        cardHandler.writeCardToFile(savedNamecardName, savedCardString, isUserCard.isChecked());
                    }else{
                        savedNamecardName = input.getText().toString();
                        collectCardData();
                        cardHandler.writeCardToFile(savedNamecardName, savedCardString, isUserCard.isChecked());
                    }
                }else{
                    Toast.makeText(context, R.string.null_text, Toast.LENGTH_LONG).show();
                    getNameCardName();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setView(v);
        builder.show();
    }
    //collect carddata for saving
    private void collectCardData(){
        ArrayList<View> textViews = new ArrayList<View>();
        NameCardParameters cardToSave = new NameCardParameters(findViewById(R.id.editor).getBackground());
        textViews.addAll(getViewsByTag((ViewGroup) findViewById(R.id.editor), "textInput" + TYPE_CLASS_TEXT));
        textViews.addAll(getViewsByTag((ViewGroup) findViewById(R.id.editor), "textInput" + TYPE_CLASS_TEXT + "underlined"));
        textViews.addAll(getViewsByTag((ViewGroup) findViewById(R.id.editor), "textInput" + TYPE_CLASS_PHONE));
        textViews.addAll(getViewsByTag((ViewGroup) findViewById(R.id.editor), "textInput" + TYPE_CLASS_PHONE + "underlined"));
        textViews.addAll(getViewsByTag((ViewGroup) findViewById(R.id.editor), "textInput" + TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS));
        textViews.addAll(getViewsByTag((ViewGroup) findViewById(R.id.editor), "textInput" + TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS + "underlined"));
        for(View textField : textViews){
            EditText textValue = (EditText) textField;
            String underline = textValue.getTag().toString();
            boolean isUnderlined = false;
            if(underline.contains("underlined")){
                underline = underline.replace("underlined", " ");
                textValue.setTag(underline);
                isUnderlined = true;
            }
            float size = textValue.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            cardToSave.Add(new NameCardTextValue(textField.getTag().toString(), textValue.getX(), textValue.getY(), textValue.getText().toString(), textValue.getCurrentTextColor(), size, textValue.getTypeface().toString(), isUnderlined));
        }
        savedCardString = (selectedDrawableId) + ";";
        for(NameCardTextValue actualText : cardToSave.getTexts()) {
            savedCardString += actualText.Value();
        }
    }
    //Collect editfields for saving
    private static ArrayList<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }
    //Add textfield to the cardeditor
    private void addText(final int input){
        final EditText edt = new EditText(context);
        edt.setText("addName");
        edt.setTag("textInput" + input);
        if(input == TYPE_CLASS_PHONE){
            edt.setText("0000");
        }
        if(input == TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS){
            edt.setText("addEmail");
        }
        edt.setInputType(input);
        edt.setTextSize(40);
        final RelativeLayout inputLayout = (RelativeLayout)findViewById(R.id.editor);
        edt.setOnTouchListener(new TextEditActions(context, edt){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return super.onTouch(v, event);
            }
        });
        inputLayout.addView(edt);
    }
    //Add buttons to the card
    private void addButtons() {
        getImages();
        for(int i = thumbnails.size()-1; i >= 0; i--){
            final int actualElement = i;
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.background_list, null);
            Button imageButtonView = (Button) v.findViewById(R.id.imageButton);
            imageButtonView.setBackgroundResource(thumbnails.get(actualElement));
            imageButtonView.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.editor);
                    rl.setBackgroundResource(drawables.get(actualElement));
                    selectedDrawableId = imageNames.get(actualElement);
                }
            });
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.innerLay);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }
    }
    //Get the images from drawable
    private void getImages()  {
        Field[] fields = R.drawable.class.getFields();
        drawables = new ArrayList<Integer>();
        imageNames = new ArrayList<Integer>();
        thumbnails = new ArrayList<Integer>();
        for (Field field : fields) {
            if (field.getName().startsWith("background")) {
                try {
                    if(field.getName().contains("small")){
                        thumbnails.add((Integer) field.get(null));
                    }else{
                        drawables.add((Integer) field.get(null));
                        imageNames.add(field.getInt(null));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
