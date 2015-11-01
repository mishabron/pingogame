package com.bronshteyn.android.pingo.model;

import java.io.IOException;

import com.bronshteyn.cardsgame.Cardsgame;
import com.bronshteyn.cardsgame.model.AuthinticateResponse;
import com.bronshteyn.cardsgame.model.Card;
import com.bronshteyn.cardsgame.model.HitRequest;
import com.bronshteyn.cardsgame.model.HitResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

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

	public interface AnimatorCallnack{		
		public void onAnimationEnd();
		public void onAnimationStart();		
	}
	
	public interface HitCallback{	
		public void onHitComplete();
	}
	
	public Pingo(int position, int number,Boolean selected, Boolean loss, Boolean win,
			ImageView view) {

		this.position = position;
		this.number = number;
		this.selected = selected;
		this.loss = loss;
		this.win = win;
		this.view = view;
		
		setUI();
	}
	
	private void setUI() {

		Drawable face = ResourcesCache.pingoFaces.get(number);		
		view.setImageDrawable(face);
		
		if(selected){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.SELECTED.toString()));
		}
		else if (loss){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.LOST.toString()));
		}
		else if(win){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.WON.toString()));
		}
		else{
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
		view.refreshDrawableState();
		
		if(number != 10){
			set = true;
		}
		else{
			set = false;
		}
	}
		
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public Boolean getLoss() {
		return loss;
	}
	public void setLoss(Boolean los) {
		this.loss = los;
	}

	public ImageView getView() {
		return view;
	}

	public void setView(ImageView view) {
		this.view = view;
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

	public void setSet(Boolean set) {
		this.set = set;
	}

	public ObjectAnimator getAnimation() {
		return animation;
	}

	public void setAnimation(ObjectAnimator animation) {
		this.animation = animation;
	}

	
	
	public Boolean getCanPlay() {
		return canPlay;
	}

	public void setCanPlay(Boolean canPlay) {
		this.canPlay = canPlay;
	}

	public void rotate(String axis, int duration, int count, int angleFrom, int angleTo, final AnimatorCallnack callback) {

		animation = ObjectAnimator.ofFloat(view, axis, angleFrom, angleTo);	
		
		animation.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {	
				if(callback != null){
					callback.onAnimationStart();
				}
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
					
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if(callback != null){
					callback.onAnimationEnd();
				}
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
		animation.setDuration(duration);
		animation.setRepeatCount(count);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());	
		animation.start();		
		
	}

	public void stopRotation(){
		
		if(animation != null){
			animation.end();

		}
	}
	
	public void select(boolean select) {

		selected = select;
		
		if(selected){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.SELECTED.toString()));
		}
		else if (loss){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.LOST.toString()));
		}
		else if(win){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.WON.toString()));
		}
		else{
			view.setBackground(null);
		}
		
	}
	
	public void won() {
		win = true;		
		view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.WON.toString()));	
		canPlay = false;
	}
	
	private void lost() {
		loss = true;
		view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.LOST.toString()));		
	}	
	
	public void pingoHit(int position, HitCallback hitCallback){
		
		this.hitCallback = hitCallback;
		
		Game game = Game.getInstance();
		
		HitAsyncTask hitTask = new HitAsyncTask();
		hitTask.execute(game);
	}
	
	private class HitAsyncTask extends AsyncTask<Game, Integer, HitResponse>{
		
		@Override
		protected HitResponse doInBackground(Game... games) {
			
			HitResponse response = null;
			
			Game game = games[0];
			//create hit
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
			
			return response;
		}

		@Override
		protected void onPostExecute(HitResponse result) {
			
			stopRotation();
			hitCallback.onHitComplete();
			
			Game game = Game.getInstance();
			
			if(result.getGuessed()){				
				won();
				game.setGuesses(game.getGuesses()+1);  
			}
			else{
				lost();
			}
			
			switch (position) {
			case 1:
				game.setTriesLeft1(4-result.getTryNumber());
				break;
			case 2:
				game.setTriesLeft2(4-result.getTryNumber());				
				break;
			case 3:
				game.setTriesLeft3(4-result.getTryNumber());				
				break;
			case 4:
				game.setTriesLeft4(4-result.getTryNumber());				
				break;				
			}
		}		
	}
	
}
