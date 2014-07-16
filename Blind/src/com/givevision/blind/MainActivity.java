package com.givevision.blind;



import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private WakeLock m_wakeLock;
	public int m_interval = 10 * 1000; // Default (Also read during onCreate from m_settingsFile)
	private Handler m_handler;
	
	private Runnable m_photoLooper = new Runnable(){
         @Override 
         public void run() {
             Log.e("Cam", "Taking picture");
             takePicture();
             Log.e("Cam", "Recursive call. With delay of " + m_interval + "ms.ad");
             m_handler.postDelayed(m_photoLooper, m_interval);
         }
    };
    private void takePicture() {
        if (inPreview) {
          camera.takePicture(null, null, photoCallback);
          inPreview=false;
        }
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
