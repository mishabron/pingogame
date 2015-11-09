package com.bronshteyn.android.pingo.activity;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bronshteyn.android.pingo.R;
import com.bronshteyn.android.pingo.model.Game;
import com.bronshteyn.android.pingo.model.Pingo;
import com.bronshteyn.android.pingo.model.Pingo.AnimatorCallback;
import com.bronshteyn.android.pingo.model.Pingo.HitCallback;
import com.bronshteyn.cardsgame.Cardsgame;
import com.bronshteyn.cardsgame.model.Card;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

@SuppressLint("ClickableViewAccessibility")
public class GameActivity extends Activity {

	private final String ROTATE_VERTICAL = "rotationY";
	private final String ROTATE_HORIZONTAL = "rotationX";
	private Pingo pingo1;
	private Pingo pingo2;
	private Pingo pingo3;
	private Pingo pingo4;
	private int activePingo;
	private final int LIMIT = 3;
	private Pingo[] pingos = new Pingo[4];

	private int currentNumber;
	private Button hitButton;

	private ProgressBar progressBar;
	private int numberSelect;
	private int digitSelect;
	private TextView counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/ftrosecu.ttf");

		Game game = Game.getInstance();
		counter = (TextView) findViewById(R.id.counter);
		counter.setTypeface(font);
		counter.setText("" + game.getTrials());

		progressBar = (ProgressBar) findViewById(R.id.gameProgress);

		digitSelect = R.raw.number_select;
		numberSelect = R.raw.digit_select;

		pingo1 = new Pingo(1, 10, false, false, false, (ImageView) findViewById(R.id.pingo1));
		pingo2 = new Pingo(2, 10, false, false, false, (ImageView) findViewById(R.id.pingo2));
		pingo3 = new Pingo(3, 10, false, false, false, (ImageView) findViewById(R.id.pingo3));
		pingo4 = new Pingo(4, 10, false, false, false, (ImageView) findViewById(R.id.pingo4));

		pingos[0] = pingo1;
		pingos[1] = pingo2;
		pingos[2] = pingo3;
		pingos[3] = pingo4;

		activePingo = 0;

