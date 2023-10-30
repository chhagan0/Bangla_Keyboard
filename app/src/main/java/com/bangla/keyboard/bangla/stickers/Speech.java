package com.bangla.keyboard.bangla.stickers;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Speech extends Service implements RecognitionListener {
    HashMap<String,String> hashMap;
    String current="ENGLISH";
    String id = "";
    String result = "";
    Boolean sent = false;
    String partial = "";
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private SpeechRecognizer speechRecognizer;


    @Nullable
    @Override

    public IBinder onBind(Intent intent) {


        return null;
    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {

        hashMap = new LinkedHashMap<>();
        putMap();
        final SharedPreferences preferences = getSharedPreferences("prefs",MODE_PRIVATE);
        if(preferences.getString("lang","").equals("")){
            current = "ENGLISH";
        }
        else{
            current = preferences.getString("lang","");
        }
        id = hashMap.get(current);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speechRecognizer.setRecognitionListener(this);
        result = "";partial="";
        Intent voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, current);
        voice.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        speechRecognizer.startListening(voice);
        return START_REDELIVER_INTENT;

    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        Log.e("sdsdf", "STOPPED");
        Log.e("partials", partial);
        if (speechRecognizer != null) speechRecognizer.stopListening();
        if (result.equals("")){
            Intent intent = new Intent().setAction("SENDMESSAGE").putExtra("message", partial);
            sendBroadcast(intent);
        }
        super.onDestroy();
    }


    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList arrayList = results.getStringArrayList("results_recognition");
        result = arrayList.get(0).toString();
        Log.e("hjfjhjfhjh", result);
        sendMessage(arrayList.get(0).toString());
        this.stopSelf();
        sendStop();
    }

    private void sendStop () {
        Intent intent = new Intent().setAction("SENDMESSAGE").putExtra("stop", true);
        sendBroadcast(intent);
    }

    private void sendMessage (String message) {
        if(!sent) {
            sent = true;
            Intent intent = new Intent().setAction("SENDMESSAGE").putExtra("message", message);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList arrayList = bundle.getStringArrayList("results_recognition");
        partial = arrayList.get(0).toString();
    }

    @Override
    public void onEvent(int i,Bundle bundle) {

    }

    void putMap(){
        hashMap.put("ENGLISH","en-IN");
        hashMap.put("AFRIKAANS","af");
        hashMap.put("ARABIC","ar");
        hashMap.put("ARMENIAN","hy");
        hashMap.put("AZERBAIJANI","az-AZ");
        hashMap.put("BASQUE","eu");
        hashMap.put("BENGALI","bn");
        hashMap.put("BULGARIAN","bg");
        hashMap.put("CATALAN","ca");
        hashMap.put("CHINESE","zh-CN");
        hashMap.put("CROATIAN","hr");
        hashMap.put("CZECH","cs");
        hashMap.put("DANISH","da");
        hashMap.put("DUTCH","nl");
        hashMap.put("ESTONIAN","et");
        hashMap.put("FILIPINO","tl");
        hashMap.put("FINNISH","fi");
        hashMap.put("FRENCH","fr");
        hashMap.put("GALICIAN","gl");
        hashMap.put("GEORGIAN","ka-GE");
        hashMap.put("GERMAN","de");
        hashMap.put("GREEK","el");
        hashMap.put("GUJARATI","gu-IN");
        hashMap.put("HEBREW","iw");
        hashMap.put("HINDI","hi");
        hashMap.put("HUNGARIAN","hu");
        hashMap.put("ICELANDIC","is");
        hashMap.put("INDONESIAN","id");
        hashMap.put("ITALIAN","it");
        hashMap.put("JAPANESE","ja");
        hashMap.put("KANNADA","kn-IN");
        hashMap.put("KOREAN","ko");
        hashMap.put("LATVIAN","lv");
        hashMap.put("LITHUANIAN","lt");
        hashMap.put("MALAY","ms");
        hashMap.put("MALAYALAM","ml-IN");
        hashMap.put("NORWEGIAN","no");
        hashMap.put("PERSIAN","fa");
        hashMap.put("POLISH","pl");
        hashMap.put("PORTUGUESE","pt");
        hashMap.put("ROMANIAN","ro");
        hashMap.put("RUSSIAN","ru");
        hashMap.put("SERBIAN","sr");
        hashMap.put("SLOVAK","sk");
        hashMap.put("SLOVENIAN","sl");
        hashMap.put("SPANISH","es");
        hashMap.put("SWAHILI","sw");
        hashMap.put("SWEDISH","sv");
        hashMap.put("TAMIL","ta");
        hashMap.put("TELUGU","te-IN");
        hashMap.put("THAI","th");
        hashMap.put("TURKISH","tr");
        hashMap.put("UKRAINIAN","uk");
        hashMap.put("URDU","ur-PK");
        hashMap.put("VIETNAMESE","vi");
    }


}
