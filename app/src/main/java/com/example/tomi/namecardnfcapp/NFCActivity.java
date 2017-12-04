package com.example.tomi.namecardnfcapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * Created by Tomi on 2017. 05. 03..
 */

public class NFCActivity extends AppCompatActivity {
    final private Context context = NFCActivity.this;
    private NfcAdapter nfcAdapter;
    private Intent intent;
    private TextView nfcMessage;
    final private CardActionsHandler cardHandler = new CardActionsHandler(NFCActivity.this);
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    private io.github.yavski.fabspeeddial.FabSpeedDial floatMenuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        nfcMessage = (TextView) findViewById(R.id.nfcMessage);
        intent = getIntent();
        String cardToSend;
        if(!intent.getStringExtra("cardParam").equals(null)){
            nfcMessage.setText(R.string.sending);
            cardToSend = intent.getStringExtra("cardParam");
        }else{
            nfcMessage.setText(R.string.receiving);
            cardToSend = cardHandler.getMainCard();
        }
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        floatMenuButton = (io.github.yavski.fabspeeddial.FabSpeedDial) findViewById(R.id.position_bottom_end);
        floatMenuButton.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch(menuItem.getTitle().toString()){
                    case "Back":
                        finish();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        if (nfcAdapter == null) {
            Toast.makeText(this, R.string.not_supported,
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, R.string.nfc_disabled, Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
        if (nfcAdapter == null) return;

        nfcAdapter.setNdefPushMessage(new NdefMessage(
                new NdefRecord[]{newTextRecord(cardToSend, Locale.ENGLISH, true)}), this);
    }
    //Create NDEF messages
    public static NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }
    //Restart the activity after getting a card
    @Override
    protected void onResume() {
        super.onResume();
        //setupForegroundDispatch(this, nfcAdapter);
        final Intent intent = new Intent(this.getApplicationContext(), this.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, 0);
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList);
    }
    //NFC device nearby
    @Override
    protected void onPause() {
        //disable
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }
    //Receive card
    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();
            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }
    //Processing message
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        //Read text from NDEF message
        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }

            return null;
        }
        private String readText(NdefRecord record) throws UnsupportedEncodingException {

            byte[] payload = record.getPayload();
            String textEncoding = new String("");
            if ((payload[0] & 128) == 0) {
                textEncoding = "UTF-8";
            } else {
                textEncoding = "UTF-16";
            }
            int languageCodeLength = payload[0] & 63;
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }
        //After the card is received it opens the card viewer
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Intent cardView = new Intent(context.getApplicationContext(), CardViewActivity.class);
                cardView.putExtra("cardNameParam", cardHandler.saveSentCard(result));
                context.startActivity(cardView);
                Toast.makeText(context, R.string.card_saved, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}