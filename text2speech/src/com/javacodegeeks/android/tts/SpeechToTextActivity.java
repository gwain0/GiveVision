package com.javacodegeeks.android.tts;

import java.util.List;

public class SpeechToTextActivity extends Activity {

	private static final int SPEECH_REQUEST = 0;
	private static final int SPEECH_SENT = 1;

    public final static String EXTRA_MESSAGE = "com.givevision.recognisedText";
    private final Class<?> classToSendTo = TextToSpeechActivity.class;

    
    private String spokenText;
    
	private void displaySpeechRecognizer() {
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    startActivityForResult(intent, SPEECH_REQUEST);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
    	Log.i("speechToTextActivity","This is OnActivityResult");

	    if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
		        List<String> results = data.getStringArrayListExtra(
		                RecognizerIntent.EXTRA_RESULTS);
		        spokenText = results.get(0);
		        Log.i("speechToTextActivity",spokenText);
		        // Do something with spokenText.
		        Intent intent = new Intent(this, classToSendTo);
			    intent.putExtra("spokenText", spokenText);
			    startActivityForResult(intent,SPEECH_SENT);
		    	Log.i("speechToTextActivity","Starting TTS");
			   // finish();
	     }
	    if (requestCode == SPEECH_SENT && resultCode == RESULT_OK){
	    	Log.i("speechToTextActivity","returned from TTS");
	        	Log.i("speechToTextActivity","speech is ok!");
	        	Intent result = new Intent();
	        	result.putExtra("spokenText", spokenText);
	        	finish();
	    }    
		super.onActivityResult(requestCode, resultCode, data);

	    
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		displaySpeechRecognizer();
	}
	