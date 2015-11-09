package com.bronshteyn.android.pingo.model;

import java.io.IOException;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bronshteyn.android.pingo.R;
import com.bronshteyn.android.pingo.data.CircularLinkedList;
import com.bronshteyn.cardsgame.Cardsgame;
import com.bronshteyn.cardsgame.model.HitRequest;
import com.bronshteyn.cardsgame.model.HitResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

public class Pingo {

	private int position;
	private int number;
	private Boolean selected;
	private Boolean loss;
	private Boolean win;
	private Boolean set = false;
	private ImageView view;
	private Boolean canPlay = true;
	private ObjectAnimator animation;
	private HitCallback hitCallback;

	private CircularLinkedList<Integer> numbers;
	private int spin;

	public interface AnimatorCallback {
		public void onAnimationEnd();

		public void onAnimationStart();
	}

	public interface HitCallback {
		public void onHitComplete();
	}

	public Pingo(int position, int number, Boolean selected, Boolean loss, Boolean win, ImageView view) {

		spin = R.raw.spin;

		this.position = position;
		this.number = number;
		this.selected = selected;
		this.loss = loss;
		this.win = win;
		this.view = view;

		numbers = new CircularLinkedList<Integer>(0);
		numbers.add(1);
		numbers.add(2);
		numbers.add(3);
		numbers.add(4);
		numbers.add(5);
		numbers.add(6);
		numbers.add(7);
		numbers.add(8);
		numbers.add(9);

		setUI();
	}

	private void setUI() {

		Drawable face = ResourcesCache.pingoFaces.get(number);
		view.setImageDrawable(face);

		if (selected) {
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.SELECTED.toString()));
		} else if (loss) {
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.LOST.toString()));
		} else if (win) {
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.WON.toString()));
		} else {
			view.setBackground(null);
		}

	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {

		this.number = number;
		Drawable face = ResourcesCache.pingoFaces.get(number);
		view.setImageDrawable(face);

		loss = false;

		if (number != 10) {
			set = true;
		} else {
			set = false;
		}
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Boolean getLoss() {
		return loss;
	}

	public void setLoss(Boolean los) {
		this.loss = los;
	}

	public Boolean getWin() {
		return win;
	}

	public void setWin(Boolean win) {
		this.win = win;
	}

	public Boolean isSet() {
		return set;
	}

	public Boolean getCanPlay() {
		return canPlay;
	}

	public void setCanPlay(Boolean canPlay) {
		this.canPlay = canPlay;
	}

	public void rotate(String axis, int duration, int count, int angleFrom, int angleTo, final AnimatorCallback callback) {

		animation = ObjectAnimator.ofFloat(view, axis, angleFrom, angleTo);

		animation.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				if (callback != null) {
					callback.onAnimationStart();
				}
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (callback != null) {
					callback.onAnimationEnd();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});

		animation.setDuration(duration);
		animation.setRepeatCount(count);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.start();

	}

	public void stopRotation() {

		if (animation != null) {
			animation.end();

		}
	}

	public void select(boolean select) {

		selected = select;

		if (selected) {
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.SELECTED.toString()));
		} else if (loss) {
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.LOST.toString()));
		} else if (win) {
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.WON.toString()));
		} else {
			view.setBackground(null);
		}

	}

	private void won() {
		win = true;
		loss = false;
		view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.WON.toString()));
		canPlay = false;
	}

	private void lost() {
		loss = true;
		win = false;
		view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.LOST.toString()));
		number = 10;

		Integer node = numbers.remove();
		node = null;

		Drawable face = ResourcesCache.pingoFaces.get(number);
		view.setImageDrawable(face);
		set = false;
	}

	public void pingoHit(HitCallback hitCallback, Context context, ProgressBar progressBar) {

		this.hitCallback = hitCallback;

		if (canPlay) {
			Game game = Game.getInstance();
			HitAsyncTask hitTask = new HitAsyncTask(context, progressBar);
			hitTask.execute(game);
		} else {
			hitCallback.onHitComplete();
		}
	}

	private class HitAsyncTask extends AsyncTask<Game, Integer, HitResponse> {

		private Context context;
		private MediaPlayer mp;
		private ProgressBar progressBar;

		public HitAsyncTask(Context context, ProgressBar progressBar) {
			this.context = context;
			this.progressBar = progressBar;
		}

		@Override
		protected HitResponse doInBackground(Game... games) {

			HitResponse response = null;

			mp = MediaPlayer.create(context, spin);
			mp.setLooping(true);
			mp.start();

			Game game = games[0];
			// create hit
			HitRequest hit = new HitRequest();
			hit.setCardID(game.getCardId());
			hit.setAuthToken(game.getAuthTocken());
			hit.setPositionNumber(position);
			hit.setPositionValue(number);

			try {
				Cardsgame.Builder builder = new Cardsgame.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
				Cardsgame service = builder.build();
				response = service.hit(hit).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// simulate delay
			for (int i = 0; i < progressBar.getMax(); i++) {
				publishProgress(i);

				// Do some long loading things
				try {
					Thread.sleep(100);
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
		protected void onPostExecute(HitResponse result) {

			if (mp != null && mp.isPlaying() && mp.isLooping()) {

				mp.stop();
				mp.release();

			}

			stopRotation();
			hitCallback.onHitComplete();

			Game game = Game.getInstance();

			if (result.getGuessed()) {
				won();
				game.setGuesses(game.getGuesses() + 1);
			} else {
				lost();
			}

			switch (position) {
			case 1:
				game.setTriesLeft1(4 - result.getTryNumber());
				break;
			case 2:
				game.setTriesLeft2(4 - result.getTryNumber());
				break;
			case 3:
				game.setTriesLeft3(4 - result.getTryNumber());
				break;
			case 4:
				game.setTriesLeft4(4 - result.getTryNumber());
				break;
			}
		}
	}

	public int getNextPingoNumber() {

		Integer numberIndex;

		if (number == 10) {
			numberIndex = numbers.getTail();
		} else {
			numberIndex = numbers.getNextNode();
		}

		return numberIndex;
	}

	public int getPreviousPingoNumber() {

		Integer numberIndex;

		if (number == 10) {
			numberIndex = numbers.getTail();
			numberIndex = numbers.getPreviousNode();
		} else {
			numberIndex = numbers.getPreviousNode();
		}

		return numberIndex;
	}

}
