package com.bronshteyn.android.pingo.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bronshteyn.android.pingo.R;
import com.bronshteyn.android.pingo.model.Game;
import com.bronshteyn.android.pingo.util.ProgressIndicator;
import com.bronshteyn.cardsgame.Cardsgame;
import com.bronshteyn.cardsgame.model.AuthinticateRequest;
import com.bronshteyn.cardsgame.model.AuthinticateResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

public class AuthActivity extends Activity {

	private TextView message = null;
	private ProgressBar progressBar;
	private Handler mHandler = new Handler();
	
	private ProgressIndicator progressIndicator;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_auth);
		
		final EditText cardNumberInput = (EditText)findViewById(R.id.cardNumber);
		message = (TextView)findViewById(R.id.message);
		message.setText("");		
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);	
		
		progressIndicator = new ProgressIndicator(progressBar,mHandler);
		
		final Button authButton = (Button)findViewById(R.id.authButton);		
		authButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(!TextUtils.isEmpty(cardNumberInput.getText())){
					long cardId = Long.parseLong(cardNumberInput.getText().toString());
					AuthinticateAsyncTask authTask = new AuthinticateAsyncTask(AuthActivity.this);
					authTask.execute(cardId);
				}
				
			}
		});
		authButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				authButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});			
        
	}
	
	private class AuthinticateAsyncTask extends AsyncTask<Long, Integer, AuthinticateResponse>{

		private Context context;
		private Long cardId;
		Thread progressThread;
				
		public AuthinticateAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected AuthinticateResponse doInBackground(Long... cardId) {

			AuthinticateResponse response = null;
			
			AuthinticateRequest request = new AuthinticateRequest();
			request.setCardID(cardId[0]);
			
			try {
				//runOnUiThread(progressUpdate);
				progressThread = new Thread(progressIndicator);
				progressThread.start();
				Cardsgame.Builder builder = new Cardsgame.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
				Cardsgame service = builder.build();
				response = service.authinticate(request).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return response;
		}

		@Override
		protected void onPostExecute(AuthinticateResponse result) {

			progressThread.interrupt();
			
			if(result.getAuthinticated()){
				message.setText("Authinticated");
				Game game = Game.getInstance();
				game.init(result);
				Intent intent = new Intent(getApplicationContext(), GameActivity.class);				
				startActivity(intent);
				//overridePendingTransition(R.anim.fade_in, R.anim.fade_out);	
				Activity activity = (Activity) context;
				activity.finish();
			}
			else{
				message.setText("Card Not Valid");
			}
		}
		
	}
}
