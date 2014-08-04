package com.javacodegeeks.android.tts;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SpeechToTextActivity extends Activity {

	private static final int SPEECH_REQUEST = 0;
	private static final int SPEECH_SENT = 1;

    public final static String EXTRA_MESSAGE = "com.givevision.recognisedText";

	private void displaySpeechRecognizer() {
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    startActivityForResult(intent, SPEECH_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
		        List<String> results = data.getStringArrayListExtra(
		                RecognizerIntent.EXTRA_RESULTS);
		        String spokenText = results.get(0);
		        Log.i("speechToTextActivity",spokenText);
		       // if(spokenText.isEmpty()){finish();}
		        // Do something with spokenText.
		        Intent intent = new Intent(this, TextToSpeechActivity.class);
			    intent.putExtra(EXTRA_MESSAGE, spokenText);
			    startActivityForResult(intent,SPEECH_SENT);
			    finish();
	     }
	    if (requestCode == SPEECH_SENT && resultCode == RESULT_OK){
	        	Log.i("speechToTextActivity","speech is ok!");
	        	finish();
	    }    
	    
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_speech_to_text);
		displaySpeechRecognizer();
	}
	
}
