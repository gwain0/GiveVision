package com.javacodegeeks.android.tts;

import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextToSpeechActivity extends Activity implements OnInitListener {
	
		
	private int MY_DATA_CHECK_CODE = 0;
	private static final int SPEECH_SENT = 1;
	private TextToSpeech tts;
	private String recognisedText;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
    
        Intent intent = getIntent();
        recognisedText = intent.getStringExtra("spokenText");
        Log.i("TextToSpeechActivity","recognisedText = "+recognisedText);

        tts = new TextToSpeech (TextToSpeechActivity.this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR){
					tts.setLanguage(Locale.UK);	
					tts.speak(recognisedText, TextToSpeech.QUEUE_FLUSH, null);
					setResult(RESULT_OK);
					while(tts.isSpeaking());
				       tts.shutdown();
						finish();
					}}});  
               
	//    Intent checkIntent = new Intent();
	//	checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	//	startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
		
    }

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
	

	

	
}