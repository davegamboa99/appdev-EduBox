package com.elearneur.edubox.notifications.helper;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;

import java.util.Locale;

public class VoiceNotification extends Service implements TextToSpeech.OnInitListener, OnUtteranceCompletedListener {

    private static TextToSpeech voiceNotification;
    public String spokenText;
    private String eventDetails, eventContent, eventInfo, eventTime, eventDate, eventSpeed, eventData;

    @Override
    public void onCreate() {
        voiceNotification = new TextToSpeech(this,this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        eventDetails = intent.getStringExtra("eventDetails");
        spokenText = eventDetails;
        return START_STICKY;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = voiceNotification.setLanguage(Locale.US);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    voiceNotification.speak(spokenText,TextToSpeech.QUEUE_FLUSH,null,null);
                    Log.d("skip", "onReceive: skipped1");
                } else {
                    voiceNotification.speak(spokenText,TextToSpeech.QUEUE_FLUSH,null);
                    Log.d("skip", "onReceive: skipped");
                }
            }
        }
    }


    @Override
    public void onUtteranceCompleted(String utteranceId) {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (voiceNotification != null) {
            voiceNotification.stop();
            voiceNotification.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
