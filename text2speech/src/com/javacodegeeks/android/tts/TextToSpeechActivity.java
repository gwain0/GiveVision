package com.javacodegeeks.android.tts;

import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
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
        recognisedText = intent.getStringExtra(SpeechToTextActivity.EXTRA_MESSAGE);
        
        setContentView(R.layout.main);

        tts = new TextToSpeech (TextToSpeechActivity.this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR){
					tts.setLanguage(Locale.UK);
					}}});  

	    Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
    }

	@Override
	protected void onResume(){
			super.onResume();
			//if (text!=null && text.length()>0) {
				//Toast.makeText(TtsActivity.this, "Saying: " + text, Toast.LENGTH_LONG).show();
			tts.speak(recognisedText, TextToSpeech.QUEUE_FLUSH, null);
			
	}
	
	

	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				//tts = new TextToSpeech(this, this);
				setResult(SPEECH_SENT);
				tts.shutdown();
				finish();
			} 
			else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}
	
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
	

	
}