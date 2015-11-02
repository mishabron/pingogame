package com.bronshteyn.android.pingo.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bronshteyn.android.pingo.R;
import com.bronshteyn.android.pingo.model.Pingo;
import com.bronshteyn.android.pingo.model.Pingo.AnimatorCallnack;
import com.bronshteyn.android.pingo.model.Pingo.HitCallback;
import com.bronshteyn.android.pingo.util.ProgressIndicator;

public class GameActivity extends Activity {

	private final String ROTATE_VERTICAL = "rotationY";
	private final String ROTATE_HORIZONTAL = "rotationX";
	private Pingo pingo1;
	private Pingo pingo2;
	private Pingo pingo3;
	private Pingo pingo4;
	private int activePingo;
	private final int LIMIT = 3;
	private final int NUMBERS = 9;
	private Pingo[] pingos = new Pingo[4];

	private int curentNUmber;
	private Button hitButton;

	private Handler mHandler = new Handler();
	private ProgressIndicator progressIndicator;
	private ProgressBar progressBar;
	private int numberSelect;
	private int digitSelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		digitSelect = R.raw.number_select;
		numberSelect = R.raw.digit_select;

		pingo1 = new Pingo(1, 10, false, false, false,
				(ImageView) findViewById(R.id.pingo1));
		pingo2 = new Pingo(2, 10, false, false, false,
				(ImageView) findViewById(R.id.pingo2));
		pingo3 = new Pingo(3, 10, false, false, false,
				(ImageView) findViewById(R.id.pingo3));
		pingo4 = new Pingo(4, 10, false, false, false,
				(ImageView) findViewById(R.id.pingo4));

		pingos[0] = pingo1;
		pingos[1] = pingo2;
		pingos[2] = pingo3;
		pingos[3] = pingo4;

		activePingo = 0;

		progressBar = (ProgressBar) findViewById(R.id.gameProgress);
		progressIndicator = new ProgressIndicator(progressBar, mHandler);

		// define game controls
		final Button nextButton = (Button) findViewById(R.id.selectNext);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(numberSelect);
				if (activePingo < LIMIT) {
					pingos[activePingo].select(false);
					activePingo++;
					pingos[activePingo].select(true);
				}
			}
		});

		nextButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				nextButton
						.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				;
				return false;
			}
		});

		final Button previousButton = (Button) findViewById(R.id.selectPrevious);
		previousButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(numberSelect);
				if (activePingo > 0) {
					pingos[activePingo].select(false);
					activePingo--;
					pingos[activePingo].select(true);
				}

			}
		});

		previousButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				previousButton
						.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});

		final Button upButton = (Button) findViewById(R.id.upNumber);
		upButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(digitSelect);
				if (pingos[activePingo].getCanPlay()) {
					curentNUmber = pingos[activePingo].getNumber();
					if (curentNUmber == NUMBERS + 1 || curentNUmber == NUMBERS) {
						curentNUmber = 0;
					} else {
						curentNUmber++;
					}

					AnimatorCallnack flippCallback = new AnimatorCallnack() {
						@Override
						public void onAnimationEnd() {
							setFace(curentNUmber);
						}

						@Override
						public void onAnimationStart() {

						}
					};

					pingos[activePingo].rotate(ROTATE_HORIZONTAL, 500, 0, 0,
							360, flippCallback);
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
					curentNUmber = pingos[activePingo].getNumber();
					if (curentNUmber == NUMBERS + 1 || curentNUmber == 0) {
						curentNUmber = 9;
					} else {
						curentNUmber--;
					}

					AnimatorCallnack flippCallback = new AnimatorCallnack() {
						@Override
						public void onAnimationEnd() {
							setFace(curentNUmber);
						}

						@Override
						public void onAnimationStart() {

						}
					};
					pingos[activePingo].rotate(ROTATE_HORIZONTAL, 500, 0, 360,
							0, flippCallback);
				}
			}
		});

		downButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				downButton
						.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});

		hitButton = (Button) findViewById(R.id.hit);
		hitButton.setOnClickListener(new OnClickListener() {

			Thread progressThread;

			final HitCallback hitCallback3 = new HitCallback() {

				@Override
				public synchronized void onHitComplete() {
					progressThread.interrupt();
					activePingo = 0;
					pingos[activePingo].select(true);
				}
			};
			final HitCallback hitCallback2 = new HitCallback() {

				@Override
				public synchronized void onHitComplete() {

					progressThread.interrupt();

					progressThread = new Thread(progressIndicator);
					progressThread.start();
					if (pingos[3].getCanPlay()) {
						pingos[3]
								.rotate(ROTATE_VERTICAL, 500, -1, 360, 0, null);
					}
					pingos[3].pingoHit(4, hitCallback3);

				}
			};
			final HitCallback hitCallback1 = new HitCallback() {

				@Override
				public synchronized void onHitComplete() {
					progressThread.interrupt();
					progressThread = new Thread(progressIndicator);
					progressThread.start();
					if (pingos[2].getCanPlay()) {
						pingos[2]
								.rotate(ROTATE_VERTICAL, 500, -1, 360, 0, null);
					}
					pingos[2].pingoHit(3, hitCallback2);
				}
			};
			final HitCallback hitCallback0 = new HitCallback() {

				@Override
				public synchronized void onHitComplete() {
					progressThread.interrupt();

					progressThread = new Thread(progressIndicator);
					progressThread.start();
					if (pingos[1].getCanPlay()) {
						pingos[1]
								.rotate(ROTATE_VERTICAL, 500, -1, 360, 0, null);
					}
					pingos[1].pingoHit(2, hitCallback1);
				}
			};

			@Override
			public void onClick(View v) {

				hitButton.setEnabled(false);
				progressThread = new Thread(progressIndicator);

				progressThread.start();
				if (pingos[0].getCanPlay()) {
					pingos[0].rotate(ROTATE_VERTICAL, 500, -1, 360, 0, null);
				}
				pingos[0].pingoHit(1, hitCallback0);
			}

		});

		hitButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hitButton
						.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});

		hitButton.setEnabled(false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		final AnimatorCallnack spinn1Callback4 = new AnimatorCallnack() {
			@Override
			public void onAnimationEnd() {

			}

			@Override
			public void onAnimationStart() {

			}
		};

		final AnimatorCallnack spinn1Callback3 = new AnimatorCallnack() {
			@Override
			public void onAnimationEnd() {

			}

			@Override
			public void onAnimationStart() {

				pingo4.rotate(ROTATE_HORIZONTAL, 400, 7, 90, 360,
						spinn1Callback4);
			}
		};

		final AnimatorCallnack spinn1Callback2 = new AnimatorCallnack() {
			@Override
			public void onAnimationEnd() {

			}

			@Override
			public void onAnimationStart() {
				pingo3.rotate(ROTATE_HORIZONTAL, 400, 6, 20, 360,
						spinn1Callback3);
			}
		};

		final AnimatorCallnack spinn1Callback1 = new AnimatorCallnack() {
			@Override
			public void onAnimationEnd() {
				pingos[activePingo].select(true);
			}

			@Override
			public void onAnimationStart() {
				pingo2.rotate(ROTATE_HORIZONTAL, 400, 4, 90, 360,
						spinn1Callback2);

			}
		};

		pingo1.rotate(ROTATE_HORIZONTAL, 400, 2, 0, 360, spinn1Callback1);
	}

	private void setFace(int number) {
		pingos[activePingo].setNumber(number);

		if (pingos[0].isSet() && pingos[1].isSet() && pingos[2].isSet()
				&& pingos[3].isSet()) {
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
}
