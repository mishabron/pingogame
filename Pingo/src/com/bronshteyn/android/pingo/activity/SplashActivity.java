package com.bronshteyn.android.pingo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.bronshteyn.android.pingo.R;
import com.bronshteyn.android.pingo.model.ResourcesCache;

public class SplashActivity extends Activity {
	
	private ProgressBar progressBar;		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		progressBar = (ProgressBar) findViewById(R.id.splashProgress);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SplashAsyncTask splashTask = new SplashAsyncTask(progressBar,SplashActivity.this);
		splashTask.execute("");
								
	}
	
	private class SplashAsyncTask extends AsyncTask<String, Integer, String>{

		private Context context;
		private ProgressBar progressBar;		
				
		public SplashAsyncTask(ProgressBar progressBar, Context context) {
			this.context = context;
			this.progressBar = progressBar;
		}

		@Override
		protected String doInBackground(String... params) {
			
			ResourcesCache.init(getResources());
			
			for(int i=0; i<progressBar.getMax();i++){
				publishProgress(i);
				
				// Do some long loading things
	            try { 
	            	Thread.sleep(50); 
	            } 
	            catch (InterruptedException ignore) {
	            	
	            }				
			}			
			return null;
		}

	    @Override
	    protected void onProgressUpdate(Integer... values) {
	        super.onProgressUpdate(values);
	        progressBar.setProgress(values[0]);
	    }
	 
	    
		@Override
		protected void onPostExecute(String result) {
	        super.onPostExecute(result);
			Intent intent = new Intent(getApplicationContext(), AuthActivity.class);				
			startActivity(intent);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);	
			Activity activity = (Activity) context;	 
			activity.finish();			
		}

		
		
	}
}