		// define game controls
		final Button resetButton = (Button) findViewById(R.id.reset);
		resetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				Activity activity = (Activity) GameActivity.this;
				activity.finish();
			}
		});

		resetButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				resetButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});

		final Button nextButton = (Button) findViewById(R.id.selectNext);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pingos[activePingo].select(false);
				int nextPingo = getNextPingo(activePingo);
				if (nextPingo != activePingo) {
					activePingo = nextPingo;
					playSound(numberSelect);
				}
				pingos[activePingo].select(true);
			}
		});

		nextButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				nextButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});

		final Button previousButton = (Button) findViewById(R.id.selectPrevious);
		previousButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pingos[activePingo].select(false);
				int previousPingo = getPreviousPingo(activePingo);
				if (previousPingo != activePingo) {
					activePingo = previousPingo;
					playSound(numberSelect);
				}
				pingos[activePingo].select(true);
			}
		});

		previousButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				previousButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});

		final Button upButton = (Button) findViewById(R.id.upNumber);
		upButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(digitSelect);
				if (pingos[activePingo].getCanPlay()) {

					currentNumber = pingos[activePingo].getNextPingoNumber();

					AnimatorCallback flippCallback = new AnimatorCallback() {
						@Override
						public void onAnimationEnd() {
							setFace(currentNumber);
						}

						@Override
						public void onAnimationStart() {

						}
					};

					pingos[activePingo].rotate(ROTATE_HORIZONTAL, 500, 0, 0, 360, flippCallback);
				}
			}
		});

		upButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				upButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});

		final Button downButton = (Button) findViewById(R.id.downNumber);
		downButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(digitSelect);
				if (pingos[activePingo].getCanPlay()) {

					currentNumber = pingos[activePingo].getPreviousPingoNumber();

					AnimatorCallback flippCallback = new AnimatorCallback() {
						@Override
						public void onAnimationEnd() {
							setFace(currentNumber);
						}

						@Override
						public void onAnimationStart() {

						}
					};
					pingos[activePingo].rotate(ROTATE_HORIZONTAL, 500, 0, 360, 0, flippCallback);
				}
			}
		});

		downButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				downButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});

		hitButton = (Button) findViewById(R.id.hit);
		hitButton.setOnClickListener(new OnClickListener() {

			final HitCallback hitCallback3 = new HitCallback() {

				@Override
				public synchronized void onHitComplete() {
					progressBar.setProgress(0);
					activePingo = getNextPingo(-1);
					pingos[activePingo].select(true);

					// reset counter
					final Game game = Game.getInstance();

					Animation slideOutBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out_bottom);
					slideOutBottom.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							counter.setText("" + game.getTrials());
							Animation slideInTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_top);
							counter.startAnimation(slideInTop);
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
						}

					});
					counter.startAnimation(slideOutBottom);

					if (game.getTrials() == 0) {
						disableControlls();
					}
				}

				private void disableControlls() {

					nextButton.setEnabled(false);
					downButton.setEnabled(false);
					upButton.setEnabled(false);
					previousButton.setEnabled(false);

					hitButton.setEnabled(true);
					hitButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							Game game = Game.getInstance();
							ResultAsyncTask result = new ResultAsyncTask();
							result.execute(game);
							hitButton.setEnabled(false);
						}
					});

				}
			};

			final HitCallback hitCallback2 = new HitCallback() {

				@Override
				public synchronized void onHitComplete() {
					progressBar.setProgress(0);
					if (pingos[3].getCanPlay()) {
						pingos[3].rotate(ROTATE_HORIZONTAL, 450, -1, 360, 0, null);
					}
					pingos[3].pingoHit(hitCallback3, GameActivity.this, progressBar);

				}
			};
			final HitCallback hitCallback1 = new HitCallback() {

				@Override
				public synchronized void onHitComplete() {
					progressBar.setProgress(0);
					if (pingos[2].getCanPlay()) {
						pingos[2].rotate(ROTATE_HORIZONTAL, 450, -1, 360, 0, null);
					}
					pingos[2].pingoHit(hitCallback2, GameActivity.this, progressBar);
				}
			};
			final HitCallback hitCallback0 = new HitCallback() {

				@Override
				public synchronized void onHitComplete() {
					progressBar.setProgress(0);
					if (pingos[1].getCanPlay()) {
						pingos[1].rotate(ROTATE_HORIZONTAL, 450, -1, 360, 0, null);
					}
					pingos[1].pingoHit(hitCallback1, GameActivity.this, progressBar);
				}
			};

			@Override
			public void onClick(View v) {

				hitButton.setEnabled(false);
				if (pingos[0].getCanPlay()) {
					pingos[0].rotate(ROTATE_HORIZONTAL, 450, -1, 360, 0, null);
				}
				pingos[0].pingoHit(hitCallback0, GameActivity.this, progressBar);
			}

		});

		hitButton.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hitButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});

		hitButton.setEnabled(false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		final AnimatorCallback spinn1Callback4 = new AnimatorCallback() {
			@Override
			public void onAnimationEnd() {

			}

			@Override
			public void onAnimationStart() {

			}
		};

		final AnimatorCallback spinn1Callback3 = new AnimatorCallback() {
			@Override
			public void onAnimationEnd() {

			}

			@Override
			public void onAnimationStart() {

				pingo4.rotate(ROTATE_HORIZONTAL, 400, 7, 90, 360, spinn1Callback4);
			}
		};

		final AnimatorCallback spinn1Callback2 = new AnimatorCallback() {
			@Override
			public void onAnimationEnd() {

			}

			@Override
			public void onAnimationStart() {
				pingo3.rotate(ROTATE_HORIZONTAL, 400, 6, 20, 360, spinn1Callback3);
			}
		};

		final AnimatorCallback spinn1Callback1 = new AnimatorCallback() {
			@Override
			public void onAnimationEnd() {
				pingos[activePingo].select(true);
			}

			@Override
			public void onAnimationStart() {
				pingo2.rotate(ROTATE_HORIZONTAL, 400, 4, 90, 360, spinn1Callback2);

			}
		};

		pingo1.rotate(ROTATE_HORIZONTAL, 400, 2, 0, 360, spinn1Callback1);
	}

	private void setFace(int number) {
		pingos[activePingo].setNumber(number);

		if (pingos[0].isSet() && pingos[1].isSet() && pingos[2].isSet() && pingos[3].isSet()) {
			hitButton.setEnabled(true);
		}
	}

	protected void playSound(int sound) {

		MediaPlayer mp = MediaPlayer.create(GameActivity.this, sound);
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
				mp = null;
			}
		});
		mp.start();
	}

	private int getNextPingo(int currentPingo) {

		int nextPingo = currentPingo;
		boolean hasNext = false;

		for (int i = currentPingo + 1; i <= LIMIT && !hasNext; i++) {
			if (pingos[i].getCanPlay()) {
				hasNext = true;
				nextPingo = i;
			}
		}
		return nextPingo;
	}

	private int getPreviousPingo(int currentPingo) {

		int previousPingo = currentPingo;
		boolean hasNext = false;

		for (int i = currentPingo - 1; i >= 0 && !hasNext; i--) {
			if (pingos[i].getCanPlay()) {
				hasNext = true;
				previousPingo = i;
			}
		}
		return previousPingo;
	}

	private class ResultAsyncTask extends AsyncTask<Game, Integer, Card> {

		@Override
		protected Card doInBackground(Game... games) {

			Card response = null;
			Game game = games[0];

			try {
				Cardsgame.Builder builder = new Cardsgame.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
				Cardsgame service = builder.build();
				response = service.getPlayedCard(game.getCardId()).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// simulate delay
			for (int i = 0; i < progressBar.getMax(); i++) {
				publishProgress(i);

				// Do some long loading things
				try {
					Thread.sleep(50);
				} catch (InterruptedException ignore) {
				}
			}
			return response;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Card result) {
			progressBar.setProgress(0);

			if (pingos[0].getLoss()) {
				pingos[0].rotate(ROTATE_HORIZONTAL, 700, 2, 360, 0, null);
				pingos[0].setNumber(result.getNumber1());
			}
			if (pingos[1].getLoss()) {
				pingos[1].rotate(ROTATE_HORIZONTAL, 700, 2, 360, 0, null);
				pingos[1].setNumber(result.getNumber2());
			}
			if (pingos[2].getLoss()) {
				pingos[2].rotate(ROTATE_HORIZONTAL, 700, 2, 360, 0, null);
				pingos[2].setNumber(result.getNumber3());
			}
			if (pingos[3].getLoss()) {
				pingos[3].rotate(ROTATE_HORIZONTAL, 700, 2, 360, 0, null);
				pingos[3].setNumber(result.getNumber4());
			}

		}
	}

}
